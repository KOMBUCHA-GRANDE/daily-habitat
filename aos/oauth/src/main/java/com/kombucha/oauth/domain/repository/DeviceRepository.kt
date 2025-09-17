package com.anipen.anipenauth.domain.repository

internal interface DeviceRepository {

    suspend fun setLocale(locale: String)

    suspend fun getLocale(): String

    suspend fun isFirstUserLogin(): Boolean

    suspend fun setIsFirstUserLogin(isFirstLogin: Boolean)

    suspend fun setClientId(clientId: String)

    suspend fun getClientId(): String
}