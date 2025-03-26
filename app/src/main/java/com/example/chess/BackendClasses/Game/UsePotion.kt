package com.example.chess.BackendClasses.Game

import com.example.chess.BackendClasses.Base.BaseMessage
import com.example.chess.BackendClasses.Base.BaseWebSocketMessage

data class UsePotion (
    override val Message: String,
    override var StatusCode: Int,
    override val Success: Boolean,
    override val MessageType: String,
    val PotionType: PotionType,
    val PotionName: String,
    val UsePersonId: String,
    val UsePersonName: String

): BaseWebSocketMessage
