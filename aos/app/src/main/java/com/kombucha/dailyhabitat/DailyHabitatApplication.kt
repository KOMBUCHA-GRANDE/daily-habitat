package com.kombucha.dailyhabitat

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class DailyHabitatApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}