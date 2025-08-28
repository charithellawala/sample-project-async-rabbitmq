package com.example.charging.service.repository

import com.example.charging.service.model.ChargingRequestEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ChargingRequestRepository : JpaRepository <ChargingRequestEntity, UUID> {
}