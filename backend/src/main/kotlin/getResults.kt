import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun parseFields(queryParameters: Parameters): Field? {
    for (param in listOf("rows", "columns", "startRow", "startColumn", "endRow", "endColumn")) {
        queryParameters[param] ?: return null
    }
    return Field(
        size = FieldSize(
            queryParameters["rows"]!!.toInt(),
            queryParameters["columns"]!!.toInt()
        ),
        start = Coords(
            queryParameters["startRow"]!!.toInt(),
            queryParameters["startColumn"]!!.toInt()
        ),
        end = Coords(
            queryParameters["endRow"]!!.toInt(),
            queryParameters["endColumn"]!!.toInt()
        )
    )
}

fun Route.getResults(dsl: DSL) {
    route("/getResults") {
        get {
            val field: Field? = parseFields(call.parameters)
            if (field == null) {
                call.respondText("Incorrect query", status = HttpStatusCode.NotAcceptable)
                return@get
            }
            call.respond(dsl.get(field))
        }
    }
}
