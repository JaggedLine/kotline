package kuyanov.kotline.server

import Coords
import Field
import FieldSize
import Submission
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import org.jetbrains.exposed.sql.Database

fun HTML.index() {
    head {
        meta("viewport", "width=device-width, initial-scale=0.85, shrink-to-fit=no")
        title("JaggedLine")
        link("https://fonts.gstatic.com", "preconnect")
        link("https://fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@300&display=swap", "stylesheet")
        link("https://use.fontawesome.com/releases/v5.11.2/css/all.css", "stylesheet")
    }
    body {
        script(src = "/static/kotline.js") {}
    }
}

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    val dsl = DSL(
        Database.connect("jdbc:h2:./database/results.db", driver = "org.h2.Driver"),
    )

    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }

        get("/getFields") {
            call.respond(dsl.getFields())
        }

        get("/getResults") {
            for (param in listOf("rows", "columns", "startRow", "startColumn", "endRow", "endColumn")) {
                if (call.parameters[param] == null) {
                    call.respondText("Invalid query", status = HttpStatusCode.NotAcceptable)
                    return@get
                }
            }
            val field = Field(
                size = FieldSize(
                    call.parameters["rows"]!!.toInt(),
                    call.parameters["columns"]!!.toInt()
                ),
                start = Coords(
                    call.parameters["startRow"]!!.toInt(),
                    call.parameters["startColumn"]!!.toInt()
                ),
                end = Coords(
                    call.parameters["endRow"]!!.toInt(),
                    call.parameters["endColumn"]!!.toInt()
                )
            )
            val results = dsl.getResults(field)
            if (results == null) {
                call.respondText("Results not found", status = HttpStatusCode.NotFound)
                return@get
            }
            call.respond(results)
        }

        post("/submit") {
            val submission: Submission
            try {
                submission = call.receive()
            } catch (e: Throwable) {
                call.respondText("Invalid data", status = HttpStatusCode.NotAcceptable)
                return@post
            }
            val status = dsl.insert(submission)
            if (status.first) {
                call.respondText("The result has been received")
            } else {
                call.respondText(
                    "The result was not admitted, reason: ${status.second}",
                    status = HttpStatusCode.NotAcceptable
                )
            }
        }

        static("/static") {
            resources()
        }
    }
}
