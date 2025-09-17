package com.anipen.anipenauth.feature.user

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.browser.customtabs.CustomTabsIntent
import com.anipen.anipenauth.BuildConfig
import com.anipen.anipenauth.data.remote.constant.QueryConstant.KEY_DEVICE_OS
import com.anipen.anipenauth.data.remote.constant.QueryConstant.KEY_LANGUAGE
import com.anipen.anipenauth.data.remote.constant.QueryConstant.VALUE_DEVICE_OS
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

internal class WebApiUtils(
    private val context: Context,
) {

    private val authorizationService by lazy {
        AuthorizationService(context)
    }

    //통합 로그인 WebApi 요청
    fun authorizationWebApi(
        redirectUrl: String,
        clientId: String,
        locale: String,
        managedActivityResultLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
        activityResultLoginLauncher: ActivityResultLauncher<Intent>?,
    ) {
        val serviceConfiguration = AuthorizationServiceConfiguration(
            Uri.parse("${BuildConfig.BASE_URL}${UrlHelper.URL_ACCOUNT_AUTH.url}"),
            Uri.parse("${BuildConfig.BASE_URL}${UrlHelper.URL_ACCOUNT_TOKEN.url}")
        )

        val redirectUri = Uri.parse(redirectUrl)
        val builder = AuthorizationRequest.Builder(
            serviceConfiguration,
            clientId,
            ResponseTypeValues.CODE,
            redirectUri
        )
        builder.setScopes(AuthorizationRequest.Scope.PROFILE)
        val additionalParams = hashMapOf<String, String>().apply {
            put(KEY_LANGUAGE, locale)
            put(KEY_DEVICE_OS, VALUE_DEVICE_OS)
        }
        builder.setAdditionalParameters(additionalParams)
        val request = builder.build()
        val intent = authorizationService.getAuthorizationRequestIntent(request)
        activityResultLoginLauncher?.let {
            it.launch(intent)
            return
        }
        managedActivityResultLauncher?.launch(intent)
    }

    //닉네임 변경 WebApi 요청
    fun nicknameChangeWebApi(
        redirectUrl: String,
        action: String,
        accessCode: String,
        clientId: String,
        locale: String,
    ) {
        val serviceConfiguration =
            AuthorizationServiceConfiguration(
                Uri.parse("${BuildConfig.BASE_URL}${UrlHelper.URL_ACCOUNT_CHANGE_NICKNAME.url}"),
                Uri.parse("${BuildConfig.BASE_URL}${UrlHelper.URL_ACCOUNT_CHANGE_NICKNAME.url}"),
            )

        val redirectUri = Uri.parse(redirectUrl)
        val builder = AuthorizationRequest.Builder(
            serviceConfiguration,
            clientId,
            ResponseTypeValues.CODE,
            redirectUri
        )
        val additionalParameters = hashMapOf<String, String>().apply {
            put(KEY_ACCESS_CODE, accessCode)
            put(KEY_LANGUAGE, locale)
        }
        builder.setAdditionalParameters(additionalParameters)
        val request = builder.build()

        val intent = Intent(context, AuthRedirectActionReceiver::class.java).apply {
            this.action = action
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            request.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val intentBuilder = CustomTabsIntent.Builder()
        intentBuilder.setShowTitle(false)
        intentBuilder.setUrlBarHidingEnabled(true)
        authorizationService.performAuthorizationRequest(
            request,
            pendingIntent,
            intentBuilder.build()
        )
    }

    //회원탈퇴 WebApi 요청
    fun requestResignWebApi(
        redirectUrl: String,
        clientId: String,
        locale: String,
        accessCode: String,
        action: String,
    ) {
        val serviceConfiguration =
            AuthorizationServiceConfiguration(
                Uri.parse("${BuildConfig.BASE_URL}${UrlHelper.URL_ACCOUNT_RESIGN.url}"),
                Uri.parse("${BuildConfig.BASE_URL}${UrlHelper.URL_ACCOUNT_RESIGN.url}"),
            )
        val redirectUri = Uri.parse(redirectUrl)
        val builder = AuthorizationRequest.Builder(
            serviceConfiguration,
            clientId,
            ResponseTypeValues.CODE,
            redirectUri
        )
        val additionalParameters = hashMapOf<String, String>().apply {
            put(KEY_ACCESS_CODE, accessCode)
            put(KEY_DEVICE_OS, VALUE_DEVICE_OS)
            put(KEY_LANGUAGE, locale)
        }
        builder.setAdditionalParameters(additionalParameters)

        val request = builder.build()

        val intent = Intent(context, AuthRedirectActionReceiver::class.java).apply {
            this.action = action
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            request.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val intentBuilder = CustomTabsIntent.Builder()
        intentBuilder.setShowTitle(false)
        intentBuilder.setUrlBarHidingEnabled(true)
        authorizationService.performAuthorizationRequest(
            request,
            pendingIntent,
            intentBuilder.build()
        )
    }

    companion object {
        private const val KEY_ACCESS_CODE = "code"
    }
}