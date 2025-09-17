package com.anipen.anipenauth.data.local.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

internal class UserAccountLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : UserAccountLocalDataSource {

    override suspend fun setUserToken(
        accessToken: String,
        refreshToken: String,
        idToken: String,
        accessTokenExpireIn: Long,
    ) {
        dataStore.edit { preferences ->
            preferences[USER_ACCESS_TOKEN] = accessToken
            preferences[USER_REFRESH_TOKEN] = refreshToken
            preferences[USER_ID_TOKEN] = idToken
            preferences[ACCESS_TOKEN_EXPIRE_IN] = accessTokenExpireIn
        }
    }

    override suspend fun setToken(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[USER_ACCESS_TOKEN] = accessToken
            preferences[USER_REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun getUserAccessToken(): String {
        return dataStore.data.map { preferences ->
            preferences[USER_ACCESS_TOKEN]
        }.firstOrNull() ?: ""
    }

    override suspend fun getAccessTokenExpireTime(): Long {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_EXPIRE_IN]
        }.firstOrNull() ?: 0L
    }

    override suspend fun getUserRefreshToken(): String {
        return dataStore.data.map { preferences ->
            preferences[USER_REFRESH_TOKEN]
        }.firstOrNull() ?: ""
    }

    override suspend fun getUserIdToken(): String {
        return dataStore.data.map { preferences ->
            preferences[USER_ID_TOKEN]
        }.firstOrNull() ?: ""
    }

    override suspend fun clearPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override suspend fun setAuthUserId(authUserId: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_USER_ID] = authUserId
        }
    }

    override suspend fun getAuthUserId(): String {
        return dataStore.data.map { preferences ->
            preferences[AUTH_USER_ID]
        }.firstOrNull() ?: ""
    }

    companion object {

        private val AUTH_USER_ID = stringPreferencesKey("authUserId")
        private val USER_ACCESS_TOKEN = stringPreferencesKey("userAccessToken")
        private val USER_REFRESH_TOKEN = stringPreferencesKey("userRefreshToken")
        private val USER_ID_TOKEN = stringPreferencesKey("userIdToken")
        private val ACCESS_TOKEN_EXPIRE_IN = longPreferencesKey("accessTokenExpireIn")
    }
}