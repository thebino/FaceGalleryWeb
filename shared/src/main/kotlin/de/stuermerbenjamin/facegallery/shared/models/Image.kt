package de.stuermerbenjamin.facegallery.shared.models

data class Image(
    val imagePath: String,
    val faces: List<String> = emptyList()
)
