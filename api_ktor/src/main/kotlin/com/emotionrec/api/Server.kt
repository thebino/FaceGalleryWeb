package com.emotionrec.api


import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import mu.KotlinLogging
import org.tensorflow.SavedModelBundle

private val logger = KotlinLogging.logger { }

// TODO: This should be done through a config file too
private const val LOCAL_INF_MODEL = "./src/main/resources/1"
private const val LOCAL_INF_TAG = "serve"

fun Application.main() {

    // choose inference service based on config property
    val inferenceService = LocalInferenceService {
        SavedModelBundle.load(
            LOCAL_INF_MODEL,
            LOCAL_INF_TAG
        )
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(DefaultHeaders)
    routing {
        get("/ping") {
            logger.debug { "Received ping" }
            call.respondText { "pong" }
        }
        postPredictionImage(inferenceService)
    }

}
