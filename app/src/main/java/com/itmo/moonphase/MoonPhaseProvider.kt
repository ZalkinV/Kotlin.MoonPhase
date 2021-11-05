package com.itmo.moonphase

import java.time.ZonedDateTime

interface MoonPhaseProvider {

    suspend fun getMoonPhases(startDate: ZonedDateTime, endDate: ZonedDateTime = startDate) : List<MoonPhaseInfo>

}