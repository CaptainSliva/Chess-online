package com.example.chess.BackendClasses.Game

import com.google.gson.annotations.SerializedName

enum class PotionType {
    @SerializedName("0")
    KillPiece,
    @SerializedName("1")
    DoublePoints,
    @SerializedName("2")
    RandomKillPiece,
    @SerializedName("3")
    EnlargedPiece,
    @SerializedName("4")
    UltimateProtectionPiece
}