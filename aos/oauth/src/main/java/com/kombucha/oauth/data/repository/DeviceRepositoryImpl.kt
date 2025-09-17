package com.anipen.anipenauth.data.repository

import com.anipen.anipenauth.data.local.device.DeviceLocalDataSource
import com.anipen.anipenauth.domain.repository.DeviceRepository

internal class DeviceRepositoryImpl(
    private val deviceLocalDataSource: DeviceLocalDataSource,
) : DeviceRepository {
    override suspend fun setLocale(locale: String) {
        deviceLocalDataSource.setLocale(locale = locale)
    }

    override suspend fun getLocale(): String {
        return deviceLocalDataSource.getLocale()
    }

    override suspend fun isFirstUserLogin(): Boolean {
        return deviceLocalDataSource.isFirstLogin()
    }

    override suspend fun setIsFirstUserLogin(isFirstLogin: Boolean) {
        deviceLocalDataSource.setIsFirstLogin(isFirstLogin = isFirstLogin)
    }

    override suspend fun setClientId(clientId: String) {
        deviceLocalDataSource.setClientId(clientId = clientId)
    }

    override suspend fun getClientId(): String {
        return deviceLocalDataSource.getClientId()
    }
}