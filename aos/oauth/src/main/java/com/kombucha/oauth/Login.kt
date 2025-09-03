package com.kombucha.oauth

import android.content.Context

interface Login {

    fun requestLogin(context: Context)

    fun logout()
}