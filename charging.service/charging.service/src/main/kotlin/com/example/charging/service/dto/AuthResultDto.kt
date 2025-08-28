package com.example.charging.service.dto

import java.time.Instant

data class AuthResultDto(
    val requestId: String,
    val stationId: String,
    val driverToken: String,
    val status: String,
    val reason: String? = null,
    val callbackUrl: String? = null,
    val timestamp: String = Instant.now().toString()
)
