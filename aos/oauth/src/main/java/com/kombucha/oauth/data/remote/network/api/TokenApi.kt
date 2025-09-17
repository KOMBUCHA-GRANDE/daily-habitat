package com.anipen.anipenauth.data.remote.network.api

import com.anipen.anipenauth.data.remote.model.response.Oauth2Token
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

internal interface TokenApi { //accessToken이 만료 시 갱신을 위한 api

    @FormUrlEncoded
    @POST("oauth2/v1/token") //refreshToken을 이용하여 accessToken 갱신
    suspend fun refreshAccessToken(@FieldMap map: HashMap<String, String>): Oauth2Token
}