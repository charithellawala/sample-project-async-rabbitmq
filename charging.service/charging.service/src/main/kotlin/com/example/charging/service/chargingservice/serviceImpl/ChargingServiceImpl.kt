package com.example.charging.service.chargingservice.serviceImpl

import com.example.charging.service.chargingservice.ChargingService
import com.example.charging.service.chargingservice.ChargingService.Companion.AUTH_EXCHANGE
import com.example.charging.service.chargingservice.ChargingService.Companion.AUTH_REQUEST_ROUTING_KEY
import com.example.charging.service.config.TimeOutConfigService
import com.example.charging.service.dto.AuthRequestDto
import com.example.charging.service.dto.ChargingRequestDto
import com.example.charging.service.model.ChargingRequestEntity
import com.example.charging.service.model.StartApiResponse
import com.example.charging.service.rabbitmq.RabbitMqService
import com.example.charging.service.repository.ChargingRequestRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ChargingServiceImpl(
    private val chargingRequestRepository: ChargingRequestRepository,
    private val rabbitMqService: RabbitMqService,
    private val timeoutHandler: TimeOutConfigService
) : ChargingService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun processChargingRequest(request: ChargingRequestDto): StartApiResponse {

        logger.info("Processing charging request for station ${request.stationId}. And Saving..")
        try {
            val requestEntity = chargingRequestRepository.save(
                ChargingRequestEntity(
                    stationId = request.stationId,
                    driverToken = request.driverToken,
                    callbackUrl = request.callbackUrl
                )
            )
            logger.info("Charging Request Saved Successfully In Database ${requestEntity.id}")

            val message = AuthRequestDto(
                requestId = requestEntity.id.toString(),
                stationId = requestEntity.stationId.toString(),
                driverToken = requestEntity.driverToken,
                callbackUrl = requestEntity.callbackUrl,
                timestamp = Instant.now().toString()
            )
            logger.info("Sending Charging Request to Rabbitmq template")
            rabbitMqService.sendAuthRequestAsTemplate(AUTH_EXCHANGE, AUTH_REQUEST_ROUTING_KEY, message)

            // Delegate timeout scheduling
            timeoutHandler.scheduleTimeout(requestEntity.id!!)

            return StartApiResponse(
                status = "accepted",
                message = "Request is being processed. The result will send to callback Url"
            )
        } catch (e: Exception) {
            logger.error("Failed to send callback to ${request.callbackUrl}", e)
            throw e
        }
    }
}