package com.anipen.anipenauth.data.local.guest

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

internal class GuestAccountLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : GuestAccountLocalDataSource {


    override suspend fun setAuthUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_USER_ID] = userId
        }
    }

    override suspend fun getAuthUserId(): String {
        return dataStore.data.map { preferences ->
            preferences[AUTH_USER_ID]
        }.firstOrNull() ?: ""
    }

    override suspend fun getUniqueId(): String {
        return dataStore.data.map { preferences ->
            preferences[UNIQUE_ID]
        }.firstOrNull() ?: ""
    }

    override suspend fun setUniqueId(uniqueId: String) {
        dataStore.edit { preferences ->
            preferences[UNIQUE_ID] = uniqueId
        }
    }

    override suspend fun getRandomPassword(): String {
        return dataStore.data.map { preferences ->
            preferences[RANDOM_PASSWORD]
        }.firstOrNull() ?: ""
    }

    override suspend fun setRandomPassword(randomPassword: String) {
        dataStore.edit { preferences ->
            preferences[RANDOM_PASSWORD] = randomPassword
        }
    }

    override suspend fun getNonce(): String {
        return dataStore.data.map { preferences ->
            preferences[NONCE]
        }.firstOrNull() ?: ""
    }

    override suspend fun setNonce(nonce: String) {
        dataStore.edit { preferences ->
            preferences[NONCE] = nonce
        }
    }

    override suspend fun setGuestToken(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[GUEST_ACCESS_TOKEN] = accessToken
            preferences[GUEST_REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun getGuestAccessToken(): String {
        return dataStore.data.map { preferences ->
            preferences[GUEST_ACCESS_TOKEN]
        }.firstOrNull() ?: ""
    }

    override suspend fun getGuestRefreshToken(): String {
        return dataStore.data.map { preferences ->
            preferences[GUEST_REFRESH_TOKEN]
        }.firstOrNull() ?: ""
    }

    companion object {
        //게스트 로그인 시 필요한 정보
        private val UNIQUE_ID = stringPreferencesKey("uniqueId")
        private val RANDOM_PASSWORD = stringPreferencesKey("randomPassword")
        private val NONCE = stringPreferencesKey("nonce")

        //게스트 정보
        private val GUEST_ACCESS_TOKEN = stringPreferencesKey("guestAccessToken")
        private val GUEST_REFRESH_TOKEN = stringPreferencesKey("guestRefreshToken")
        private val AUTH_USER_ID = stringPreferencesKey("authUserId")
    }
}