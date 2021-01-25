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
                    ResultsTable.update({ResultsTable.id eq standings[i].id}) {
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
            ResultsTable.select { ResultsTable.field eq fieldString }.map {
                Result(it[ResultsTable.score], it[Json.decodeFromString(it[ResultsTable.names])])
            }
        })
    }
}
