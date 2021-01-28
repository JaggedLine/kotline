import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.routing.*
import io.ktor.serialization.*
import org.jetbrains.exposed.sql.Database
import java.io.File

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    val dsl = DSL(
        Database.connect("jdbc:h2:./database/results.db", driver = "org.h2.Driver"),
    )
    routing {
        submit(dsl)
        getResults(dsl)
        getFields(dsl)
        static {
            staticRootFolder = File("../frontend/build/distributions")
            default("index.html")
            files(".")
        }
    }
}
