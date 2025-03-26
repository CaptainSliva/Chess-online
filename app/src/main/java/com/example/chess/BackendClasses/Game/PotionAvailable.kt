package com.example.chess.BackendClasses.Game

data class PotionAvailable (
    val Potions: MutableList<PotionDataAvailable>
)

data class PotionDataAvailable (
    val Type: PotionType,
    val PotionId: String,
    val IsAvailable: Boolean,
    val IsUnlocked: Boolean
)
