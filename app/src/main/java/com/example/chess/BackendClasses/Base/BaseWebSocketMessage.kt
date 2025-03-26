package com.example.chess.BackendClasses.Base

interface BaseWebSocketMessage : BasewsMessage {
    override val Message: String
    override var StatusCode: Int
    override val Success: Boolean
    val MessageType: String
}
