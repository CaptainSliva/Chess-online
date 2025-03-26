package com.example.chess.Datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.chess.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class FunctionsDS(private val context: Context) {

    suspend fun saveToDataStore(token: String, id: String) {
        context.dataStore.edit { preference ->
            preference[KeysDS.TOKEN] = token
            preference[KeysDS.ID] = id
        }
    }
}