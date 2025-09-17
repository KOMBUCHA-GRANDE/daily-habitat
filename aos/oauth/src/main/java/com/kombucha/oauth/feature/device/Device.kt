package com.anipen.anipenauth.feature.device

interface Device {

    // 사용자의 현재 언어를 로컬에 저장
    suspend fun setLocale(locale: String)

    // 사용자의 현재 사용 언어가 변경되었는지 확인
    suspend fun isChangeLocale(): Boolean

    // 사용자가 사용하는 애니펜 앱의 클라이언트 id 저장
    suspend fun setClientId(clientId: String)
}