package org.edc.sic.kirill.recipes

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import org.edc.sic.kirill.recipes.routers.recipes
import java.io.File

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    install(ContentNegotiation) {
        json()
    }

    routing {
        route("/api") {
            staticFiles("/images", File("images"))
            swaggerUI(path = "/docs")
        }
        recipes()
    }

}
