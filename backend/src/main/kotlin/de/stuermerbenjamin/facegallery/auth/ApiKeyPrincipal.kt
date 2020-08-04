package de.stuermerbenjamin.facegallery.auth

import io.ktor.auth.Principal

data class ApiKeyPrincipal(val apiKeyCredential: String) : Principal
