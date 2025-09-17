package com.anipen.anipenauth.data.remote.guest

import com.anipen.anipenauth.data.remote.constant.QueryConstant
import com.anipen.anipenauth.data.remote.model.request.PasswordRequest
import com.anipen.anipenauth.data.remote.model.response.AccountUserInfoResponse
import com.anipen.anipenauth.data.remote.model.response.Oauth2Token
import com.anipen.anipenauth.data.remote.network.api.GuestAccountApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class GuestAccountRemoteDataSourceImpl(
    private val guestAccountApi: GuestAccountApi,
) : GuestAccountRemoteDataSource {
    override fun guestLogin(
        uniqueId: String,
        password: String,
        nonce: String,
        locale: String,
        clientId: String,
    ): Flow<Oauth2Token> {
        return flow {
            val map = hashMapOf<String, String>().apply {
                put(QueryConstant.KEY_GRANT_TYPE, QueryConstant.VALUE_GRANT_TYPE_GUEST)
                put(QueryConstant.KEY_CLIENT_ID, clientId)
                put(QueryConstant.KEY_DEVICE_ID, uniqueId)
                put(QueryConstant.KEY_PASSWORD, password)
                put(QueryConstant.KEY_NONCE, nonce)
                put(QueryConstant.KEY_DEVICE_OS, QueryConstant.VALUE_DEVICE_OS)
                put(QueryConstant.KEY_LANGUAGE, locale)
            }
            val result = guestAccountApi.guestLogin(map = map)
            emit(result)
        }
    }

    override fun getUserInfo(): Flow<AccountUserInfoResponse> {
        return flow {
            val result = guestAccountApi.getUserInfo()
            emit(result)
        }
    }

    override fun getGuestCode(
        userId: String,
        password: String,
        locale: String,
    ): Flow<Oauth2Token> {
        return flow {
            val result = guestAccountApi.getGuestCode(
                userId = userId,
                passwordRequest = PasswordRequest(password),
                lang = locale
            )
            emit(result)
        }
    }
}