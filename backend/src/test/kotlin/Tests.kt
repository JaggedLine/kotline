import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.*
import kotlinx.serialization.*

class Tests {
    val field = Field(
        size = FieldSize(5, 5),
        start = Coords(0, 0),
        end = Coords(1, 0)
    )

    fun testGet(uri: String, status: HttpStatusCode) {
        withTestApplication({ module() }) {
            handleRequest(HttpMethod.Get, uri).apply {
                assertEquals(status, response.status())
                println(response.content)
            }
        }
    }

    @Test
    fun testGetFields() {
        testGet("/getFields", HttpStatusCode.OK)
    }

    fun testSequence(solution: List<Coords>, status: HttpStatusCode, name: String = "cookiedoth") {
        withTestApplication({ module() }) {
            handleRequest(HttpMethod.Post, "/submit") {
                val a = Claim(name, field, solution)
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Json.encodeToString(a))
            }.apply {
                assertEquals(status, response.status())
                println(response.content)
            }
        }
    }

    @Test
    fun incorrectJSON() {
        withTestApplication({ module() }) {
            handleRequest(HttpMethod.Post, "/submit") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"hi": "server"}""")
            }.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
                println(response.content)
            }
        }
    }

    @Test
    fun testSubmit0() {
        testSequence(emptyList(), HttpStatusCode.NotAcceptable)
    }

    @Test
    fun testSubmit1() {
        testSequence(listOf(
            Coords(0, 2),
            Coords(1, 0)
        ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun testSubmit2() {
        testSequence(listOf(
            Coords(0, 0),
            Coords(1, 2)
        ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun testSubmit3() {
        testSequence(listOf(
            Coords(0, 0),
            Coords(1, 2),
            Coords(3, 3),
            Coords(1, 2),
            Coords(3, 1),
            Coords(1, 0)
        ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun testSubmit4() {
        testSequence(listOf(
            Coords(0, 0),
            Coords(1, 0)
        ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun testSubmit5() {
        testSequence(listOf(
            Coords(0, 0),
            Coords(-1, 0),
            Coords(1, 0)
        ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun testSubmit6() {
        testSequence(listOf(
            Coords(0, 0),
            Coords(2, 1),
            Coords(0, 2),
            Coords(1, 0)
        ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun testSubmit7() {
        testSequence(listOf(
            Coords(0, 0),
            Coords(1, 2),
            Coords(3, 1),
            Coords(1, 0)
        ), HttpStatusCode.Accepted
        )
    }

    @Test
    fun testSubmit8() {
        testSequence(listOf(
            Coords(0, 0),
            Coords(2, 1),
            Coords(0, 2),
            Coords(2, 3),
            Coords(3, 1),
            Coords(1, 0)
        ), HttpStatusCode.Accepted
        )
    }

    @Test
    fun testSubmit9() {
        testSequence(listOf(
            Coords(0, 0),
            Coords(1, 2),
            Coords(3, 1),
            Coords(1, 0)
        ), HttpStatusCode.NotAcceptable
        )
    }

    @Test
    fun testResults0() {
        testGet("/getResults", HttpStatusCode.NotAcceptable)
    }

    fun testResults(rows: Int, columns: Int, startRow: Int, startColumn: Int, endRow: Int, endColumn: Int,
                    status: HttpStatusCode) {
        testGet("/getResults?rows=$rows&columns=$columns&startRow=$startRow&startColumn=$startColumn&endRow=$endRow&endColumn=$endColumn", status)
    }

    @Test
    fun testResults1() {
        testResults(0, 0, 0, 0, 0, 0, HttpStatusCode.OK)
    }

    @Test
    fun testResults2() {
        testResults(5, 5, 0, 0, 1, 0, HttpStatusCode.OK)
    }

    @Test
    fun testResults3() {
        testSequence(listOf(
            Coords(0, 0),
            Coords(2, 1),
            Coords(0, 2),
            Coords(2, 3),
            Coords(3, 1),
            Coords(1, 0)
        ), HttpStatusCode.Accepted, "user1"
        )
        testSequence(listOf(
            Coords(0, 0),
            Coords(1, 2),
            Coords(3, 1),
            Coords(1, 0)
        ), HttpStatusCode.Accepted, "user3"
        )
        testSequence(listOf(
            Coords(0, 0),
            Coords(1, 2),
            Coords(2, 4),
            Coords(4, 3),
            Coords(3, 1),
            Coords(1, 0)
        ), HttpStatusCode.Accepted, "user2"
        )
        testResults(5, 5, 0, 0, 1, 0, HttpStatusCode.OK)
    }
}
