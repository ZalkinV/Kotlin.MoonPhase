package com.itmo.moonphase

import android.util.Log

fun Exception.log(tag: String) =
    Log.e(tag,"Message: ${message}\nStacktrace: ${stackTraceToString()}")
