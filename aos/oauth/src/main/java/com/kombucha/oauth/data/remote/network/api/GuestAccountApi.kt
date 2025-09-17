package com.anipen.anipenauth.data.remote.network.api

import com.anipen.anipenauth.data.remote.model.request.PasswordRequest
import com.anipen.anipenauth.data.remote.model.response.AccountUserInfoResponse
import com.anipen.anipenauth.data.remote.model.response.Oauth2Token
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface GuestAccountApi { //게스트 로그인 관련 api

    @FormUrlEncoded
    @POST("oauth2/v1/token") //게스트 로그인
    suspend fun guestLogin(@FieldMap map: HashMap<String, String>): Oauth2Token

    @GET("oauth2/v1/userinfo") //게스트 accessToken 검증 및 authUserId 받아오기
    suspend fun getUserInfo(): AccountUserInfoResponse

    @FormUrlEncoded
    @POST("users/v1/{userId}/guestCode") //게스트 코드 받아오기
    suspend fun getGuestCode(
        @Path("userId") userId: String,
        @Body passwordRequest: PasswordRequest,
        @Field("lang") lang: String,
    ): Oauth2Token
}