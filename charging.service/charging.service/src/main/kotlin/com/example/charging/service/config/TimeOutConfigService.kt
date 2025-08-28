package com.example.charging.service.config

import com.example.charging.service.repository.ChargingRequestRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.UUID

@Component
class TimeOutConfigService (
    private val repository: ChargingRequestRepository,
    private val scheduler: TaskScheduler,
    private val webClient: WebClient
        ) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun scheduleTimeout(requestId: UUID) {
        scheduler.schedule({
            val opt = repository.findById(requestId)
            if (opt.isPresent) {
                val payload = opt.get()
                if (payload.status == "PENDING") {
                    payload.status = "UNKNOWN"
                    payload.updatedAt = Instant.now()
                    payload.rawResponse = "Timeout: no response from auth-service"
                    repository.save(payload)
                    logger.info("⚠️ Request ${requestId} timed out, marked as UNKNOWN")

                    webClient.post()
                .uri(payload.callbackUrl)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String::class.java)
                .onErrorResume { Mono.empty() }
                .subscribe()
            logger.info("⚠️ Request ${payload.id} send to callback")
                }
            }
        }, Instant.now().plusSeconds(5))
    }

}