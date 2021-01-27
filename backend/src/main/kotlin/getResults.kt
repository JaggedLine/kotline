import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getResults(dsl: DSL) {
    route("/getResults") {
        get {
            val field = Field(
                size = FieldSize(
                    call.parameters["rows"]!!.toInt(),
                    call.parameters["columns"]!!.toInt()
                ),
                start = Coords(
                    call.parameters["startRow"]!!.toInt(),
                    call.parameters["startColumn"]!!.toInt()
                ),
                end = Coords(
                    call.parameters["endRow"]!!.toInt(),
                    call.parameters["endColumn"]!!.toInt()
                )
            )
            println(field)
            call.respond(dsl.get(field))
        }
    }
}
