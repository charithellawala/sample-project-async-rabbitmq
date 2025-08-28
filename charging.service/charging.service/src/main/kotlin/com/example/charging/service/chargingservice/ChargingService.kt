package com.example.charging.service.chargingservice

import com.example.charging.service.dto.ChargingRequestDto
import com.example.charging.service.model.StartApiResponse

interface ChargingService {
    fun processChargingRequest(request: ChargingRequestDto) : StartApiResponse
}