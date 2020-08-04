package de.stuermerbenjamin.facegallery.prediction

import java.text.DecimalFormat

data class Prediction(val probability: Float) {
    override fun toString(): String {
        val formatted = DecimalFormat("##").format(probability * 100)
        return "Prediction{probability=$formatted %}"
    }
}
