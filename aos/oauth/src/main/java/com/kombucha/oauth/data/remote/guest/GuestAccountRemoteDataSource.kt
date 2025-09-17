package com.anipen.anipenauth.data.remote.guest

import com.anipen.anipenauth.data.remote.model.response.AccountUserInfoResponse
import com.anipen.anipenauth.data.remote.model.response.Oauth2Token
import kotlinx.coroutines.flow.Flow

internal interface GuestAccountRemoteDataSource { //게스트 로그인 관련 api 연결 datasource

    fun guestLogin(
        uniqueId: String,
        password: String,
        nonce: String,
        locale: String,
        clientId: String,
    ): Flow<Oauth2Token>

    fun getUserInfo(): Flow<AccountUserInfoResponse>

    fun getGuestCode(userId: String, password: String, locale: String): Flow<Oauth2Token>

}