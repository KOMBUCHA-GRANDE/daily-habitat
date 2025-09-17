package com.anipen.anipenauth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("idToken")
    val idToken: String,
    @SerialName("os")
    val os: Int,
)
