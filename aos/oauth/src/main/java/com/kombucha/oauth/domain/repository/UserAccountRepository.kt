package com.anipen.anipenauth.domain.repository

import com.anipen.anipenauth.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

internal interface UserAccountRepository {

    suspend fun fetchAccessToken(code: String, codeVerifier: String, clientId: String): Flow<Unit>

    suspend fun clearUserInfo()

    suspend fun logout(): Flow<Unit>

    suspend fun getAccessCode(): Flow<String>

    suspend fun getUserIdToken(): String

    suspend fun getAccessTokenInfo(): Pair<String, Long> //accessToken, expireTime

    suspend fun isLogin(): Flow<Boolean>

    suspend fun getUserInfo(): Flow<UserInfo>

}