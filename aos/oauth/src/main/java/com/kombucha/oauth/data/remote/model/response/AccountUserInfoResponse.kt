package com.anipen.anipenauth.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AccountUserInfoResponse(
    @SerialName("sub")
    val sub: String = "",
    @SerialName("aud")
    val aud: String = "",
    @SerialName("level")
    val level: Int = -1,
    @SerialName("scope")
    val scope: List<String> = emptyList(),
    @SerialName("iss")
    val iss: String = "",
    @SerialName("nickname")
    val nickname: String = "",
    @SerialName("rid")
    val rid: String = "",
    @SerialName("exp")
    val exp: Long = -1,
    @SerialName("iat")
    val iat: Long = -1,
    @SerialName("jti")
    val jti: String = "",
    @SerialName("email")
    val email: String = "",
    @SerialName("userId")
    val userId: String = "",
    @SerialName("url")
    val url: String = "",
    @SerialName("expireAt")
    val expireAt: String = "",
    @SerialName("lang")
    val lang: String = "",
    @SerialName("authorized")
    val authorized: Int = -1,
    @SerialName("regData")
    val regDate: String = "",
    @SerialName("guest_code")
    val guestCode: String = "",
    @SerialName("expires_in")
    val expiresIn: Long = 0,
)
