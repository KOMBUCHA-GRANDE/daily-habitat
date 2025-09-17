package com.anipen.anipenauth.data.remote.network.client

import okhttp3.Interceptor

interface AnipenClient {

    fun createInterceptor(clientState: ClientState = ClientState.USER): Interceptor
}