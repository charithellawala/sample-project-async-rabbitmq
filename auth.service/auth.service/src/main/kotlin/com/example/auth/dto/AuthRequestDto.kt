package com.example.auth.dto

data class AuthRequestDto(
    val requestId: String,
    val stationId: String,
    val driverToken: String,
    val callbackUrl: String,
    val timestamp: String,
)
