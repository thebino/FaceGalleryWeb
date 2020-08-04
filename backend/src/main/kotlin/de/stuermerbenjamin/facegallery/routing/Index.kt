package de.stuermerbenjamin.facegallery.routing

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.accept
import io.ktor.routing.get

fun Route.index() {
    accept(ContentType.Application.Json) {
        authenticate("apikey") {
            get("/") {
                call.respond("content")
            }
        }
    }

    accept(ContentType.Text.Html) {
        get("/") {
            call.respond(HttpStatusCode.Forbidden, "There is no content for this request")
        }
    }
}
