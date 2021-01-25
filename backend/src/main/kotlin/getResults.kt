import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getResults(dsl: DSL) {
    route("/getResults") {
        get {
            val field = call.receive<Field>()
            call.respond(dsl.get(field))
        }
    }
}
