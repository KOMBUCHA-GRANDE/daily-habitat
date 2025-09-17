package com.anipen.anipenauth.feature.guest

import com.anipen.anipenauth.domain.usecase.GuestAccountUseCase
import kotlinx.coroutines.flow.Flow

internal class AnipenGuestImpl(
    private val guestAccountUseCase: GuestAccountUseCase,
) : AnipenGuest {
    override suspend fun guestLogin(): Flow<Unit> {
        return guestAccountUseCase.guestLogin()
    }

    override suspend fun getGuestCode(): Flow<String> {
        return guestAccountUseCase.getGuestCode()
    }
}