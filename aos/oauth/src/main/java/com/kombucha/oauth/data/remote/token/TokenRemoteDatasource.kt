package com.anipen.anipenauth.data.remote.token

import com.anipen.anipenauth.data.remote.model.response.Oauth2Token

internal interface TokenRemoteDatasource {
    suspend fun refreshAccessToken(
        clientId: String,
        refreshToken: String,
        locale: String,
    ): Oauth2Token
}