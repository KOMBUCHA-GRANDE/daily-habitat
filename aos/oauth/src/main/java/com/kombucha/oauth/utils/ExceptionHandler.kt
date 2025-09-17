package com.anipen.anipenauth.utils

import android.util.Log
import retrofit2.HttpException
import java.io.IOException

open class AnipenAuthException(message: String? = null) : IOException(message)

open class NeedLogoutException(message: String? = null) : AnipenAuthException(message)

class GuestRefreshTokenExpiredException : AnipenAuthException() //RefreshToken 만료 된 경우
class UserRefreshTokenExpiredException : NeedLogoutException() //RefreshToken 만료 된 경우
class NoUserAccessTokenException : NeedLogoutException() //AccessToken이 없는 경우
class NoUserIdTokenException : NeedLogoutException() //IdToken이 없을 경우
class NoAccessCodeException : AnipenAuthException() //AccessCode가 없을 경우
class NoAuthUserIdException : NeedLogoutException() //AuthUserId가 없을 경우
class ForbiddenCodeException : AnipenAuthException() //해당 accessCode가 거절된 경우
class NoNicknameChangeActionException :
    AnipenAuthException("initAnipenAuth에서 nicknameChange action을 지정해주세요.") //닉네임변경 action을 지정하지 않은 경우

class NoResignActionException :
    AnipenAuthException("initAnipenAuth에서 resign action을 지정해주세요.") //회원탈퇴 action을 지정하지 않은 경우


class ServerError : IOException() // 서버 에러

fun Throwable.toException(): Throwable {
    Log.d(ANIPEN_AUTH_ERROR, this.toString())
    return when (this) {
        is HttpException -> {
            this.toException()
        }

        else -> this
    }
}

fun HttpException.toException(): Throwable {
    return when (this.code()) {

        in 500 until 600 -> {
            ServerError()
        }

        else -> {
            this
        }
    }
}

const val STATUS_CODE_NO_AUTHORIZATION = 401
const val STATUS_CODE_FORBIDDEN = 403


const val ANIPEN_AUTH_ERROR = "ANIPEN_AUTH_ERROR"
