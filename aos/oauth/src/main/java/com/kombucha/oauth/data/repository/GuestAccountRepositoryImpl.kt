package com.anipen.anipenauth.data.repository


import com.anipen.anipenauth.data.local.guest.GuestAccountLocalDataSource
import com.anipen.anipenauth.data.remote.guest.GuestAccountRemoteDataSource
import com.anipen.anipenauth.data.utils.AuthHelper.getNonce
import com.anipen.anipenauth.data.utils.AuthHelper.getRandomPassword
import com.anipen.anipenauth.data.utils.AuthHelper.getUniqueId
import com.anipen.anipenauth.domain.repository.GuestAccountRepository
import com.anipen.anipenauth.utils.toException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retry

internal class GuestAccountRepositoryImpl(
    private val guestAccountLocalDataSource: GuestAccountLocalDataSource,
    private val guestAccountRemoteDataSource: GuestAccountRemoteDataSource,
) : GuestAccountRepository {
    override suspend fun guestLogin(locale: String, clientId: String): Flow<Unit> {
        val uniqueId = getUniqueId()
        val password = getRandomPassword()
        val nonce = getNonce()
        return guestAccountRemoteDataSource.guestLogin(
            uniqueId = uniqueId,
            password = password,
            nonce = nonce,
            locale = locale,
            clientId = clientId
        ).catch {
            throw it.toException()
        }.map { token ->
            saveGuestToken(accessToken = token.accessToken, refreshToken = token.refreshToken)
            fetchGuestAuthUserId().collect()
        }
    }

    override suspend fun getGuestCode(
        locale: String,
        clientId: String,
    ): Flow<String> { // guestCode 받아오며 authUserId가 없을 경우 게스트 로그인 진행 후 다시 요청
        val userId = guestAccountLocalDataSource.getAuthUserId()
        val guestAccessToken = guestAccountLocalDataSource.getGuestAccessToken()
        val password = getRandomPassword()
        return guestAccountRemoteDataSource.getGuestCode(
            userId = userId,
            password = password,
            locale = locale
        ).onStart {
            if (userId.isEmpty() || guestAccessToken.isEmpty()) {
                guestLogin(locale = locale, clientId = clientId).firstOrNull()
            }
        }.retry(1) {
            guestLogin(locale = locale, clientId = clientId).firstOrNull()
            true
        }.map { token ->
            token.guestCode
        }.catch {
            throw it.toException()
        }
    }

    private suspend fun fetchGuestAuthUserId(): Flow<Unit> { //게스트 authUserId 요청
        return guestAccountRemoteDataSource.getUserInfo().catch {
            throw it.toException()
        }.retry {
            true
        }.map { result ->
            guestAccountLocalDataSource.setAuthUserId(result.sub)
        }
    }

    private suspend fun saveGuestToken(accessToken: String, refreshToken: String) {
        guestAccountLocalDataSource.setGuestToken(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}