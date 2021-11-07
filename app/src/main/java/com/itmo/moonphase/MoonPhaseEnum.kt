package com.itmo.moonphase

enum class MoonPhaseEnum(val imageId: Int, val nameId: Int) {
    NEW_MOON(R.drawable.emoji_new_moon, R.string.moonPhase_newMoon),
    WAXING_CRESCENT(R.drawable.emoji_waxing_crescent, R.string.moonPhase_waxingCrescent),
    FIRST_QUARTER(R.drawable.emoji_first_quarter, R.string.moonPhase_firstQuarter),
    WAXING_GIBBOUS(R.drawable.emoji_waxing_gibbous, R.string.moonPhase_waxingGibbous),
    FULL_MOON(R.drawable.emoji_full_moon, R.string.moonPhase_fullMoon),
    WANING_GIBBOUS(R.drawable.emoji_wanning_gibbous, R.string.moonPhase_waningGibbous),
    LAST_QUARTER(R.drawable.emoji_last_quarter, R.string.moonPhase_lastQuarter),
    WANING_CRESCENT(R.drawable.emoji_wanning_crescent, R.string.moonPhase_waningCrescent),
}
