package com.itmo.moonphase

import java.time.ZonedDateTime

class MoonPhaseInfo(
    val dateTime: ZonedDateTime,
    val phase: MoonPhaseEnum,
    val age: Double,
    val illumination: Double,
)
