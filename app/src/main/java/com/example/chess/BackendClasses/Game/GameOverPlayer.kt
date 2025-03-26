package com.example.chess.BackendClasses.Game

import com.example.chess.BackendClasses.Base.BaseWebSocketMessage

data class GameOverPlayer(
    override val Message: String,
    override var StatusCode: Int,
    override val Success: Boolean,
    override val MessageType: String,
    val Person: String
) : BaseWebSocketMessage
