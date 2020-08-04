package de.stuermerbenjamin.facegallery.database.dto

data class Face(
    val id: Int,
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float,
    val confidenceScore: Float,
    val regressionScale: FloatArray = floatArrayOf()
)
