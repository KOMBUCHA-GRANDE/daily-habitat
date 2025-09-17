package com.anipen.anipenauth.feature

import android.content.Context
import com.anipen.anipenauth.data.di.dataModule
import com.anipen.anipenauth.data.remote.network.client.AnipenClient
import com.anipen.anipenauth.domain.di.domainModule
import com.anipen.anipenauth.feature.device.Device
import com.anipen.anipenauth.feature.di.featureModule
import com.anipen.anipenauth.feature.guest.AnipenGuest
import com.anipen.anipenauth.feature.user.AnipenUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.error.KoinAppAlreadyStartedException
import org.koin.java.KoinJavaComponent.inject
import java.util.Locale

object AnipenAuth {

    private val koinModule = featureModule + dataModule + domainModule

    val anipenGuest: AnipenGuest by inject(AnipenGuest::class.java) //게스트 로그인 관련 인터페이스
    val anipenUser: AnipenUser by inject(AnipenUser::class.java) //유저 계정 관련 기능 인터페이스
    val device: Device by inject(Device::class.java)

    private val anipenClient: AnipenClient by inject(AnipenClient::class.java)

    private val redirectActionMap = hashMapOf<String, String>()
    private val redirectActionOnSuccessMap = hashMapOf<String, suspend () -> Unit>()

    /**
     * anipenAuth를 사용하기 위해 MainApplication에서 선언 필요
     * 앱에서 Koin 사용 중일 경우 koin 선언 후에 해당 함수 호출필요.
     * @param context: androidContext 필요
     * @param clientId: 사용하는 앱의 clientId
     * @param actionNickNameChange: 앱에서 닉네임 변경 이후 받을 action 지정 - AuthRedirectActionReceiver intent filter action과 동일
     * @param actionResign: 앱에서 회원탈퇴 이후 받을 action 지정 - AuthRedirectActionReceiver intent filter action과 동일
     */
    fun initAnipenAuth(
        context: Context,
        clientId: String,
        actionNickNameChange: String? = null,
        actionResign: String? = null,
    ) {
        try {
            startKoin {
                androidContext(context)
                loadKoinModules(koinModule)
            }
        } catch (e: KoinAppAlreadyStartedException) {
            loadKoinModules(koinModule)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val locale = Locale.getDefault().language
            device.setLocale(locale = locale)
            device.setClientId(clientId = clientId)
            actionNickNameChange?.let {
                redirectActionMap[KEY_NICKNAME_CHANGE_ACTION] = it
            }
            actionResign?.let {
                redirectActionMap[KEY_RESIGN_ACTION] = it
            }
        }
    }

    /**
     * userAccessToken을 헤더에 넣고 만료 시 갱신해주는 클라이언트를 생성
     */
    fun createUserTokenInterceptor(): Interceptor {
        return anipenClient.createInterceptor()
    }

    internal fun getAction(key: String): String? {
        return redirectActionMap[key]
    }

    internal fun setRedirectActionOnSuccess(key: String, onSuccess: suspend () -> Unit) {
        redirectActionOnSuccessMap[key] = onSuccess
    }

    internal fun getRedirectActionOnSuccess(key: String): (suspend () -> Unit)? {
        return redirectActionOnSuccessMap[key]
    }

    const val TAG_LOGIN = "anipenAuthLogin"
    internal const val KEY_NICKNAME_CHANGE_ACTION = "nickNameChange"
    internal const val KEY_RESIGN_ACTION = "resign"
}