package com.anipen.anipenauth.domain.usecase

import com.anipen.anipenauth.domain.model.UserInfo
import com.anipen.anipenauth.domain.repository.DeviceRepository
import com.anipen.anipenauth.domain.repository.UserAccountRepository
import com.anipen.anipenauth.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

internal class UserAccountUseCase(
    private val userAccountRepository: UserAccountRepository,
    private val deviceRepository: DeviceRepository,
) {
    suspend fun clearUserInfo() {
        userAccountRepository.clearUserInfo()
    }

    suspend fun logout(): Flow<Unit> {
        return userAccountRepository.logout()
    }

    suspend fun isFirstUserLogin(): Boolean {
        return deviceRepository.isFirstUserLogin()
    }

    suspend fun getUserIdToken(): String {
        return userAccountRepository.getUserIdToken()
    }

    suspend fun getLocale(): String {
        return deviceRepository.getLocale()
    }

    suspend fun getClientId(): String {
        return deviceRepository.getClientId()
    }

    suspend fun getAccessCode(): Flow<String> {
        return userAccountRepository.getAccessCode()
    }

    suspend fun fetchAccessToken(code: String, codeVerifier: String, clientId: String): Flow<Unit> {
        return userAccountRepository.fetchAccessToken(
            code = code,
            codeVerifier = codeVerifier,
            clientId = clientId
        )
    }

    suspend fun setIsFirstUserLogin(isFirstLogin: Boolean) {
        deviceRepository.setIsFirstUserLogin(isFirstLogin = isFirstLogin)
    }

    suspend fun isLogin(): Boolean {
        val idToken = userAccountRepository.getUserIdToken()
        if (idToken.isEmpty()) return false
        return userAccountRepository.isLogin().firstOrNull() ?: false
    }

    suspend fun isPassAccessTokenExpireTime(): Boolean {
        val (accessToken, expireTime) = userAccountRepository.getAccessTokenInfo()
        return accessToken.isEmpty() || expireTime <= DateUtils.getCurrentUnixTime()
    }

    suspend fun getUserInfo(): Flow<UserInfo> {
        return userAccountRepository.getUserInfo()
    }
}