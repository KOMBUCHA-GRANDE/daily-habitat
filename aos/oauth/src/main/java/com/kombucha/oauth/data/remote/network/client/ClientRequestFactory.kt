package com.anipen.anipenauth.data.remote.network.client

import com.anipen.anipenauth.domain.model.LoginRequest
import com.anipen.anipenauth.domain.repository.TokenRepository
import com.anipen.anipenauth.feature.AnipenAuth.TAG_LOGIN
import com.anipen.anipenauth.utils.GuestRefreshTokenExpiredException
import com.anipen.anipenauth.utils.NoUserAccessTokenException
import com.anipen.anipenauth.utils.UserRefreshTokenExpiredException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer

internal class ClientRequestFactory(
    private val tokenRepository: TokenRepository,
) {
    fun createTokenHeaderRequest(clientState: ClientState, chain: Interceptor.Chain): Request {
        val accessToken = tokenRepository.getAccessToken(clientState = clientState)
        if (accessToken.isEmpty()) throw NoUserAccessTokenException()
        return chain.request().newBuilder()
            .addHeader(HEADER_KEY_AUTHORIZATION, "$HEADER_BEARER $accessToken")
            .build()
    }

    fun createRefreshedHeaderTokenRequest(
        clientState: ClientState,
        chain: Interceptor.Chain,
    ): Request {
        return runBlocking(Dispatchers.IO) {
            try {
                val newToken = tokenRepository.requestNewToken(clientState = clientState)
                chain.request()
                    .changeIdTokenIfLoginRequest(idToken = newToken.idToken)
                    .putTokenHeader(newToken.accessToken)
            } catch (e: Exception) {
                when (clientState) {
                    ClientState.USER -> {
                        throw UserRefreshTokenExpiredException()
                    }

                    ClientState.GUEST -> {
                        throw GuestRefreshTokenExpiredException()
                    }
                }
            }
        }
    }

    private fun Request.changeIdTokenIfLoginRequest(idToken: String): Request {
        return if (tag(String::class.java) == TAG_LOGIN) {
            try {
                val contentType = body?.contentType()
                val buffer = Buffer()
                body?.writeTo(buffer) ?: return this
                val originBodyString = buffer.readUtf8()
                val originRequest = Json.decodeFromString(
                    deserializer = LoginRequest.serializer(),
                    string = originBodyString
                )
                val modifiedRequest = originRequest.copy(idToken = idToken)
                val modifiedBodyString =
                    Json.encodeToString(LoginRequest.serializer(), modifiedRequest)
                val modifiedBody = modifiedBodyString.toRequestBody(contentType = contentType)
                val request = newBuilder().method(method, modifiedBody).build()
                request
            } catch (e: Exception) {
                this
            }
        } else {
            this
        }
    }

    private fun Request.putTokenHeader(token: String): Request {
        return this.newBuilder()
            .addHeader(HEADER_KEY_AUTHORIZATION, "$HEADER_BEARER $token")
            .build()
    }

    companion object {
        private const val HEADER_KEY_AUTHORIZATION = "Authorization"
        private const val HEADER_BEARER = "Bearer"
    }
}