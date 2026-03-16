package com.example.guestservicesapp.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("guest_prefs")

object RoomPreference {

    private val ROOM_KEY = stringPreferencesKey("room_number")

    suspend fun saveRoom(context: Context, room: String) {
        context.dataStore.edit { prefs ->
            prefs[ROOM_KEY] = room
        }
    }

    fun getRoom(context: Context): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[ROOM_KEY] ?: ""
        }
    }
}