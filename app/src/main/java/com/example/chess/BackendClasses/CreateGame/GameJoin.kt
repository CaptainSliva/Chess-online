package com.example.chess.BackendClasses.CreateGame

import com.example.chess.BackendClasses.Base.BaseMessage

data class GameJoin(
    override val message: String,
    override val success: Boolean,
    override var statusCode: Int,
    val error: String
    ) : BaseMessage
