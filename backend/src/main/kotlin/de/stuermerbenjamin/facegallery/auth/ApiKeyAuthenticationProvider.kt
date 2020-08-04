package de.stuermerbenjamin.facegallery.auth

import io.ktor.application.ApplicationCall
import io.ktor.auth.AuthenticationFunction
import io.ktor.auth.AuthenticationProvider
import io.ktor.auth.Principal

class ApiKeyAuthenticationProvider(configuration: Configuration) : AuthenticationProvider(configuration) {
    class Configuration(name: String?) : AuthenticationProvider.Configuration(name) {
        var authenticationFunction: AuthenticationFunction<ApiKeyCredential> = { null }

        fun validate(body: suspend ApplicationCall.(ApiKeyCredential) -> Principal?) {
            authenticationFunction = body
        }
    }

    val authenticationFunction = configuration.authenticationFunction





//        internal var authenticationFunction: suspend ApplicationCall.(ApiKeyCredential) -> Principal? = { null }

//    var apiKeyName: String = ""
//
//    var apiKeyLocation: ApiKeyLocation = ApiKeyLocation.QUERY

    /**
     * Sets a validation function that will check given [ApiKeyCredential] instance and return [Principal],
     * or null if credential does not correspond to an authenticated principal
     */
//    fun validate(body: suspend ApplicationCall.(ApiKeyCredential) -> Principal?) {
//        authenticationFunction = body
//    }
}
