package com.anipen.anipenauth.data.remote.user

import com.anipen.anipenauth.data.remote.constant.QueryConstant
import com.anipen.anipenauth.data.remote.model.response.AccountUserInfoResponse
import com.anipen.anipenauth.data.remote.model.response.Oauth2Token
import com.anipen.anipenauth.data.remote.network.api.UserAccountApi
import com.anipen.anipenauth.data.remote.network.api.UserTokenApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class UserAccountRemoteDataSourceImpl(
    private val userAccountApi: UserAccountApi,
    private val userTokenApi: UserTokenApi,
) : UserAccountRemoteDataSource {

    override suspend fun logout(token: String): Flow<Unit> {
        return flow {
            emit(userAccountApi.logout(token = token))
        }
    }

    override suspend fun getAccessCode(authUserId: String): Flow<AccountUserInfoResponse> {
        return flow {
            val result = userAccountApi.getAccessCode(authUserId = authUserId)
            emit(result)
        }
    }

    override suspend fun getUserInfo(): Flow<AccountUserInfoResponse> {
        return flow {
            val result = userAccountApi.getUserInfo()
            emit(result)
        }
    }

    override suspend fun getUserInfo(authUserId: String): Flow<AccountUserInfoResponse> {
        return flow {
            emit(userAccountApi.getUserInfo(authUserId = authUserId))
        }
    }


    override suspend fun getAccessToken(
        code: String,
        codeVerifier: String,
        clientId: String,
    ): Flow<Oauth2Token> {
        return flow {
            val map = hashMapOf<String, String>().apply {
                put(QueryConstant.KEY_GRANT_TYPE, QueryConstant.VALUE_GRANT_TYPE_AUTHORIZATION_CODE)
                put(QueryConstant.KEY_CLIENT_ID, clientId)
                put(QueryConstant.KEY_CODE, code)
                put(QueryConstant.KEY_DEVICE_OS, QueryConstant.VALUE_DEVICE_OS)
                put(QueryConstant.KEY_CODE_VERIFIER, codeVerifier)
            }
            val result = userTokenApi.getAccessToken(map = map)
            emit(result)
        }
    }
}