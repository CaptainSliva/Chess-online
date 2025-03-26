package com.example.chess.Datastore

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey


object KeysDS {
    val TOKEN = stringPreferencesKey("token")
    val ID = stringPreferencesKey("id")
}
