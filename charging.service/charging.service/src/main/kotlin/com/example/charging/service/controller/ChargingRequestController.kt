package com.example.charging.service.controller

import com.example.charging.service.chargingservice.ChargingService
import com.example.charging.service.dto.ChargingRequestDto
import com.example.charging.service.model.StartApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@RequestMapping("/api/v1/charging")
class ChargingRequestController(
    private val chargingRequestService: ChargingService
) {

    @PostMapping("/start-charging")
    fun startCharging(@Validated @RequestBody chargingRequest: ChargingRequestDto): ResponseEntity<StartApiResponse> {

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(chargingRequestService.processChargingRequest(chargingRequest))
    }


}