import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.submit() {
    route("/submit") {
        post {
            println("here")
            val claim = call.receive<Claim>()
            println("fieldSize: ${claim.field.size.rows}x${claim.field.size.columns}")
            call.respondText("The result has been received", status = HttpStatusCode.Accepted)
        }
    }
}
