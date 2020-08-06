package com.emotionrec.api.responses

import com.emotionrec.api.models.Prediction
import com.emotionrec.api.models.PredictionGroup

data class PredictionResponse(val sortedPredictions: List<Prediction>,
                              val guessedPrediction: Prediction)

fun List<PredictionGroup>.toPredictionResult(): PredictionResponse {
    return PredictionResponse(this[0].sortedPredictions, this[0].sortedPredictions[0])
}