package de.stuermerbenjamin.facegallery.auth

import io.ktor.application.call
import io.ktor.auth.Authentication
import io.ktor.auth.AuthenticationPipeline
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import io.ktor.request.ApplicationRequest
import io.ktor.request.header
import io.ktor.response.respond

fun Authentication.Configuration.apiKey(name: String? = null, configure: ApiKeyAuthenticationProvider.Configuration.() -> Unit) {
    val provider = ApiKeyAuthenticationProvider(ApiKeyAuthenticationProvider.Configuration(name).apply(configure))
    val authenticateFunction = provider.authenticationFunction

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        val credential = call.request.appIdAuthenticationCredential()
        val principal = credential?.let { authenticateFunction(call, it) }

        val cause = when {
            credential == null -> call.respond(HttpStatusCode.Forbidden)
            principal == null -> call.respond(HttpStatusCode.Unauthorized)
            else -> null
        }

        if (cause != null) {
            // maybe respond with something? probably not...
        }

        principal?.let { context.principal(it) }
    }

    register(provider)

//    val provider = ApiKeyAuthenticationProvider(name).apply(configure)
//    val apiKeyName = provider.apiKeyName
//    val apiKeyLocation = provider.apiKeyLocation
//    val authenticate = provider.authenticationFunction
//
//    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
//        val credentials = call.request.apiKeyAuthenticationCredentials(apiKeyName, apiKeyLocation)
//        val principal = credentials?.let { authenticate(call, it) }
//
//        val cause = when {
//            credentials == null -> AuthenticationFailedCause.NoCredentials
//            principal == null -> AuthenticationFailedCause.InvalidCredentials
//            else -> null
//        }
//
//        if (cause != null) {
//            context.challenge(apiKeyName, cause) {
//                // TODO: Verify correct response structure here.
//                call.respond(
//                    UnauthorizedResponse(
//                        HttpAuthHeader.Parameterized(
//                            "API_KEY",
//                            mapOf("key" to apiKeyName),
//                            HeaderValueEncoding.QUOTED_ALWAYS
//                        )
//                    )
//                )
//                it.complete()
//            }
//        }
//
//        if (principal != null) {
//            context.principal(principal)
//        }
//    }
}


// retrieves App ID Credential for this `ApplicationRequest`
@Suppress("MoveVariableDeclarationIntoWhen")
fun ApplicationRequest.appIdAuthenticationCredential(): ApiKeyCredential? {
    val appId = header("X-API-KEY")
    // we need a valid app ID
    if (appId.isNullOrBlank()) return null
    // we have "something", so let's use this
    return ApiKeyCredential(appId)
}
