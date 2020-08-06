package com.emotionrec.api

sealed class PredictionError(val message: String) {
    class MissingInput(message: String) : PredictionError(message)
    class InvalidInput(message: String) : PredictionError(message)
    class TodoErr : PredictionError("Err")
}
