package com.anipen.anipenauth.domain.repository

import com.anipen.anipenauth.data.remote.network.client.ClientState
import com.anipen.anipenauth.domain.model.Token

internal interface TokenRepository {

    fun getAccessToken(clientState: ClientState): String

    fun getRefreshToken(clientState: ClientState): String

    suspend fun requestNewToken(clientState: ClientState): Token

}