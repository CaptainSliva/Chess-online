package com.example.chess.BackendClasses.Login

import com.example.chess.BackendClasses.Base.BaseMessage

data class RegAuth(
    override val message: String,
    override val success: Boolean,
    override var statusCode: Int,
    val error: String,
    val accessToken: String,
    val refreshToken: String,
    val personId: String
    ) : BaseMessage
