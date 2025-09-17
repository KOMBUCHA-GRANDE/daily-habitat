package com.anipen.anipenauth.data.remote.network.retrofit

import com.anipen.anipenauth.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

internal class RetrofitService {

    private val json = Json { ignoreUnknownKeys = true }

    fun build(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .build()

    fun build(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}