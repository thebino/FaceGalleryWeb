package de.stuermerbenjamin.facegallery.shared.response

import de.stuermerbenjamin.facegallery.shared.models.Image

data class ImagesResponse(
    val version: String,
    val images: List<Image> = emptyList()
)

