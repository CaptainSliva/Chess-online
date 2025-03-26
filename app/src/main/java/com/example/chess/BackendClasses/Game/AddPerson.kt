package com.example.chess.BackendClasses.Game

import com.example.chess.BackendClasses.Base.BaseWebSocketMessage

data class AddPerson(
    override val Message: String,
    override var StatusCode: Int,
    override val Success: Boolean,
    override val MessageType: String,
    val PersonId: String,
    val Nickname: String,
    val Time: String,

) : BaseWebSocketMessage
