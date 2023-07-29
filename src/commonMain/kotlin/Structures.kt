import kotlinx.serialization.Serializable

@Serializable
data class Coords(val row: Int, val column: Int)

@Serializable
data class FieldSize(val rows: Int, val columns: Int)

@Serializable
data class Field(val size: FieldSize, val start: Coords, val end: Coords)

@Serializable
data class Submission(val name: String, val field: Field, val solution: List<Coords>)

@Serializable
data class ResultsEntry(val score: Int, val names: List<String>)

@Serializable
data class Results(val entries: List<ResultsEntry>)

@Serializable
data class WorkerMessage(val polyline: List<Coords>?)
