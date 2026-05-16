package com.gramavasathi.utils

import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun nightsBetween(checkIn: String, checkOut: String): Long {
    return try {
        ChronoUnit.DAYS.between(LocalDate.parse(checkIn), LocalDate.parse(checkOut)).coerceAtLeast(0)
    } catch (_: Exception) {
        0
    }
}
