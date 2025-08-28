package com.example.charging.service.dto

import java.time.Instant

data class AuthRequestDto(
    val requestId: String,
    val stationId: String,
    val driverToken: String,
    val callbackUrl: String,
    val timestamp: String = Instant.now().toString()
)
