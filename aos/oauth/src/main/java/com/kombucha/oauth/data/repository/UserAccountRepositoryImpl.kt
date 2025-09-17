package com.anipen.anipenauth.data.repository

import com.anipen.anipenauth.data.local.user.UserAccountLocalDataSource
import com.anipen.anipenauth.data.remote.user.UserAccountRemoteDataSource
import com.anipen.anipenauth.domain.model.UserInfo
import com.anipen.anipenauth.domain.repository.UserAccountRepository
import com.anipen.anipenauth.utils.DateUtils
import com.anipen.anipenauth.utils.NoAuthUserIdException
import com.anipen.anipenauth.utils.UserRefreshTokenExpiredException
import com.anipen.anipenauth.utils.toException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion

internal class UserAccountRepositoryImpl(
    private val userAccountRemoteDataSource: UserAccountRemoteDataSource,
    private val userAccountLocalDataSource: UserAccountLocalDataSource,
) : UserAccountRepository {

    override suspend fun fetchAccessToken(
        code: String,
        codeVerifier: String,
        clientId: String,
    ): Flow<Unit> { //통합 로그인 성공 후 서버에서 token 받아오고 로컬에 저장
        return userAccountRemoteDataSource.getAccessToken(
            code = code,
            codeVerifier = codeVerifier,
            clientId = clientId
        ).catch {
            throw it.toException()
        }.map { token ->
            userAccountLocalDataSource.setUserToken(
                accessToken = token.accessToken,
                refreshToken = token.refreshToken,
                idToken = token.idToken,
                accessTokenExpireIn = DateUtils.getCurrentUnixTime() + token.expiresIn
            )
            fetchAuthUserId().collect()
        }
    }

    override suspend fun getAccessCode(): Flow<String> { //회원탈퇴, 닉네임 변경 시 필요한 AccessCode 요청(authUserId가 없다면 재요청)
        var authUserId = userAccountLocalDataSource.getAuthUserId()
        if (authUserId.isEmpty()) {
            authUserId = fetchAuthUserId().firstOrNull()
                ?: return flowOf(Unit).map { throw NoAuthUserIdException() }
        }
        return userAccountRemoteDataSource.getAccessCode(authUserId = authUserId)
            .catch {
                throw it.toException()
            }.map { userInfo ->
                userInfo.guestCode
            }
    }

    override suspend fun getUserIdToken(): String {
        return userAccountLocalDataSource.getUserIdToken()
    }

    override suspend fun getAccessTokenInfo(): Pair<String, Long> {
        return Pair(
            userAccountLocalDataSource.getUserAccessToken(),
            userAccountLocalDataSource.getAccessTokenExpireTime()
        )
    }

    override suspend fun isLogin(): Flow<Boolean> {
        return userAccountRemoteDataSource.getUserInfo().map {
            true
        }.catch {
            return@catch emit(
                it !is UserRefreshTokenExpiredException
            )
        }
    }

    override suspend fun getUserInfo(): Flow<UserInfo> {
        val authUserId = userAccountLocalDataSource.getAuthUserId()
        return userAccountRemoteDataSource.getUserInfo(authUserId = authUserId).map {
            UserInfo(
                userId = it.userId,
                nickname = it.nickname,
                email = it.email,
                regData = it.regDate,
                lang = it.lang,
                level = it.level,
                authorized = it.authorized
            )
        }
    }

    override suspend fun logout(): Flow<Unit> { //서버에 로그아웃 요청 후 로컬 데이터 초기화
        val accessToken = userAccountLocalDataSource.getUserAccessToken()
        return if (accessToken.isNotEmpty()) {
            userAccountRemoteDataSource.logout(accessToken).onCompletion {
                clearUserInfo()
            }.catch {
                throw it.toException()
            }
        } else {
            flowOf(clearUserInfo())
        }
    }

    override suspend fun clearUserInfo() { //유저 관련 전체 정보 초기화
        userAccountLocalDataSource.clearPreferences()
    }

    private suspend fun fetchAuthUserId(): Flow<String> { //유저 authUserId 요청
        return userAccountRemoteDataSource.getUserInfo().catch {
            throw it.toException()
        }.map { result ->
            userAccountLocalDataSource.setAuthUserId(result.sub)
            result.sub
        }
    }
}