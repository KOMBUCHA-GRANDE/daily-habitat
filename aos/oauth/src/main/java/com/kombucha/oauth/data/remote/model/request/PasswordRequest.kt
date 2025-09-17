package com.anipen.anipenauth.data.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
internal data class PasswordRequest(
    @SerialName("password")
    val password: String,
)
