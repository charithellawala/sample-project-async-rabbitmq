package com.example.charging.service.chargingservice

import com.example.charging.service.dto.ChargingRequestDto
import com.example.charging.service.model.StartApiResponse

interface ChargingService {

    companion object {
        const val AUTH_EXCHANGE = "auth.exchange"
        const val AUTH_REQUEST_ROUTING_KEY = "auth.request"
    }
    fun processChargingRequest(request: ChargingRequestDto) : StartApiResponse
}