package com.itmo.moonphase.api.farmsense

import com.itmo.moonphase.MoonPhaseEnum
import com.itmo.moonphase.MoonPhaseInfo
import java.text.ParseException
import java.time.Instant
import java.time.ZoneId

fun MoonPhaseResponse.toEntity() = MoonPhaseInfo(
    Instant.ofEpochSecond(targetDate).atZone(ZoneId.systemDefault()),
    moonPhaseNameToEnum(phase),
    age,
    illumination
)

private fun moonPhaseNameToEnum(moonPhaseName: String) = when(moonPhaseName) {
    Consts.NEW_MOON -> MoonPhaseEnum.NEW_MOON
    Consts.WAXING_CRESCENT -> MoonPhaseEnum.WAXING_CRESCENT
    Consts.FIRST_QUARTER -> MoonPhaseEnum.FIRST_QUARTER
    Consts.WAXING_GIBBOUS -> MoonPhaseEnum.WAXING_GIBBOUS
    Consts.FULL_MOON -> MoonPhaseEnum.FULL_MOON
    Consts.WANING_GIBBOUS -> MoonPhaseEnum.WANING_GIBBOUS
    Consts.LAST_QUARTER -> MoonPhaseEnum.LAST_QUARTER
    Consts.WANING_CRESCENT -> MoonPhaseEnum.WANING_CRESCENT
    Consts.DARK_MOON -> MoonPhaseEnum.NEW_MOON
    else -> throw IllegalArgumentException("Cannot parse moonPhaseName=$moonPhaseName to ${MoonPhaseEnum::class.java.simpleName}")
}
