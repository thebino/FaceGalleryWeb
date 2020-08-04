package de.stuermerbenjamin.facegallery.frontend.routing

import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.login() {
    get("/login") {
        call.respond(FreeMarkerContent("login.ftl", null))
    }
}
