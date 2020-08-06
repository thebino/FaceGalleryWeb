package com.emotionrec.inferenceservice


import com.emotionrec.api.models.Emotion
import com.emotionrec.api.models.InferenceInput
import com.emotionrec.inferenceservice.utils.ValidationInputRetrieval
import com.emotionrec.api.LocalInferenceService
import org.junit.Assert
import org.junit.Test
import org.tensorflow.SavedModelBundle
import java.io.BufferedReader
import java.io.InputStreamReader

class ValidationInferenceTest {


    val CSV_FILE_NAME = "fer2013.csv"
    fun createResourceBufferedReader(fileName: String = CSV_FILE_NAME): BufferedReader {
        //                    BufferedReader(FileReader(CSV_FILE_NAME))
//        BufferedReader(InputStreamReader(ValidationInputRetrieval::class.java.getResourceAsStream(("/$CSV_FILE_NAME"))))
        return BufferedReader(InputStreamReader(ValidationInputRetrieval::class.java.classLoader.getResourceAsStream((fileName))))
    }


    @Test
    fun localInferenceTest() {
        getPrediction_20(
            LocalInferenceService {
                SavedModelBundle.load(
                    "./fifSavedModel",
                    "serve"
                )
            },
                ValidationInputRetrieval(createResourceBufferedReader()))
    }

    private fun getPrediction_20(inferenceService: LocalInferenceService, validationInputRetrieval: ValidationInputRetrieval) {
        val formattedInputData = validationInputRetrieval.getFormattedInput(20)

        inferenceService.getPrediction(formattedInputData.toInferenceInput())
                .fold(
                        {
                            Assert.fail("Inference failed: $it")
                            println("Failed: $it")
                        },
                        {
                            it.forEachIndexed { index, predictionGroup ->
                                println(predictionGroup)
                                val correctEmotion = formattedInputData[index].second
                                println("""
                        |   Correct Emotion: $correctEmotion
                        """.trimMargin())
                            }

                            Assert.assertTrue(it.size == formattedInputData.size)
                        })
    }


    fun List<Pair<InferenceInput, Emotion>>.toInferenceInput(): List<InferenceInput> {
        return this.map { it.first }
    }
}
