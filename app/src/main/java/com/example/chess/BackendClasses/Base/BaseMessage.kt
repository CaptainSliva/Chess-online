package com.example.chess.BackendClasses.Base

interface BaseMessage {
    var statusCode: Int
    val success: Boolean
    val message: String
}
