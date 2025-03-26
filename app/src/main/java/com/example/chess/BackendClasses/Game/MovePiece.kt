package com.example.chess.BackendClasses.Game

data class MovePiece(
    val Type: String,
    val gameId: String,
    val token: String,
    val FromRow: Int,
    val FromCol: Int,
    val ToRow: Int,
    val ToCol: Int
)
