import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

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
            }
            call.respond(dsl.get(field!!))
        }
    }
}
