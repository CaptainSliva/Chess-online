package com.example.chess.BackendClasses.Profile


typealias allPublicGames = MutableList<PublicGame>

data class PublicGame (
    val title: String,
    val gameId: String,
    val playerCount: Int,
    val totalPlayers: Int,
    val gameMode: String,
    val isPotion: Boolean,
)