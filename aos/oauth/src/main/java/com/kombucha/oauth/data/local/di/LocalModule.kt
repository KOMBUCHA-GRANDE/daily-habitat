package com.anipen.anipenauth.data.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.anipen.anipenauth.data.local.device.DeviceLocalDataSource
import com.anipen.anipenauth.data.local.device.DeviceLocalDataSourceImpl
import com.anipen.anipenauth.data.local.guest.GuestAccountLocalDataSource
import com.anipen.anipenauth.data.local.guest.GuestAccountLocalDataSourceImpl
import com.anipen.anipenauth.data.local.user.UserAccountLocalDataSource
import com.anipen.anipenauth.data.local.user.UserAccountLocalDataSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val datasourceModule = module {
    single<DeviceLocalDataSource> {
        DeviceLocalDataSourceImpl(get(qualifier = named(PREFERENCES_DEVICE_STORE)))
    }
    single<GuestAccountLocalDataSource> {
        GuestAccountLocalDataSourceImpl(get(qualifier = named(PREFERENCES_GUEST_ACCOUNT_STORE)))
    }
    single<UserAccountLocalDataSource> {
        UserAccountLocalDataSourceImpl(get(qualifier = named(PREFERENCES_USER_ACCOUNT_STORE)))
    }
}

private val dataStoreModule = module {
    single<DataStore<Preferences>>(named(PREFERENCES_GUEST_ACCOUNT_STORE)) {
        provideDataStore(
            androidApplication(),
            PREFERENCES_GUEST_ACCOUNT_STORE
        )
    }
    single<DataStore<Preferences>>(named(PREFERENCES_DEVICE_STORE)) {
        provideDataStore(
            androidApplication(),
            PREFERENCES_DEVICE_STORE
        )
    }
    single<DataStore<Preferences>>(named(PREFERENCES_USER_ACCOUNT_STORE)) {
        provideDataStore(
            androidApplication(),
            PREFERENCES_USER_ACCOUNT_STORE
        )
    }
}

private fun provideDataStore(context: Context, storeName: String): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(produceFile = {
        context.preferencesDataStoreFile(storeName)
    })
}

internal val localModule = datasourceModule + dataStoreModule

private const val PREFERENCES_GUEST_ACCOUNT_STORE = "AnipenGuestAccountDataStore"
private const val PREFERENCES_DEVICE_STORE = "AnipenDeviceDataStore"
private const val PREFERENCES_USER_ACCOUNT_STORE = "AnipenUserAccountDataStore"
