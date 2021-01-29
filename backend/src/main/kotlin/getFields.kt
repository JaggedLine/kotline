import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getFields(dsl: DSL) {
    route("/getFields") {
        get {
            call.respond(dsl.getFields())
        }
    }
}
