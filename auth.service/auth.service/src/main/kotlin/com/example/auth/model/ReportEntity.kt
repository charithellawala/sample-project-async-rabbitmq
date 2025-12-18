package com.example.auth.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

@Entity
@Table(name = "authorization_reports")
data class ReportEntity(
    @Id val id: UUID, val stationId: UUID, val driverToken: String, val decision: String, val createdAt: Instant
) {
    fun toDomain() = Report(id, stationId, driverToken, decision, createdAt)

    companion object {
        fun fromDomain(r: Report) = ReportEntity(
            id = r.id,
            stationId = r.stationId,
            driverToken = r.driverToken,
            decision = r.decision,
            createdAt = r.createdAt
        )
    }
}