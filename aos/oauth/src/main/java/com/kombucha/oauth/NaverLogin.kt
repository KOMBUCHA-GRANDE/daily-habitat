package com.kombucha.oauth

import android.content.Context
import android.util.Log
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback

class NaverLogin : Login {

    private val loginCallback = object : OAuthLoginCallback {
        override fun onError(errorCode: Int, message: String) {
            Log.e("kgb", message)
        }

        override fun onFailure(httpStatus: Int, message: String) {
            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            Log.e("kgb", "errorCode : $errorCode, errorDescriptions: $errorDescription")
        }

        override fun onSuccess() {
            Log.d("kgb", "AccessToken: ${NaverIdLoginSDK.getAccessToken()}")
            Log.d("kgb", "RefreshToken: ${NaverIdLoginSDK.getRefreshToken()}")
            Log.d("kgb", "ExpiresAt: ${NaverIdLoginSDK.getExpiresAt()}")
            Log.d("kgb", "TokenType: ${NaverIdLoginSDK.getTokenType()}")
        }
    }

    override suspend fun requestLogin(context: Context) {
        NaverIdLoginSDK.authenticate(context, loginCallback)
    }

    override fun logout() {
    }
}