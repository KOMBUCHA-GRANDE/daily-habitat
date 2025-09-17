package com.anipen.anipenauth.data.local.user

internal interface UserAccountLocalDataSource {

    suspend fun setUserToken(
        accessToken: String,
        refreshToken: String,
        idToken: String,
        accessTokenExpireIn: Long,
    )

    suspend fun setToken(accessToken: String, refreshToken: String)

    suspend fun getUserAccessToken(): String

    suspend fun getAccessTokenExpireTime(): Long

    suspend fun getUserRefreshToken(): String

    suspend fun getUserIdToken(): String

    suspend fun clearPreferences()

    suspend fun setAuthUserId(authUserId: String)

    suspend fun getAuthUserId(): String
}