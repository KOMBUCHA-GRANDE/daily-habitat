package com.kombucha.oauth

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import java.security.SecureRandom
import java.util.Base64

class GoogleLogin : Login {

    private val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
        .setAutoSelectEnabled(true)
        .setNonce(getNounce())
        .build()

    private val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    override suspend fun requestLogin(context: Context) {
        val result = signIn(context = context)
    }

    private suspend fun signIn(context: Context): String {
        return try {
            val credentialManager = CredentialManager.create(context = context)
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )
            val credential = result.credential
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                googleCredential.idToken
            } else {
                error("")
            }
        } catch (e: GetCredentialException) {
            Log.d("kgb", e.toString())
            throw e
        }
    }

    override fun logout() {
    }

    private fun getNounce(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(ByteArray(16).also { SecureRandom().nextBytes(it) })
        } else {
            val buf = ByteArray(16)
            SecureRandom().nextBytes(buf)
            android.util.Base64.encodeToString(
                buf,
                android.util.Base64.URL_SAFE or
                        android.util.Base64.NO_WRAP or
                        android.util.Base64.NO_PADDING
            )
        }
    }
}