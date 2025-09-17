package com.anipen.anipenauth.data.utils

import com.anipen.anipenauth.data.local.guest.GuestAccountLocalDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

internal object AuthHelper : KoinComponent {

    private val guestAccountLocalDataSource: GuestAccountLocalDataSource by inject()

    suspend fun getUniqueId(): String {
        var uniqueId = guestAccountLocalDataSource.getUniqueId()
        if (uniqueId.isEmpty()) {
            uniqueId = createUniqueId()
            guestAccountLocalDataSource.setUniqueId(uniqueId = uniqueId)
        }
        return uniqueId
    }

    suspend fun getRandomPassword(): String {
        var randomPassword = guestAccountLocalDataSource.getRandomPassword()
        if (randomPassword.isEmpty()) {
            randomPassword = createRandomPassword()
            guestAccountLocalDataSource.setRandomPassword(randomPassword = randomPassword)
        }
        return randomPassword
    }

    suspend fun getNonce(): String {
        var nonce = guestAccountLocalDataSource.getNonce()
        if (nonce.isEmpty()) {
            nonce = createNonce()
            guestAccountLocalDataSource.setNonce(nonce)
        }
        return nonce
    }

    private fun createRandomPassword(): String { //게스트 로그인에 필요한 랜덤 패스워드 생성
        return (0 until PASSWORD_LENGTH).map {
            ALLOWED_PASSWORD_CHARACTERS.random()
        }.joinToString(separator = "")
    }

    private fun createUniqueId(): String { //게스트 로그인에 필요한 식별자 id 생성
        return UUID.randomUUID().toString()
    }

    private fun createNonce(): String { //게스트 로그인 시 보안을 위한 nonce 생성
        return (0 until NONCE_LENGTH).map {
            ALLOWED_NONCE_CHARACTERS.random()
        }.joinToString("")
    }

    private const val ALLOWED_PASSWORD_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"
    private const val PASSWORD_LENGTH = 16
    private const val ALLOWED_NONCE_CHARACTERS =
        "0123456789ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijklmnopqrstuvwxyz-._"
    private const val NONCE_LENGTH = 48
}