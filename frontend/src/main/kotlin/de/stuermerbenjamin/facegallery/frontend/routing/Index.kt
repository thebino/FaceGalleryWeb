package de.stuermerbenjamin.facegallery.frontend.routing

import de.stuermerbenjamin.facegallery.frontend.WebAppSession
import de.stuermerbenjamin.facegallery.frontend.services.ImageService
import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.content.files
import io.ktor.http.content.resource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import java.io.File

fun Route.index(imageService: ImageService) {
    static("static") {
        resources("css")
        resources("images")
        resources("js")
        resource("favicon.ico")
    }

    static("images") {
        staticRootFolder = File("/Users/stuermer/workspace/github/thebino/FaceGallery")
        files("images")
    }

    static("font-files") {
        resources("font-files")
    }

    get("/") {
        println("GET /")

        val session = call.sessions.get<WebAppSession>()
        if (session == null) {
            call.respond(FreeMarkerContent("index.ftl", null))
        } else {
            val webAppSession = call.sessions.get<WebAppSession>()

            // TODO: remove API Key from source
            val imagesResponse = imageService.getImages("MVh4d2RTOGkzWmg4NlFTUTJrblBLcWpqemxnM2lwQ2w=")

            println("images: ${imagesResponse.images[0].imagePath}")

            call.respond(
                FreeMarkerContent(
                    "internal.ftl", mapOf(
                        "version" to imagesResponse.version,
                        "user" to webAppSession?.name,
                        "images" to imagesResponse.images
                    )
                )
            )
        }
    }

    authenticate {
        post("/") {
            val principal = call.principal<UserIdPrincipal>()

            if (principal == null) {
                call.respondRedirect("/", permanent = false)
            } else {
                call.sessions.set(WebAppSession(principal.name))
                call.respondRedirect("/", permanent = false)
            }
        }
    }
}
