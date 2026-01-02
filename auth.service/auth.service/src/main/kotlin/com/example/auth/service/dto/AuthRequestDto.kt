package com.example.auth.service.dto

data class AuthRequestDto(
    val requestId: String,
    val stationId: String,
    val driverToken: String,
    val callbackUrl: String,
    val timestamp: String,
)
