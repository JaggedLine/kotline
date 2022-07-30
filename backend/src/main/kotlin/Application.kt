import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
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
