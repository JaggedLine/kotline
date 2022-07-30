import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.*
import kotlinx.serialization.*

class Tests {
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

    @Test
    fun testGetFields() {
        testGet("/getFields", HttpStatusCode.OK)
    }

    private fun testSequence(solution: List<Coords>, status: HttpStatusCode, name: String = "cookiedoth") =
        testApplication {
            val response = client.post("/submit") {
                val a = Claim(name, field, solution)
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Json.encodeToString(a))
            }
            assertEquals(status, response.status)
            println(response.bodyAsText())
        }

    @Test
    fun incorrectJSON() = testApplication {
        val response = client.post("/submit") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"hi": "server"}""")
        }
        assertEquals(HttpStatusCode.NotAcceptable, response.status)
        println(response.bodyAsText())
    }

    @Test
    fun testSubmit0() {
        testSequence(emptyList(), HttpStatusCode.NotAcceptable)
    }

    @Test
    fun testSubmit1() {
        testSequence(
            listOf(
                Coords(2, 4),
                Coords(4, 3)
            ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun testSubmit2() {
        testSequence(
            listOf(
                Coords(1, 2),
                Coords(2, 4)
            ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun testSubmit3() {
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
    fun testSubmit4() {
        testSequence(
            listOf(
                Coords(1, 2),
                Coords(4, 3)
            ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun testSubmit5() {
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
    fun testSubmit6() {
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
    fun testSubmit7() {
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
    fun testSubmit8() {
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
    fun testSubmit9() {
        testSequence(
            listOf(
                Coords(1, 2),
                Coords(0, 4),
                Coords(2, 3),
                Coords(3, 1),
                Coords(4, 3)
            ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun testResults0() {
        testGet("/getResults", HttpStatusCode.NotAcceptable)
    }

    private fun testResults(
        rows: Int, columns: Int, startRow: Int, startColumn: Int, endRow: Int, endColumn: Int,
        status: HttpStatusCode
    ) {
        testGet(
            "/getResults?rows=$rows&columns=$columns&startRow=$startRow&startColumn=$startColumn&endRow=$endRow&endColumn=$endColumn",
            status
        )
    }

    @Test
    fun testResults1() {
        testResults(0, 0, 0, 0, 0, 0, HttpStatusCode.OK)
    }

    @Test
    fun testResults2() {
        testResults(5, 5, 1, 2, 4, 3, HttpStatusCode.OK)
    }

    @Test
    fun testResults3() {
        testSequence(
            listOf(
                Coords(1, 2),
                Coords(0, 4),
                Coords(2, 3),
                Coords(3, 1),
                Coords(4, 3)
            ), HttpStatusCode.Accepted, "user1"
        )
        testSequence(
            listOf(
                Coords(1, 2),
                Coords(3, 1),
                Coords(4, 3)
            ), HttpStatusCode.Accepted, "user3"
        )
        testSequence(
            listOf(
                Coords(1, 2),
                Coords(2, 0),
                Coords(4, 1),
                Coords(2, 2),
                Coords(4, 3)
            ), HttpStatusCode.Accepted, "user2"
        )
        testResults(5, 5, 1, 2, 4, 3, HttpStatusCode.OK)
    }
}
