package com.example.chess.BackendClasses.Game

import com.google.gson.annotations.SerializedName

enum class PieceType() {
    @SerializedName("0")
    King,
    @SerializedName("1")
    Queen,
    @SerializedName("2")
    Rook,
    @SerializedName("3")
    Bishop,
    @SerializedName("4")
    Knight,
    @SerializedName("5")
    Pawn
}