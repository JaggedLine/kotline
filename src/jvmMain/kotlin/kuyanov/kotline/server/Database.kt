package kuyanov.kotline.server

import Coords
import Field
import Results
import ResultsEntry
import Submission
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

data class ResultsTableRow(val id: Int, val solution: List<Coords>, val names: List<String>)

class DSL(private val connection: Database) {
    object ResultsTable : Table() {
        val id: Column<Int> = integer("id").autoIncrement()
        val fieldId: Column<Int> = integer("fieldId")
        val score: Column<Int> = integer("score")
        val solution: Column<String> = text("solution", eagerLoading = true)
        val names: Column<String> = text("names", eagerLoading = true)
        override val primaryKey = PrimaryKey(id)
    }

    object FieldsTable : Table() {
        val description: Column<String> = text("description", eagerLoading = true)
        val id: Column<Int> = integer("id").autoIncrement()
        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(connection) {
            SchemaUtils.create(ResultsTable)
        }
        transaction(connection) {
            SchemaUtils.create(FieldsTable)
            if (FieldsTable.selectAll().empty()) {
                val fields: List<Field> = Json.decodeFromString(File("src/jvmMain/resources/fields.json").readText())
                FieldsTable.batchInsert(fields) {
                    this[FieldsTable.description] = Json.encodeToString(it)
                }
            }
        }
    }

    private fun removeOccurrences(name: String, standings: List<ResultsTableRow>, newSize: Int): Boolean {
        for (row in standings) {
            if (name in row.names) {
                if (row.solution.size >= newSize) {
                    return false
                }
                val newNames = row.names.minus(name)
                if (newNames.isEmpty()) {
                    transaction(connection) {
                        ResultsTable.deleteWhere { id eq row.id }
                    }
                } else {
                    transaction(connection) {
                        ResultsTable.update({ ResultsTable.id eq row.id }) {
                            it[names] = Json.encodeToString(newNames)
                        }
                    }
                }
            }
        }
        return true
    }

    private fun getFieldId(fieldString: String): Int? {
        return transaction(connection) {
            FieldsTable.select { FieldsTable.description eq fieldString }.map {
                it[FieldsTable.id]
            }.firstOrNull()
        }
    }

    fun insert(submission: Submission): Pair<Boolean, String> {
        val fieldString = Json.encodeToString(submission.field)
        val curFieldId = getFieldId(fieldString) ?: return Pair(false, "Invalid field")

        val verificationResult = verify(submission)
        if (!verificationResult.first) {
            return verificationResult
        }
        val standings = transaction(connection) {
            ResultsTable.select { ResultsTable.fieldId eq curFieldId }.orderBy(ResultsTable.score to SortOrder.DESC)
                .map {
                    ResultsTableRow(
                        it[ResultsTable.id],
                        Json.decodeFromString(it[ResultsTable.solution]),
                        Json.decodeFromString(it[ResultsTable.names])
                    )
                }
        }

        if (submission.name.isNotEmpty()) {
            if (!removeOccurrences(submission.name, standings, submission.solution.size)) {
                return Pair(true, "")
            }
        }

        for (i in 0..standings.size) {
            if (i == standings.size || submission.solution.size > standings[i].solution.size) {
                transaction(connection) {
                    ResultsTable.insert {
                        it[fieldId] = curFieldId
                        it[score] = submission.solution.size - 1
                        it[solution] = Json.encodeToString(submission.solution)
                        it[names] = Json.encodeToString(listOf(submission.name))
                    }
                }
                break
            }
            if (submission.solution == standings[i].solution) {
                transaction(connection) {
                    ResultsTable.update({ ResultsTable.id eq standings[i].id }) {
                        it[names] = Json.encodeToString(standings[i].names + listOf(submission.name))
                    }
                }
                break
            }
        }
        return Pair(true, "")
    }

    fun getResults(field: Field): Results? {
        val fieldId = getFieldId(Json.encodeToString(field)) ?: return null
        return Results(transaction(connection) {
            ResultsTable.select { ResultsTable.fieldId eq fieldId }
                .orderBy(ResultsTable.score to SortOrder.DESC)
                .map { it ->
                    ResultsEntry(
                        it[ResultsTable.score],
                        Json.decodeFromString<List<String>>(it[ResultsTable.names]).map {
                            when (it) {
                                "" -> "Anonymous"
                                else -> it
                            }
                        }
                    )
                }
        })
    }

    fun getFields(): List<Field> = transaction(connection) {
        FieldsTable.selectAll().map { Json.decodeFromString(it[FieldsTable.description]) }
    }
}
