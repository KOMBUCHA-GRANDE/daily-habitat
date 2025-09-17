package com.anipen.anipenauth.data.di

import com.anipen.anipenauth.data.local.di.localModule
import com.anipen.anipenauth.data.remote.di.remoteModule
import com.anipen.anipenauth.data.repository.DeviceRepositoryImpl
import com.anipen.anipenauth.data.repository.GuestAccountRepositoryImpl
import com.anipen.anipenauth.data.repository.TokenRepositoryImpl
import com.anipen.anipenauth.data.repository.UserAccountRepositoryImpl
import com.anipen.anipenauth.domain.repository.DeviceRepository
import com.anipen.anipenauth.domain.repository.GuestAccountRepository
import com.anipen.anipenauth.domain.repository.TokenRepository
import com.anipen.anipenauth.domain.repository.UserAccountRepository
import org.koin.dsl.module

private val repositoryModule = module {
    single<DeviceRepository> { DeviceRepositoryImpl(get()) }
    single<GuestAccountRepository> { GuestAccountRepositoryImpl(get(), get()) }
    single<UserAccountRepository> { UserAccountRepositoryImpl(get(), get()) }
    single<TokenRepository> { TokenRepositoryImpl(get(), get(), get(), get()) }
}

internal val dataModule = localModule + remoteModule + repositoryModule