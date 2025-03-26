package com.example.chess.BackendClasses.Profile

data class DataProfile(
    val personId: String,
    val name: String,
    val league: String,
    val score: Long,
    val level: Long,
    val numberOfWinsLevel: Long,
    val requiredNumberOfWinsLevel: Long,
    val isChest: Boolean,
    val potions: List<Potion>,
)

data class Potion(
    val potionId: String,
    val type: String,
    val count: Long,
    val isPurchased: Boolean,
    val isUnlocked: Boolean,
)

