package com.anipen.anipenauth.domain.di

import com.anipen.anipenauth.domain.usecase.DeviceUseCase
import com.anipen.anipenauth.domain.usecase.GuestAccountUseCase
import com.anipen.anipenauth.domain.usecase.UserAccountUseCase
import org.koin.dsl.module


private val useCaseModule = module {
    single<DeviceUseCase> { DeviceUseCase(get()) }
    single<GuestAccountUseCase> { GuestAccountUseCase(get(), get()) }
    single<UserAccountUseCase> { UserAccountUseCase(get(), get()) }
}

internal val domainModule = useCaseModule