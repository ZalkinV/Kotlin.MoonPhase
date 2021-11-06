package com.itmo.moonphase

class MoonPhaseResource private constructor(val imageId: Int, val nameId: Int) {

    companion object {
        fun getMoonPhaseResource(moonPhase: MoonPhaseEnum) = when(moonPhase) {
            MoonPhaseEnum.NEW_MOON -> MoonPhaseResource(R.drawable.emoji_new_moon, R.string.moonPhase_newMoon)
            MoonPhaseEnum.WAXING_CRESCENT -> MoonPhaseResource(R.drawable.emoji_waxing_crescent, R.string.moonPhase_waxingCrescent)
            MoonPhaseEnum.FIRST_QUARTER -> MoonPhaseResource(R.drawable.emoji_first_quarter, R.string.moonPhase_firstQuarter)
            MoonPhaseEnum.WAXING_GIBBOUS -> MoonPhaseResource(R.drawable.emoji_waxing_gibbous, R.string.moonPhase_waxingGibbous)
            MoonPhaseEnum.FULL_MOON -> MoonPhaseResource(R.drawable.emoji_full_moon, R.string.moonPhase_fullMoon)
            MoonPhaseEnum.WANING_GIBBOUS -> MoonPhaseResource(R.drawable.emoji_wanning_gibbous, R.string.moonPhase_waningGibbous)
            MoonPhaseEnum.LAST_QUARTER -> MoonPhaseResource(R.drawable.emoji_last_quarter, R.string.moonPhase_lastQuarter)
            MoonPhaseEnum.WANING_CRESCENT -> MoonPhaseResource(R.drawable.emoji_wanning_crescent, R.string.moonPhase_waningCrescent)
        }
    }

}
