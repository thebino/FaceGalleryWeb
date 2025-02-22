package com.emotionrec.api.models

data class Shape(val columns: Int, val rows: Int)

val INPUT_SHAPE = Shape(48, 48)

data class InferenceInput(val images: List<List<RGB>>, private val shape: Shape = INPUT_SHAPE) {
    init {
        require(images.size == shape.rows)
        require(images.all { it.size == shape.columns })
    }
}

class RGB(val r: Float, val g: Float, val b: Float)
