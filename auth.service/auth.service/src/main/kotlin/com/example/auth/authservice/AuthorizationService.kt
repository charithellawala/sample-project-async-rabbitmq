package com.example.auth.authservice

import com.example.auth.common.AuthorizationResult
import com.example.auth.config.RabbitConfig
import com.example.auth.config.RabbitMqService
import com.example.auth.dto.AuthRequestDto
import com.example.auth.dto.AuthResultDto
import com.example.auth.reporting.events.AuthorizationCompletedEvent
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthorizationService(
    private val rabbitMqService: RabbitMqService,
    private val eventPublisher: ApplicationEventPublisher
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = ["auth.requests"])
    fun handle(req: AuthRequestDto) {

        logger.info("Auth-service received request: $req")
        logger.info("Checking authorization for station ${req.stationId} and driver ${req.driverToken}")

        val result = when {
            req.driverToken.startsWith("A") -> AuthResultDto(
                requestId = req.requestId,
                stationId = req.stationId,
                driverToken = req.driverToken,
                status = AuthorizationResult.ALLOWED.toString(),
                callbackUrl = req.callbackUrl
            )

            else -> AuthResultDto(
                requestId = req.requestId,
                stationId = req.stationId,
                driverToken = req.driverToken,
                status = AuthorizationResult.NOT_ALLOWED.toString(),
                reason = "Invalid driver token",
                callbackUrl = req.callbackUrl
            )
        }


        rabbitMqService.sendAuthRequestAsTemplate(RabbitConfig.EXCHANGE, RabbitConfig.RESULT_ROUTING, result)
        logger.info("Auth-service publishing event for reports")
        eventPublisher.publishEvent(
            AuthorizationCompletedEvent(
                stationId = UUID.fromString(result.stationId),
                driverToken = result.driverToken,
                decision = result.status
            )
        )
        logger.info("Auth-service published result: $result")

    }
}