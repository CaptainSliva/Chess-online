package com.example.chess.BackendClasses.Game

import com.google.gson.annotations.SerializedName

enum class GameState {
    @SerializedName("0")
    WaitingForPlayers,
    @SerializedName("1")
    CountDown,
    @SerializedName("2")
    InProgress,
    @SerializedName("3")
    Finished,
    @SerializedName("4")
    Stopped
}