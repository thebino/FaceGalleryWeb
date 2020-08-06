package com.emotionrec.api

import arrow.core.Try
import localtfinference.JavaUtils
import com.emotionrec.api.models.InferenceInput
import com.emotionrec.api.models.PredictionGroup
import com.emotionrec.api.models.toPredictionGroup
import org.tensorflow.SavedModelBundle

class LocalInferenceService(val getSavedModelBundle: () -> SavedModelBundle) {

    fun getPrediction(inferenceInputs: List<InferenceInput>): Try<List<PredictionGroup>> {
        // TODO: load on every request ?
        val savedModelBundle: SavedModelBundle = getSavedModelBundle()

        return try {
            val inferenceResult = JavaUtils.runInference(
                savedModelBundle.session(),
                inferenceInputs.toLocalInferenceInput()
            )
            val result = inferenceResult.map { it.toTypedArray().toPredictionGroup(false) }
            Try.just(result)
        } catch (e: Exception) {
            Try.raise(e)
        } finally {
            savedModelBundle.close()
        }
    }
}
