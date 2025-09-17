package com.anipen.anipenauth.domain.usecase

import com.anipen.anipenauth.domain.repository.DeviceRepository

internal class DeviceUseCase(
    private val deviceRepository: DeviceRepository,
) {

    suspend fun setLocale(locale: String) {
        deviceRepository.setLocale(locale = locale)
    }

    suspend fun setClientId(clientId: String) {
        deviceRepository.setClientId(clientId = clientId)
    }

    suspend fun getLocale(): String {
        return deviceRepository.getLocale()
    }
}