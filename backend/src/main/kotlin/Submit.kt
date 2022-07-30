import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.submit(dsl: DSL) {
    route("/submit") {
        post {
            val claim: Claim
            try {
                claim = call.receive()
            } catch (e: Throwable) {
                call.respondText("Incorrect data", status = HttpStatusCode.NotAcceptable)
                return@post
            }
            val isGood = dsl.insert(claim)
            if (isGood.first) {
                call.respondText("The result has been received", status = HttpStatusCode.Accepted)
            } else {
                call.respondText(
                    "The result was not admitted, reason: ${isGood.second}",
                    status = HttpStatusCode.NotAcceptable
                )
            }
        }
    }
}
