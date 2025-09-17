package com.anipen.anipenauth.feature.guest

import kotlinx.coroutines.flow.Flow

interface AnipenGuest {

    suspend fun guestLogin(): Flow<Unit> //애니펜 통합 로그인을 게스트 로그인으로 시도

    suspend fun getGuestCode(): Flow<String> //현재 로그인 된 게스트 계정의 게스트 코드를 받아오기(유저 로그인 시 전환을 위해 필요)

}