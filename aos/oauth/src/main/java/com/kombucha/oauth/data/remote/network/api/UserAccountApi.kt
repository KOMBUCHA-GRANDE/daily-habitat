package com.anipen.anipenauth.data.remote.network.api


import com.anipen.anipenauth.data.remote.model.response.AccountUserInfoResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface UserAccountApi { //로그인 후 유저 관련 api

    @FormUrlEncoded
    @POST("oauth2/v1/revoke") //로그아웃 진행 시 유저의 token 폐기 요청
    suspend fun logout(@Field("token") token: String)

    @GET("users/v1/{authUserId}/code") //회원가입과 닉네임 변경을 위한 AccessCode를 요청
    suspend fun getAccessCode(@Path("authUserId") authUserId: String): AccountUserInfoResponse

    @GET("oauth2/v1/userinfo") //유저의 token 검증 및 authUserId 받아오기
    suspend fun getUserInfo(): AccountUserInfoResponse

    @GET("users/v1/{authUserId}") //유저 프로필 요청
    suspend fun getUserInfo(@Path("authUserId") authUserId: String): AccountUserInfoResponse

}