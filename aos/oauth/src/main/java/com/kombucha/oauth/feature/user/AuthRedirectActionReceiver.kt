package com.anipen.anipenauth.feature.user

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.anipen.anipenauth.feature.AnipenAuth
import com.anipen.anipenauth.feature.AnipenAuth.KEY_NICKNAME_CHANGE_ACTION
import com.anipen.anipenauth.feature.AnipenAuth.KEY_RESIGN_ACTION
import com.anipen.anipenauth.feature.AnipenAuth.getRedirectActionOnSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
닉네임 변경, 회원 탈퇴 web api 후에 결과를 받아오는 broadcastReceiver
사용하는 앱의 manifest에 해당 receiver를 등록하고 닉네임 변경, 회원탈퇴에서 받을 actioin을 intent-filter에 지정해주세요.
Ex) : com.anipen.aniemoji.HANDLE_CHANGE_NICKNAME_RESPONSE
 */
class AuthRedirectActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        checkIntent(intent = intent)
    }

    private fun checkIntent(intent: Intent) {
        when (intent.action) {
            AnipenAuth.getAction(KEY_NICKNAME_CHANGE_ACTION) -> {
                CoroutineScope(Dispatchers.IO).launch {
                    getRedirectActionOnSuccess(KEY_NICKNAME_CHANGE_ACTION)?.invoke()
                }
            }

            AnipenAuth.getAction(KEY_RESIGN_ACTION) -> {
                CoroutineScope(Dispatchers.IO).launch {
                    AnipenAuth.anipenUser.clearUserInfo()
                    getRedirectActionOnSuccess(KEY_RESIGN_ACTION)?.invoke()
                }
            }
        }
    }
}