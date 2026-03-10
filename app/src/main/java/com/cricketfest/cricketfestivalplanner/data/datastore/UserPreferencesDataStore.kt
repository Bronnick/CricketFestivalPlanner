package com.cricketfest.cricketfestivalplanner.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesDataStore(private val context: Context) {

    private object Keys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val SELECTED_THEME = stringPreferencesKey("selected_theme")
        val ORGANIZER_NAME = stringPreferencesKey("organizer_name")
    }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.ONBOARDING_COMPLETED] ?: false
    }

    val selectedTheme: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.SELECTED_THEME] ?: "Light"
    }

    val organizerName: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.ORGANIZER_NAME] ?: ""
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SELECTED_THEME] = theme
        }
    }

    suspend fun setOrganizerName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.ORGANIZER_NAME] = name
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}
