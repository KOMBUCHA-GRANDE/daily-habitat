package com.anipen.anipenauth.data.remote.network.client


import com.anipen.anipenauth.utils.STATUS_CODE_FORBIDDEN
import com.anipen.anipenauth.utils.STATUS_CODE_NO_AUTHORIZATION
import com.orhanobut.logger.Logger
import net.openid.appauth.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.util.concurrent.TimeUnit

internal class ClientFactory(
    private val clientRequestFactory: ClientRequestFactory,
) : AnipenClient {

    override fun createInterceptor(clientState: ClientState): Interceptor {
        return provideInterceptor(clientState = clientState)
    }

    fun createClient(clientState: ClientState): OkHttpClient {
        return provideClientBuilder().addInterceptor(createInterceptor(clientState = clientState))
            .build()
    }

    private fun provideClientBuilder() = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor {
                try {
                    JSONObject(it)
                    Logger.t(LOGGER_NAME).json(it)
                } catch (e: Exception) {
                    Logger.t(LOGGER_NAME).i(it)
                }
            }.apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }
        )

    private fun provideInterceptor(clientState: ClientState): Interceptor {
        return Interceptor { chain ->
            val tokenAddedRequest =
                clientRequestFactory.createTokenHeaderRequest(
                    clientState = clientState,
                    chain = chain
                )
            val response = chain.proceed(tokenAddedRequest)

            processResponse(
                clientState = clientState,
                chain = chain,
                response = response
            )
        }
    }

    private fun processResponse(
        clientState: ClientState,
        chain: Interceptor.Chain,
        response: Response,
    ): Response {
        return if (response.code == STATUS_CODE_NO_AUTHORIZATION || response.code == STATUS_CODE_FORBIDDEN) {
            response.close()
            val refreshRequest =
                clientRequestFactory.createRefreshedHeaderTokenRequest(
                    clientState = clientState,
                    chain = chain
                )
            chain.proceed(refreshRequest)
        } else {
            response
        }
    }


    companion object {
        private const val LOGGER_NAME = "ANIPEN_AUTH_LOGGER"
    }
}