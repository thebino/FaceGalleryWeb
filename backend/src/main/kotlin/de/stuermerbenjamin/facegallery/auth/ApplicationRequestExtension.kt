package de.stuermerbenjamin.facegallery.auth

import io.ktor.request.ApplicationRequest

fun ApplicationRequest.apiKeyAuthenticationCredentials(apiKeyName: String, apiKeyLocation: ApiKeyLocation): ApiKeyCredential? {
    val value: String? = when (apiKeyLocation) {
        ApiKeyLocation.QUERY -> this.queryParameters[apiKeyName]
        ApiKeyLocation.HEADER -> this.headers[apiKeyName]
    }
    return when (value) {
        null -> null
        else -> ApiKeyCredential(value)
    }
}
