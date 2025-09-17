package com.anipen.anipenauth.data.remote.di

import com.anipen.anipenauth.data.remote.guest.GuestAccountRemoteDataSource
import com.anipen.anipenauth.data.remote.guest.GuestAccountRemoteDataSourceImpl
import com.anipen.anipenauth.data.remote.network.api.GuestAccountApi
import com.anipen.anipenauth.data.remote.network.api.TokenApi
import com.anipen.anipenauth.data.remote.network.api.UserAccountApi
import com.anipen.anipenauth.data.remote.network.api.UserTokenApi
import com.anipen.anipenauth.data.remote.network.client.AnipenClient
import com.anipen.anipenauth.data.remote.network.client.ClientFactory
import com.anipen.anipenauth.data.remote.network.client.ClientRequestFactory
import com.anipen.anipenauth.data.remote.network.client.ClientState
import com.anipen.anipenauth.data.remote.network.retrofit.RetrofitService
import com.anipen.anipenauth.data.remote.token.TokenRemoteDatasource
import com.anipen.anipenauth.data.remote.token.TokenRemoteDatasourceImpl
import com.anipen.anipenauth.data.remote.user.UserAccountRemoteDataSource
import com.anipen.anipenauth.data.remote.user.UserAccountRemoteDataSourceImpl
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

private val networkModule = module {
    single { RetrofitService() }
    single { ClientFactory(get()) }
    single { ClientRequestFactory(get()) }
    single<AnipenClient> { ClientFactory(get()) }

    single<OkHttpClient>(named(CLIENT_GUEST)) {
        get<ClientFactory>().createClient(clientState = ClientState.GUEST)
    }
    single<OkHttpClient>(named(CLIENT_USER)) {
        get<ClientFactory>().createClient(ClientState.USER)
    }

    single<Retrofit>(named(RETROFIT_GUEST)) {
        get<RetrofitService>().build(get(qualifier = named(CLIENT_GUEST)))
    }
    single<Retrofit>(named(RETROFIT_USER)) {
        get<RetrofitService>().build(get(qualifier = named(CLIENT_USER)))
    }
    single<Retrofit>(named(RETROFIT_NO_TOKEN)) { get<RetrofitService>().build() }

}

private val apiModule = module {
    single { get<Retrofit>(qualifier = named(RETROFIT_GUEST)).create(GuestAccountApi::class.java) }
    single { get<Retrofit>(qualifier = named(RETROFIT_NO_TOKEN)).create(TokenApi::class.java) }
    single { get<Retrofit>(qualifier = named(RETROFIT_USER)).create(UserAccountApi::class.java) }
    single { get<Retrofit>(qualifier = named(RETROFIT_NO_TOKEN)).create(UserTokenApi::class.java) }
}

private val datasourceModule = module {
    single<GuestAccountRemoteDataSource> { GuestAccountRemoteDataSourceImpl(get()) }
    single<UserAccountRemoteDataSource> { UserAccountRemoteDataSourceImpl(get(), get()) }
    single<TokenRemoteDatasource> { TokenRemoteDatasourceImpl(get()) }
}

internal val remoteModule = networkModule + apiModule + datasourceModule


private const val CLIENT_GUEST = "guestClient"
private const val CLIENT_USER = "userClient"

private const val RETROFIT_GUEST = "guestRetrofit"
private const val RETROFIT_USER = "userRetrofit"
private const val RETROFIT_NO_TOKEN = "noTokenRetrofit"
