package com.example.auth.service.serviceadapter

import com.example.auth.service.common.AuthorizationResult
import com.example.auth.service.reporting.ports.DriverValidationPort
import com.example.auth.service.repository.DriverAclRepository
import com.github.benmanes.caffeine.cache.Cache
import io.github.resilience4j.timelimiter.TimeLimiter
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

@Component
class CachedDriverValidationAdapter(
    private val localCache: Cache<String, AuthorizationResult>,
    private val stringRedisTemplate: StringRedisTemplate,
    private val repository: DriverAclRepository,
    private val timeLimiter: TimeLimiter
) : DriverValidationPort {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val executor = Executors.newCachedThreadPool()

    override fun validate(driverToken: String): AuthorizationResult {

        // 1️⃣ L1 Cache (fastest)
        localCache.getIfPresent(driverToken)?.let {
            return it
        }

        // 2️⃣ L2 Cache (Redis)
        stringRedisTemplate.opsForValue().get(driverToken)?.let { cached ->
            val status = AuthorizationResult.valueOf(cached.toString())
            localCache.put(driverToken, status)
            logger.debug("Driver Status after localcach check: $status")
            return status
        }

        // 3️⃣ DB lookup with timeout protection
        return try {
            val future = CompletableFuture.supplyAsync({
                repository.findById(driverToken)
                    .map { it.status }
                    .orElse(AuthorizationResult.UNKNOWN)
            }, executor)

            val status = timeLimiter.executeFutureSupplier {
                CompletableFuture.supplyAsync { future.get() }
            }
            logger.debug("Driver Status after Db check: $status")

            // ✅ NEGATIVE CACHE TTL HANDLING
            val ttl = if (status == AuthorizationResult.UNKNOWN)
                Duration.ofSeconds(30)
            else
                Duration.ofMinutes(15)

            // cache results
            localCache.put(driverToken, status)
            logger.info("Driver Validation completed..!!")
            stringRedisTemplate.opsForValue()
                .set(driverToken, status.name, ttl)
            status

        } catch (ex: Exception) {
            // Fail-safe
            AuthorizationResult.UNKNOWN
        }
    }
}