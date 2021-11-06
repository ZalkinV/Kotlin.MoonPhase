package com.itmo.moonphase

import android.util.Log
import java.time.LocalDateTime
import java.time.ZoneId

fun Exception.log(tag: String) =
    Log.e(tag,"Message: ${message}\nStacktrace: ${stackTraceToString()}")

// TODO: move it to separate DateTimeProvider class
fun getDateTimeNow() = LocalDateTime.now().atZone(ZoneId.systemDefault())
