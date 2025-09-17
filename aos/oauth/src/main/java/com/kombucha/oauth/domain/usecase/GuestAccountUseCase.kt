package com.anipen.anipenauth.domain.usecase

import com.anipen.anipenauth.domain.repository.DeviceRepository
import com.anipen.anipenauth.domain.repository.GuestAccountRepository
import kotlinx.coroutines.flow.Flow

internal class GuestAccountUseCase(
    private val guestAccountRepository: GuestAccountRepository,
    private val deviceRepository: DeviceRepository,
) {

    suspend fun guestLogin(): Flow<Unit> {
        val locale = deviceRepository.getLocale()
        val clientId = deviceRepository.getClientId()
        return guestAccountRepository.guestLogin(locale = locale, clientId = clientId)
    }

    suspend fun getGuestCode(): Flow<String> {
        val locale = deviceRepository.getLocale()
        val clientId = deviceRepository.getClientId()
        return guestAccountRepository.getGuestCode(locale = locale, clientId = clientId)
    }
}