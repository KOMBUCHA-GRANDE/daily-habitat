package com.anipen.anipenauth.utils

import java.util.Calendar

object DateUtils {

    fun getCurrentUnixTime(): Long {
        return Calendar.getInstance().timeInMillis / 1000
    }
}