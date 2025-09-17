package com.anipen.anipenauth.feature.user

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import com.anipen.anipenauth.domain.model.AuthResponse
import com.anipen.anipenauth.domain.model.UserInfo
import com.anipen.anipenauth.domain.usecase.UserAccountUseCase
import com.anipen.anipenauth.feature.AnipenAuth
import com.anipen.anipenauth.feature.AnipenAuth.KEY_NICKNAME_CHANGE_ACTION
import com.anipen.anipenauth.feature.AnipenAuth.KEY_RESIGN_ACTION
import com.anipen.anipenauth.utils.NoAccessCodeException
import com.anipen.anipenauth.utils.NoNicknameChangeActionException
import com.anipen.anipenauth.utils.NoResignActionException
import com.anipen.anipenauth.utils.UserRefreshTokenExpiredException
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal class AnipenUserImpl(
    private val userAccountUseCase: UserAccountUseCase,
    private val webApiUtils: WebApiUtils,
) : AnipenUser {

    private val json = Json {
        encodeDefaults = true
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override suspend fun clearUserInfo() {
        Logger.i("AnipenAuth.anipenUser.clearUserInfo()")
        userAccountUseCase.clearUserInfo()
    }

    override suspend fun logout(): Flow<Unit> {
        return userAccountUseCase.logout()
    }

    override suspend fun isFirstUserLogin(): Boolean {
        return userAccountUseCase.isFirstUserLogin()
    }

    override suspend fun getUserIdToken(): String {
        return userAccountUseCase.getUserIdToken()
    }

    override suspend fun isLogin(): Boolean {
        return userAccountUseCase.isLogin().also { result ->
            if (!result) {
                CoroutineScope(Dispatchers.IO).launch {
                    clearUserInfo()
                }
            }
        }
    }

    override suspend fun requestUserAccountLoginWeb(
        redirectUrl: String,
        composableLoginLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
        activityResultLoginLauncher: ActivityResultLauncher<Intent>?,
    ) {
        val locale = userAccountUseCase.getLocale()
        val clientId = userAccountUseCase.getClientId()

        webApiUtils.authorizationWebApi(
            redirectUrl = redirectUrl,
            locale = locale,
            clientId = clientId,
            managedActivityResultLauncher = composableLoginLauncher,
            activityResultLoginLauncher = activityResultLoginLauncher,
        )
    }

    @Composable
    override fun loginLauncher(
        onError: (String) -> Unit,
        onSuccess: () -> Unit,
    ): ManagedActivityResultLauncher<Intent, ActivityResult> {
        return rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { intent ->
            val result = intent.data?.getStringExtra(VALUE_AUTH_RESPONSE)
            val error = intent.data?.getStringExtra(VALUE_AUTH_ERROR)
            if (error != null) {
                onError(error)
            } else if (result != null) {
                val authResponse = json.decodeFromString(AuthResponse.serializer(), result)
                CoroutineScope(Dispatchers.IO).launch {
                    fetchAccessToken(
                        code = authResponse.code,
                        codeVerifier = authResponse.request.codeVerifier
                    ).catch {
                    }.collect()
                    withContext(Dispatchers.Main) {
                        onSuccess.invoke()
                    }
                }
            }
        }
    }

    override fun loginLauncher(
        context: Context,
        onError: (String) -> Unit,
        onSuccess: () -> Unit,
    ): ActivityResultLauncher<Intent>? {
        return (context as? AppCompatActivity)?.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { intent ->
            val result = intent.data?.getStringExtra(VALUE_AUTH_RESPONSE)
            val error = intent.data?.getStringExtra(VALUE_AUTH_ERROR)
            if (error != null) {
                onError(error)
            } else if (result != null) {
                val authResponse = json.decodeFromString(AuthResponse.serializer(), result)
                CoroutineScope(Dispatchers.IO).launch {
                    fetchAccessToken(
                        code = authResponse.code,
                        codeVerifier = authResponse.request.codeVerifier
                    ).catch {
                    }.collect()
                    withContext(Dispatchers.Main) {
                        onSuccess.invoke()
                    }
                }
            }
        }
    }

    override suspend fun requestUserNicknameChangeWeb(
        redirectUrl: String,
        onSuccess: suspend () -> Unit,
    ) {
        val action = AnipenAuth.getAction(key = KEY_NICKNAME_CHANGE_ACTION)
            ?: throw NoNicknameChangeActionException()
        AnipenAuth.setRedirectActionOnSuccess(
            key = KEY_NICKNAME_CHANGE_ACTION,
            onSuccess = onSuccess
        )
        val clientId = userAccountUseCase.getClientId()
        val locale = userAccountUseCase.getLocale()
        userAccountUseCase.getAccessCode().catch {
            when (it) {
                is UserRefreshTokenExpiredException -> {
                    throw it
                }

                else -> {
                    throw NoAccessCodeException()
                }
            }
        }.collectLatest { accessCode ->
            webApiUtils.nicknameChangeWebApi(
                redirectUrl = redirectUrl,
                action = action,
                accessCode = accessCode,
                clientId = clientId,
                locale = locale
            )
        }
    }

    override suspend fun requestResignWeb(redirectUrl: String, onSuccess: suspend () -> Unit) {
        val action = AnipenAuth.getAction(key = KEY_RESIGN_ACTION)
            ?: throw NoResignActionException()
        AnipenAuth.setRedirectActionOnSuccess(
            key = KEY_RESIGN_ACTION,
            onSuccess = onSuccess
        )
        val clientId = userAccountUseCase.getClientId()
        val locale = userAccountUseCase.getLocale()
        userAccountUseCase.getAccessCode().catch {
            when (it) {
                is UserRefreshTokenExpiredException -> {
                    throw it
                }

                else -> {
                    throw NoAccessCodeException()
                }
            }
        }.collectLatest { accessCode ->
            webApiUtils.requestResignWebApi(
                redirectUrl = redirectUrl,
                action = action,
                accessCode = accessCode,
                clientId = clientId,
                locale = locale
            )
        }
    }

    override suspend fun getUserInfo(): Flow<UserInfo> {
        return userAccountUseCase.getUserInfo()
    }

    override suspend fun isPassAccessTokenExpireTime(): Boolean {
        return userAccountUseCase.isPassAccessTokenExpireTime()
    }

    private suspend fun fetchAccessToken(
        code: String,
        codeVerifier: String,
    ): Flow<Unit> {
        val clientId = userAccountUseCase.getClientId()
        return userAccountUseCase.fetchAccessToken(
            code = code,
            codeVerifier = codeVerifier,
            clientId = clientId
        ).onEach {
            userAccountUseCase.setIsFirstUserLogin(isFirstLogin = false)
        }
    }

    companion object {
        private const val VALUE_AUTH_RESPONSE = "net.openid.appauth.AuthorizationResponse"
        private const val VALUE_AUTH_ERROR = "net.openid.appauth.AuthorizationException"
    }
}