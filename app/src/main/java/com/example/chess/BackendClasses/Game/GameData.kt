package com.example.chess.BackendClasses.Game

data class GameData( // это возвращает palying field
    val PersonId: String,
    val Score: Int,
    val PotionAvailable: PotionAvailable,
    val Moves: MutableList<Move>,
    val KillPiece: MutableList<PieceType>,
    val GameId: String,
    val GameName: String,
    val Board: MutableList<MutableList<Board>>,
    val Players: MutableList<Player>,
    val CurrentPlayer: String,
    val GameState: GameState,
    val WaitingPlayers: MutableList<Player>?
)

data class Board(
    val Color: String,
    val Type: PieceType,
    val PieceId: String,
    val PersonId: String,
)

data class Player(
    val PlayerId: String,
    val Nickname: String,
    var Time: String,
    var Color: String
)
