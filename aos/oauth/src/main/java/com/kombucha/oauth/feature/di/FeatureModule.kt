package com.anipen.anipenauth.feature.di

import com.anipen.anipenauth.feature.device.Device
import com.anipen.anipenauth.feature.device.DeviceImpl
import com.anipen.anipenauth.feature.guest.AnipenGuest
import com.anipen.anipenauth.feature.guest.AnipenGuestImpl
import com.anipen.anipenauth.feature.user.AnipenUser
import com.anipen.anipenauth.feature.user.AnipenUserImpl
import com.anipen.anipenauth.feature.user.WebApiUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val guestModule = module {
    single<AnipenGuest> { AnipenGuestImpl(get()) }
}


private val userModule = module {
    single<AnipenUser> { AnipenUserImpl(get(), get()) }
    single<WebApiUtils> { WebApiUtils(androidContext()) }
}

private val deviceModule = module {
    single<Device> { DeviceImpl(get()) }
}

internal val featureModule = guestModule + userModule + deviceModule