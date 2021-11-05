package com.itmo.moonphase

import java.time.Instant
import java.time.ZoneId

fun MoonPhaseResponse.toEntity() = MoonPhaseInfo(
    Instant.ofEpochSecond(targetDate).atZone(ZoneId.systemDefault()),
    moonPhaseNameToEnum(phase),
    age,
    illumination
)

private fun moonPhaseNameToEnum(moonPhaseName: String) = when(moonPhaseName) {
    Consts.Farmsense.NEW_MOON -> MoonPhaseEnum.NEW_MOON
    Consts.Farmsense.WAXING_CRESCENT -> MoonPhaseEnum.WAXING_CRESCENT
    Consts.Farmsense.FIRST_QUARTER -> MoonPhaseEnum.FIRST_QUARTER
    Consts.Farmsense.WAXING_GIBBOUS -> MoonPhaseEnum.WAXING_GIBBOUS
    Consts.Farmsense.FULL_MOON -> MoonPhaseEnum.FULL_MOON
    Consts.Farmsense.WANING_GIBBOUS -> MoonPhaseEnum.WANING_GIBBOUS
    Consts.Farmsense.LAST_QUARTER -> MoonPhaseEnum.LAST_QUARTER
    Consts.Farmsense.WANING_CRESCENT -> MoonPhaseEnum.WANING_CRESCENT
    else -> throw EnumConstantNotPresentException(MoonPhaseEnum::class.java, "moonPhaseName")
}
