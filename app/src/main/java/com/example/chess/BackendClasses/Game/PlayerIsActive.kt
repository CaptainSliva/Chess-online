package com.example.chess.BackendClasses.Game

import com.example.chess.BackendClasses.Base.BaseWebSocketMessage

data class PlayerIsActive(
    override val Message: String,
    override var StatusCode: Int,
    override val Success: Boolean,
    override val MessageType: String,
    val PlayerId: String,
    val Nickname: String,
    val State: Boolean,
    val Time: String
) : BaseWebSocketMessage
