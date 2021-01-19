import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getResults() {
    route("/getResults") {
        get {
            val field = call.receive<Field>()
            call.respond(database.get(field))
        }
    }
}
