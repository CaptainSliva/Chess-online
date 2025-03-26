package com.example.chess.BackendClasses.Game

import com.example.chess.BackendClasses.Base.BaseWebSocketMessage


data class ResultPlayerTheGame(
    override val Message: String,
    override var StatusCode: Int,
    override val Success: Boolean,
    override val MessageType: String,
    val Status: Boolean,
    val League: String,
    val Score: ScoreData,
    val Rating: Int,
    val AddPotion: AddPotion?,
    val UsedPotions: List<PotionType>?
) : BaseWebSocketMessage

data class ScoreData(
    val Score: Int,
    val AddScoreWine: Int?,
    val PotionScore: Int?,
    val ModeScore: Int,
    val TotalScore: Int
)

data class AddPotion(
    val Type: PotionType,
    val PotionName: String,
    val Count: Int
)
