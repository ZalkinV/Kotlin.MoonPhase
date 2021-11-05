package com.itmo.moonphase

import java.time.ZonedDateTime

class MoonPhaseInfo(
    val dateTime: ZonedDateTime,
    val imageResourceId: Int,
    val name: String,
    val age: Double,
    val illumination: Double,
)
