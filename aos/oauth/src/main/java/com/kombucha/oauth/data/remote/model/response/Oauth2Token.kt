package com.anipen.anipenauth.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Oauth2Token(
    @SerialName("access_token")
    var accessToken: String = "",
    @SerialName("token_type")
    var tokenType: String = "",
    @SerialName("expires_in")
    var expiresIn: Long = 0,
    @SerialName("refresh_token")
    var refreshToken: String = "",
    @SerialName("id_token")
    var idToken: String = "",
    @SerialName("guest_code")
    var guestCode: String = "",
)
