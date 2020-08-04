package de.stuermerbenjamin.facegallery.auth

import io.ktor.auth.Credential

data class ApiKeyCredential(val value: String) : Credential
