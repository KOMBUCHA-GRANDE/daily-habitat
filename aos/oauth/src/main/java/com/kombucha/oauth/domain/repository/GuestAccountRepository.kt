package com.anipen.anipenauth.domain.repository

import kotlinx.coroutines.flow.Flow

internal interface GuestAccountRepository {

    suspend fun guestLogin(locale: String, clientId: String): Flow<Unit>

    suspend fun getGuestCode(locale: String, clientId: String): Flow<String>

}