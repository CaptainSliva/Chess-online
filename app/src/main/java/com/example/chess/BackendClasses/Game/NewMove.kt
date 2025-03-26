package com.example.chess.BackendClasses.Game

import com.example.chess.BackendClasses.Base.BaseWebSocketMessage

data class Move(
    val MoveId: String,
    val PlayerId: String,
    val DataMove: String,
    val Duration: String,
    val StartRow: Int,
    val StartColumn: Int,
    val EndRow: Int,
    val EndColumn: Int
)

data class NewMove(
    override val Message: String,
    override var StatusCode: Int,
    override val Success: Boolean,
    override val MessageType: String,
    val Move: Move
) : BaseWebSocketMessage
