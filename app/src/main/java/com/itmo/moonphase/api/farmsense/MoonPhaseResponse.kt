package com.itmo.moonphase.api.farmsense

data class MoonPhaseResponse(
    val targetDate: Long = 0L,
    val age: Double = 0.0,
    val phase: String = "",
    val illumination: Double = 0.0,
)
