package com.kombucha.oauth

import android.content.Context

interface Login {

    suspend fun requestLogin(context: Context)

    fun logout()
}