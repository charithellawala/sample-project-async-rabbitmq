package com.example.auth.model

import java.time.Instant
import java.util.UUID

data class Report(val id: UUID = UUID.randomUUID(),
                  val stationId: UUID,
                  val driverToken: String,
                  val decision: String,
                  val createdAt: Instant = Instant.now())
