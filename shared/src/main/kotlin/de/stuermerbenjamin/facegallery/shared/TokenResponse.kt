package de.stuermerbenjamin.facegallery.shared

data class TokenResponse(
    val access_token: String,
    val refresh_token: String,
    val id_token: String
)
