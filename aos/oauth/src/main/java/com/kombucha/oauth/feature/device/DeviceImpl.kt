package com.anipen.anipenauth.feature.device

import com.anipen.anipenauth.domain.usecase.DeviceUseCase
import java.util.Locale

internal class DeviceImpl(
    private val deviceUseCase: DeviceUseCase,
) : Device {

    // 사용자의 현재 위치를 로컬에 저장
    override suspend fun setLocale(locale: String) {
        deviceUseCase.setLocale(locale = locale)
    }

    override suspend fun isChangeLocale(): Boolean {
        return deviceUseCase.getLocale() != Locale.getDefault().language
    }


    // 사용자가 사용하는 애니펜 앱의 클라이언트 id 저장
    override suspend fun setClientId(clientId: String) {
        deviceUseCase.setClientId(clientId = clientId)
    }
}