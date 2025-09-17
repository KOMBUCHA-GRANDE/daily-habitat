package com.anipen.anipenauth.data.remote.token

import com.anipen.anipenauth.data.remote.constant.QueryConstant
import com.anipen.anipenauth.data.remote.model.response.Oauth2Token
import com.anipen.anipenauth.data.remote.network.api.TokenApi

internal class TokenRemoteDatasourceImpl(
    private val tokenApi: TokenApi,
) : TokenRemoteDatasource {
    override suspend fun refreshAccessToken(
        clientId: String,
        refreshToken: String,
        locale: String,
    ): Oauth2Token {
        val map = hashMapOf<String, String>().apply {
            put(
                QueryConstant.KEY_GRANT_TYPE,
                QueryConstant.VALUE_GRANT_TYPE_REFRESH_TOKEN
            )
            put(QueryConstant.KEY_CLIENT_ID, clientId)
            put(QueryConstant.KEY_REFRESH_TOKEN, refreshToken)
            put(QueryConstant.KEY_LANGUAGE, locale)
        }
        return tokenApi.refreshAccessToken(map = map)
    }
}