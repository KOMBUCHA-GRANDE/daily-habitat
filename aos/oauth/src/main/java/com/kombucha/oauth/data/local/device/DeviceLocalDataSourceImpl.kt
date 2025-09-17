package com.anipen.anipenauth.data.local.device

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


internal class DeviceLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : DeviceLocalDataSource {

    override suspend fun isFirstLogin(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[IS_FIRST_LOGIN]
        }.firstOrNull() ?: true
    }

    override suspend fun setIsFirstLogin(isFirstLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_LOGIN] = isFirstLogin
        }
    }

    override suspend fun setLocale(locale: String) {
        dataStore.edit { preferences ->
            preferences[LOCALE] = locale
        }
    }

    override suspend fun getLocale(): String {
        return dataStore.data.map { preferences ->
            preferences[LOCALE]
        }.firstOrNull() ?: VALUE_LANGUAGE
    }

    override suspend fun setClientId(clientId: String) {
        dataStore.edit { preferences ->
            preferences[CLIENT_ID] = clientId
        }
    }

    override suspend fun getClientId(): String {
        return dataStore.data.map { preferences ->
            preferences[CLIENT_ID]
        }.firstOrNull() ?: ""
    }


    companion object {
        private val IS_FIRST_LOGIN = booleanPreferencesKey("isFirstLogin")
        private val LOCALE = stringPreferencesKey("locale")
        private val CLIENT_ID = stringPreferencesKey("clientId")
        private const val VALUE_LANGUAGE = "ko"
    }
}