package com.example.auth.service.reporting.events

import java.time.Instant
import java.util.UUID

data class AuthorizationCompletedEvent(val stationId: UUID,
                                       val driverToken: String,
                                       val decision: String,
                                       val timestamp: Instant = Instant.now())
