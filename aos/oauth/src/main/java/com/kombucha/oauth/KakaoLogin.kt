package com.kombucha.oauth

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class KakaoLogin : Login {

    override fun requestLogin(context: Context) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context = context) { token, error ->
                if (error != null) {
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    loginWithKakaoAccount(context)
                } else if (token != null) {
                    onLoginResult(token = token, error = null)
                }
            }
        } else {
            loginWithKakaoAccount(context = context)
        }
    }

    private fun loginWithKakaoAccount(context: Context) {
        UserApiClient.instance.loginWithKakaoAccount(context = context, callback = ::onLoginResult)
    }

    private fun onLoginResult(token: OAuthToken?, error: Throwable?) {
        if (error != null) {

        } else if (token != null) {

        }
    }

}