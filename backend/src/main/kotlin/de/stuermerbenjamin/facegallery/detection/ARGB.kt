package de.stuermerbenjamin.facegallery.detection

private const val alpha = 0.0078125f
private const val mean = 127.5f

data class ARGB(
    val a: Float,
    val r: Float,
    val g: Float,
    val b: Float
) {
    constructor(r: Double, g: Double, b: Double) : this(
        1.0f,
        ((r - mean) * alpha).toFloat(),
        ((g - mean) * alpha).toFloat(),
        ((b - mean) * alpha).toFloat()
    )
}
