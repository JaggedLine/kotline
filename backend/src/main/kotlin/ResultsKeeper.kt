class Database {
    private val results: MutableMap<Field, MutableList<Pair<List<Coords>, MutableList<String>>>> = mutableMapOf()

    fun insert(claim: Claim): Pair<Boolean, String> {
        val verificationResult = verify(claim)
        if (!verificationResult.first) {
            return verificationResult
        }
        if (claim.field !in results) {
            results[claim.field] = mutableListOf()
        }
        val standings = results[claim.field]!!
        for (i in 0..standings.size) {
            if (i == standings.size || claim.solution.size > standings[i].first.size) {
                standings.add(i, Pair(claim.solution, mutableListOf(claim.name)))
                break
            }
            if (claim.solution == standings[i].first) {
                standings[i].second.add(claim.name)
            }
        }
        return Pair(true, "")
    }

    fun get(field: Field): GetResultsResponse =
        GetResultsResponse(
            when {
                results[field] == null -> emptyList()
                else -> results[field]!!.map { Result(it.first.size, it.second) }
            }
        )
}

val database = Database()
