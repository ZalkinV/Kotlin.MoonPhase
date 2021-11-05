package com.itmo.moonphase

import java.time.Instant
import java.time.ZoneId

fun MoonPhaseResponse.toEntity() = MoonPhaseInfo(
    Instant.ofEpochSecond(targetDate).atZone(ZoneId.systemDefault()),
    phase,
    age,
    illumination
)
