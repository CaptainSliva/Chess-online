package com.example.chess

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.chess.BackendClasses.Base.WebSocketListener
import com.example.chess.BackendClasses.Game.Move
import okhttp3.MediaType.Companion.toMediaType

class GlobalVariables {
}

val Context.dataStore by preferencesDataStore(name = "userdata")
const val baseUrl = "https://linksshrink.ru/api-chess/v1"
val MEDIA_TYPE = "application/x-www-form-urlencoded".toMediaType()
var token = ""
var userID = ""
var wssNumberConnectFailure = 0
var numColumnsGame = 8
var gameID = ""
var wsL = WebSocketListener()

