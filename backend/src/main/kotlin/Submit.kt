import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.submit() {
    route("/submit") {
        post {
            val claim = call.receive<Claim>()
            val isGood = database.insert(claim)
            if (isGood.first) {
                call.respondText("The result has been received", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("The result was not admitted, reason: ${isGood.second}", status = HttpStatusCode.NotAcceptable)
            }
        }
    }
}
