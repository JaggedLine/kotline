import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.registerRoutes() {
    routing {
        submit()
        getResults()
    }
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    registerRoutes()
}
