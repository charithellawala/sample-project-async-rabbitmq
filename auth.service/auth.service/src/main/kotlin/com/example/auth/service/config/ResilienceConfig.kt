package com.example.auth.service.config

import io.github.resilience4j.timelimiter.TimeLimiter
import io.github.resilience4j.timelimiter.TimeLimiterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ResilienceConfig {
    @Bean
    fun driverValidationTimeLimiter(registry: TimeLimiterRegistry): TimeLimiter? {
        // "driverValidation" should match a config in your application.yml
        return registry.timeLimiter("driverValidation")
    }
}