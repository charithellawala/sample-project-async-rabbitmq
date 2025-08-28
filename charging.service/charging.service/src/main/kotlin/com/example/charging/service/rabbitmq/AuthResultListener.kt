package com.example.charging.service.rabbitmq

import com.example.charging.service.dto.AuthResultDto
import com.example.charging.service.repository.ChargingRequestRepository
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.UUID

@Component
class AuthResultListener(
    private val chargingRepo: ChargingRequestRepository,
    private val webClient: WebClient
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = [RabbitConfig.RESULT_QUEUE])
    fun handle(result: AuthResultDto) {

        logger.info("Authorization result received successfully: ${result}")

        val id = UUID.fromString(result.requestId)
        val eOpt = chargingRepo.findById(id)
        if (eOpt.isPresent) {
            val e = eOpt.get()
            e.status = when (result.status.lowercase()) {
                "allowed" -> "ALLOWED"
                "not_allowed" -> "NOT_ALLOWED"
                "unknown" -> "UNKNOWN"
                else -> "INVALID"
            }
            e.rawResponse = result.toString()
            e.updatedAt = Instant.now()
            logger.info("Updating New Results in db.")
            chargingRepo.save(e)

            // attempt callback (non-blocking)
            val cbUrl = e.callbackUrl
            val payload = mapOf(
                "request_id" to result.requestId,
                "station_id" to result.stationId,
                "driver_token" to result.driverToken,
                "status" to result.status,
                "reason" to result.reason
            )

            logger.info("Sending results to callback: ${payload}")
            webClient.post()
                .uri(cbUrl)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String::class.java)
                .onErrorResume { Mono.empty() }
                .subscribe()
            logger.info("Results is send to callback")
        }
    }
}