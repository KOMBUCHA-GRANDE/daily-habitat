package com.anipen.anipenauth.data.remote.user

import com.anipen.anipenauth.data.remote.model.response.AccountUserInfoResponse
import com.anipen.anipenauth.data.remote.model.response.Oauth2Token
import kotlinx.coroutines.flow.Flow

internal interface UserAccountRemoteDataSource { //유저 로그인 관련 api 연결 datasource

    suspend fun logout(token: String): Flow<Unit>

    suspend fun getAccessCode(authUserId: String): Flow<AccountUserInfoResponse>

    suspend fun getUserInfo(): Flow<AccountUserInfoResponse>

    suspend fun getAccessToken(
        code: String,
        codeVerifier: String,
        clientId: String,
    ): Flow<Oauth2Token>

    suspend fun getUserInfo(authUserId: String): Flow<AccountUserInfoResponse>
}