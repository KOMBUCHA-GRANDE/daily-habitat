package com.anipen.anipenauth.feature.user

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import com.anipen.anipenauth.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface AnipenUser {

    /**
     * 유저 로컬 정보 모두 초기화
     */
    suspend fun clearUserInfo()


    /**
     * 서버에 유저 token을 초기화 요청 후 유저 로컬 정보 모두 초기화
     */
    suspend fun logout(): Flow<Unit>

    /**
     * 사용자가 처음 로그인인지 여부를 확인
     */
    suspend fun isFirstUserLogin(): Boolean

    /**
     * 유저의 idToken 받기
     */
    suspend fun getUserIdToken(): String


    /**
     * 서버에 userToken이 유효한지 검사
     */
    suspend fun isLogin(): Boolean

    /**
     * 통합 로그인을 진행하고 토큰을 로컬에 저장. 사용자가 지정한 액션을 진행
     * launcher 우선 순위: 1. activityResultLoginLauncher -> 2. composableLoginLauncher
     * @param redirectUrl: 앱에서 로그인 성공 후 요청하는 redirectUrl
     * @param composableLoginLauncher: 컴포저블에서 사용할 수 있는 loginLauncher[onError: 로그인 실패 시, onSuccess: 로그인 성공 시]
     * @param activityResultLoginLauncher: Activity에서 등록하고 사용할 수 있는 loginLauncher[onError: 로그인 실패 시, onSuccess: 로그인 성공 시]
     */
    suspend fun requestUserAccountLoginWeb(
        redirectUrl: String,
        composableLoginLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>? = null,
        activityResultLoginLauncher: ActivityResultLauncher<Intent>? = null,
    )

    /**
     * Composable에서 사용할 수 있는 LoginLauncher 발행
     */
    @Composable
    fun loginLauncher(
        onError: (String) -> Unit,
        onSuccess: () -> Unit,
    ): ManagedActivityResultLauncher<Intent, ActivityResult>

    /**
     * Activity에서 등록하고 사용할 수 있는 LoginLauncher 발행
     */
    fun loginLauncher(
        context: Context,
        onError: (String) -> Unit,
        onSuccess: () -> Unit,
    ): ActivityResultLauncher<Intent>?

    /**
     * 닉네임 변경을 요청
     * @param redirectUrl: redirect될 url
     */
    suspend fun requestUserNicknameChangeWeb(redirectUrl: String, onSuccess: suspend () -> Unit)

    /**
     * 회원탈퇴를 요청
     * @param redirectUrl: redirect될 url
     * @param onSuccess: 회원탈퇴 성공 후 로직
     */
    suspend fun requestResignWeb(redirectUrl: String, onSuccess: suspend () -> Unit)


    /**
     * 회원정보 요청
     */
    suspend fun getUserInfo(): Flow<UserInfo>

    /**
     * accessToken expire 시간 다 되었는 지 체크(로컬 체크)
     */
    suspend fun isPassAccessTokenExpireTime(): Boolean
}