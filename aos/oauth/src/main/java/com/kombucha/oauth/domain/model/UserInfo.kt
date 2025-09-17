package com.anipen.anipenauth.domain.model

data class UserInfo(
    val userId: String,
    val nickname: String,
    val email: String,
    val regData: String,
    val lang: String,
    val level: Int,
    val authorized: Int,
)
