package com.anipen.anipenauth.data.remote.network.api

import com.anipen.anipenauth.data.remote.model.response.Oauth2Token
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * No Authorization Need
 */
internal interface UserTokenApi {
    @FormUrlEncoded
    @POST("oauth2/v1/token") //통합 로그인 성공 후 받아온 code를 통해 유저 accessToken 받아오기
    suspend fun getAccessToken(@FieldMap map: HashMap<String, String>): Oauth2Token
}