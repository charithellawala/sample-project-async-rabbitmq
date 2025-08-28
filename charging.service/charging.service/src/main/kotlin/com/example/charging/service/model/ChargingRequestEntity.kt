package com.example.charging.service.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "charging_requests")
data class ChargingRequestEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    val stationId: UUID,

    @Column(nullable = false)
    val driverToken: String,

    @Column(nullable = false)
    val callbackUrl: String,

    @Column(nullable = false)
    var status: String = "PENDING",

    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    var updatedAt: Instant? = null,

    @Lob
    var rawResponse: String? = null


)
