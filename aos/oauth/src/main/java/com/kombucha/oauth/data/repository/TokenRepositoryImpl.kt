package com.anipen.anipenauth.data.repository

import com.anipen.anipenauth.data.local.device.DeviceLocalDataSource
import com.anipen.anipenauth.data.local.guest.GuestAccountLocalDataSource
import com.anipen.anipenauth.data.local.user.UserAccountLocalDataSource
import com.anipen.anipenauth.data.remote.network.client.ClientState
import com.anipen.anipenauth.data.remote.token.TokenRemoteDatasource
import com.anipen.anipenauth.domain.model.Token
import com.anipen.anipenauth.domain.repository.TokenRepository
import com.anipen.anipenauth.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

internal class TokenRepositoryImpl(
    private val userAccountLocalDataSource: UserAccountLocalDataSource,
    private val guestAccountLocalDataSource: GuestAccountLocalDataSource,
    private val deviceLocalDataSource: DeviceLocalDataSource,
    private val tokenRemoteDatasource: TokenRemoteDatasource,
) : TokenRepository {

    override fun getAccessToken(clientState: ClientState): String {
        return runBlocking {
            when (clientState) {
                ClientState.USER -> {
                    userAccountLocalDataSource.getUserAccessToken()
                }

                ClientState.GUEST -> {
                    guestAccountLocalDataSource.getGuestAccessToken()
                }
            }
        }
    }

    override fun getRefreshToken(clientState: ClientState): String {
        return runBlocking(Dispatchers.IO) {
            when (clientState) {
                ClientState.USER -> {
                    userAccountLocalDataSource.getUserRefreshToken()
                }

                ClientState.GUEST -> {
                    guestAccountLocalDataSource.getGuestRefreshToken()
                }
            }
        }
    }

    override suspend fun requestNewToken(clientState: ClientState): Token {
        val refreshToken = getRefreshToken(clientState = clientState)
        val locale = deviceLocalDataSource.getLocale()
        val clientId = deviceLocalDataSource.getClientId()
        val token = tokenRemoteDatasource.refreshAccessToken(
            clientId = clientId,
            locale = locale,
            refreshToken = refreshToken
        )
        when (clientState) {
            ClientState.USER -> {
                userAccountLocalDataSource.setUserToken(
                    accessToken = token.accessToken,
                    refreshToken = token.refreshToken,
                    idToken = token.idToken,
                    accessTokenExpireIn = DateUtils.getCurrentUnixTime() + token.expiresIn
                )
            }

            ClientState.GUEST -> {
                guestAccountLocalDataSource.setGuestToken(
                    accessToken = token.accessToken,
                    refreshToken = token.refreshToken
                )
            }
        }
        return Token(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
            idToken = token.idToken
        )
    }
}