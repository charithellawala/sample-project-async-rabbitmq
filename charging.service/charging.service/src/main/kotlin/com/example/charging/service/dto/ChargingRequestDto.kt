package com.example.charging.service.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.util.UUID

data class ChargingRequestDto(

    @field:NotNull
    val stationId: UUID,

    @field:NotBlank
    @field:Size(min = 20, max = 80)
    @field:Pattern(regexp = "^[a-zA-Z0-9\\-\\._~]+$")
    val driverToken: String,

    @field:NotBlank
    @field:Pattern(regexp = "https?://.+")
    val callbackUrl: String
)
