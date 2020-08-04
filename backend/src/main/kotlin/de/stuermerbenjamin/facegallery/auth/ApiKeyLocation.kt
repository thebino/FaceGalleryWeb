package de.stuermerbenjamin.facegallery.auth

enum class ApiKeyLocation(val location: String) {
    QUERY("query"),
    HEADER("header")
}
