package com.anipen.anipenauth.domain.model

internal data class Token(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String,
)
