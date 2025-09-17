package com.anipen.anipenauth.data.local.guest

internal interface GuestAccountLocalDataSource { //게스트 로그인 관련 로컬 데이터 정보

    suspend fun setAuthUserId(userId: String)

    suspend fun getAuthUserId(): String

    suspend fun getUniqueId(): String

    suspend fun setUniqueId(uniqueId: String)

    suspend fun getRandomPassword(): String

    suspend fun setRandomPassword(randomPassword: String)

    suspend fun getNonce(): String

    suspend fun setNonce(nonce: String)

    suspend fun setGuestToken(accessToken: String, refreshToken: String)

    suspend fun getGuestAccessToken(): String

    suspend fun getGuestRefreshToken(): String
}