package de.stuermerbenjamin.facegallery.detection

data class ARGB(
    val a: Float,
    val r: Float,
    val g: Float,
    val b: Float
) {
    constructor(r: Double, g: Double, b: Double) : this(
        1.0f,
        ((r - 127.5) * 0.0078125).toFloat(),
        ((g - 127.5) * 0.0078125).toFloat(),
        ((b - 127.5) * 0.0078125).toFloat()
    )
}
