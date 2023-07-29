import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ServerTests {
    private val field = Field(
        size = FieldSize(5, 5),
        start = Coords(1, 2),
        end = Coords(4, 3)
    )

    private fun testGet(uri: String, status: HttpStatusCode) = testApplication {
        val response = client.get(uri)
        assertEquals(status, response.status)
        println(response.bodyAsText())
    }

    private fun testSequence(solution: List<Coords>, status: HttpStatusCode, name: String = "test") =
        testApplication {
            val response = client.post("/submit") {
                val submission = Submission(name, field, solution)
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Json.encodeToString(submission))
            }
            assertEquals(status, response.status)
            println(response.bodyAsText())
        }

    private fun testGetResults(field: Field, status: HttpStatusCode) {
        testGet(
            "/getResults" +
                    "?rows=${field.size.rows}" +
                    "&columns=${field.size.columns}" +
                    "&startRow=${field.start.row}" +
                    "&startColumn=${field.start.column}" +
                    "&endRow=${field.end.row}" +
                    "&endColumn=${field.end.column}",
            status
        )
    }

    @Test
    fun getFields() {
        testGet("/getFields", HttpStatusCode.OK)
    }

    @Test
    fun submitInvalidJSON() = testApplication {
        val response = client.post("/submit") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"hi": "server"}""")
        }
        assertEquals(HttpStatusCode.NotAcceptable, response.status)
        println(response.bodyAsText())
    }

    @Test
    fun submitEmpty() {
        testSequence(emptyList(), HttpStatusCode.NotAcceptable)
    }

    @Test
    fun submitInvalidStart() {
        testSequence(
            listOf(
                Coords(2, 4),
                Coords(4, 3)
            ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun submitInvalidEnd() {
        testSequence(
            listOf(
                Coords(1, 2),
                Coords(2, 4)
            ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun submitDuplicateNodes() {
        testSequence(
            listOf(
                Coords(1, 2),
                Coords(3, 1),
                Coords(1, 2),
                Coords(2, 4),
                Coords(4, 3)
            ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun submitKnightMove() {
        testSequence(
            listOf(
                Coords(1, 2),
                Coords(4, 3)
            ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun submitNodeBounds() {
        testSequence(
            listOf(
                Coords(1, 2),
                Coords(-1, 3),
                Coords(1, 4),
                Coords(2, 2),
                Coords(4, 3)
            ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun submitIntersecting() {
        testSequence(
            listOf(
                Coords(1, 2),
                Coords(3, 3),
                Coords(1, 4),
                Coords(2, 2),
                Coords(4, 3)
            ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun submitOK1() {
        testSequence(
            listOf(
                Coords(1, 2),
                Coords(0, 4),
                Coords(2, 3),
                Coords(3, 1),
                Coords(4, 3)
            ), HttpStatusCode.Accepted
        )
    }

    @Test
    fun submitOK2() {
        testSequence(
            listOf(
                Coords(1, 2),
                Coords(0, 4),
                Coords(2, 3),
                Coords(4, 4),
                Coords(3, 2),
                Coords(1, 3),
                Coords(2, 1),
                Coords(0, 2),
                Coords(1, 0),
                Coords(3, 1),
                Coords(4, 3)
            ), HttpStatusCode.Accepted
        )
    }

    @Test
    fun getResultsEmpty() {
        testGet("/getResults", HttpStatusCode.NotAcceptable)
    }

    @Test
    fun getResultsNotFound() {
        testGetResults(Field(FieldSize(0, 0), Coords(0, 0), Coords(0, 0)), HttpStatusCode.NotFound)
    }

    @Test
    fun getResultsOK() {
        testGetResults(Field(FieldSize(5, 5), Coords(1, 2), Coords(4, 3)), HttpStatusCode.OK)
    }
}
