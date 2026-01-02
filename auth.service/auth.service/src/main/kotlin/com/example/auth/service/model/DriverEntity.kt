package com.example.auth.service.model

import com.example.auth.service.common.AuthorizationResult
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "user_driver")
data class DriverEntity(
    @Id @Column(length = 80) val driverToken: String,

    @Enumerated(EnumType.STRING) val status: AuthorizationResult,

    val updatedAt: Instant = Instant.now()

)
