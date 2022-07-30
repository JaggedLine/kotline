import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getFields(dsl: DSL) {
    route("/getFields") {
        get {
            call.respond(dsl.getFields())
        }
    }
}
