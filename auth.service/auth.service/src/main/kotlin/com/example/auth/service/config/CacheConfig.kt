package com.example.auth.service.config

import com.example.auth.service.common.AuthorizationResult
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class CacheConfig {

    @Bean
    fun driverLocalCache() = Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).maximumSize(1_000_000)
        .build<String, AuthorizationResult>()


}