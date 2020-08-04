package de.stuermerbenjamin.facegallery.detection

data class FaceBox(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float,
    val confidenceScore: Float,
    val regressionScale: FloatArray,
    val padding: FacePadding,
    val faceLandmark: FaceLandmark
)
