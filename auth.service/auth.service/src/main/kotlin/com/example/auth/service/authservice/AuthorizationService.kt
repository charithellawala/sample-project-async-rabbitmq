package com.example.auth.service.authservice

import com.example.auth.service.common.AuthorizationResult
import com.example.auth.service.config.RabbitConfig
import com.example.auth.service.dto.AuthRequestDto
import com.example.auth.service.dto.AuthResultDto
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class AuthorizationService (
    private val rabbitTemplate: RabbitTemplate
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = ["auth.requests"])
    fun handle(req: AuthRequestDto) {

        logger.info("Auth-service received request: $req")
        logger.info("Checking authorization for station ${req.stationId} and driver ${req.driverToken}")

        val result = if (req.driverToken.startsWith("A")) {
            AuthResultDto(
                requestId = req.requestId,
                stationId = req.stationId,
                driverToken = req.driverToken,
                status = AuthorizationResult.ALLOWED.toString(),
                callbackUrl = req.callbackUrl
            )
        } else {
            AuthResultDto(
                requestId = req.requestId,
                stationId = req.stationId,
                driverToken = req.driverToken,
                status = AuthorizationResult.NOT_ALLOWED.toString(),
                reason = "Invalid driver token",
                callbackUrl = req.callbackUrl
            )
        }

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.RESULT_ROUTING, result)
        logger.info("Auth-service published result: $result")

    }
}