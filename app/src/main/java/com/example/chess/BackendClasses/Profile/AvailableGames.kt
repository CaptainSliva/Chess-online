package com.example.chess.BackendClasses.Profile


typealias historyGames = MutableList<HistoryGame>

data class HistoryGame(
    val gameName: String,
    val gameDuration: String,
    val isWinner: Boolean,
    val playerCount: Int,
    val gameMode: String,
    val isPrivate: Boolean,
    val isPotion: Boolean,
    val dateCreated: String,
)
