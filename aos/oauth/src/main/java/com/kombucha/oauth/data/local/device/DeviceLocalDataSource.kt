package com.anipen.anipenauth.data.local.device

internal interface DeviceLocalDataSource { //유저와 상관없는 디바이스 관련 로컬 데이터

    suspend fun isFirstLogin(): Boolean // 최초 로그인인지 확인

    suspend fun setIsFirstLogin(isFirstLogin: Boolean)

    suspend fun setLocale(locale: String)

    suspend fun getLocale(): String

    suspend fun setClientId(clientId: String)

    suspend fun getClientId(): String

}