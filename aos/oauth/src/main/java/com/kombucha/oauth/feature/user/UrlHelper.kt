package com.anipen.anipenauth.feature.user

internal enum class UrlHelper(val url: String) {
    URL_ACCOUNT_AUTH("oauth2/v1/auth"),
    URL_ACCOUNT_TOKEN("oauth2/v1/token"),
    URL_ACCOUNT_RESIGN("page/v1/user/resign"),
    URL_ACCOUNT_CHANGE_NICKNAME("page/v1/user/nickname"),
}