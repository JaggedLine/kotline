import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

data class ResultsTableRow(val id: Int, val solution: List<Coords>, val names: List<String>)

class DSL(private val connection: Database) {
    object ResultsTable : Table() {
        val id: Column<Int> = integer("id").autoIncrement()
        val field: Column<String> = text("field", eagerLoading = true)
        val score: Column<Int> = integer("score")
        val solution: Column<String> = text("solution", eagerLoading = true)
        val names: Column<String> = text("names", eagerLoading = true)
        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(connection) {
            SchemaUtils.create(ResultsTable)
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
                        ResultsTable.deleteWhere { ResultsTable.id eq row.id }
                    }
                } else {
                    transaction(connection) {
                        ResultsTable.update({ResultsTable.id eq row.id}) {
                            it[names] = Json.encodeToString(newNames)
                        }
                    }
                }
            }
        }
        return true
    }

    fun insert(claim: Claim): Pair<Boolean, String> {
        val verificationResult = verify(claim)
        if (!verificationResult.first) {
            return verificationResult
        }

        val fieldString = Json.encodeToString(claim.field)
        val standings = transaction(connection) {
            ResultsTable.select { ResultsTable.field eq fieldString }.orderBy(ResultsTable.score to SortOrder.DESC).map {
                ResultsTableRow(it[ResultsTable.id],
                    Json.decodeFromString(it[ResultsTable.solution]),
                    Json.decodeFromString(it[ResultsTable.names]))
            }
        }

        if (claim.name.isNotEmpty()) {
            if (!removeOccurrences(claim.name, standings, claim.solution.size)) {
                return Pair(false, "You have already got a better result")
            }
        }

        for (i in 0..standings.size) {
            if (i == standings.size || claim.solution.size > standings[i].solution.size) {
                transaction(connection) {
                    ResultsTable.insert {
                        it[field] = fieldString
                        it[score] = claim.solution.size - 1
                        it[solution] = Json.encodeToString(claim.solution)
                        it[names] = Json.encodeToString(mutableListOf(claim.name))
                    }
                }
                break
            }
            if (claim.solution == standings[i].solution) {
                transaction(connection) {
                    ResultsTable.update({ ResultsTable.id eq standings[i].id }) {
                        it[names] = Json.encodeToString(standings[i].names + listOf(claim.name))
                    }
                }
                break
            }
        }
        return Pair(true, "")
    }

    fun get(field: Field): GetResultsResponse {
        val fieldString = Json.encodeToString(field)
        return GetResultsResponse(transaction(connection) {
            ResultsTable.select { ResultsTable.field eq fieldString }
                .orderBy(ResultsTable.score to SortOrder.DESC)
                .map {
                Result(
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
}
