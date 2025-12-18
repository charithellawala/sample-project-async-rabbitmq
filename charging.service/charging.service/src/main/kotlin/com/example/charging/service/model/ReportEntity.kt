package com.example.charging.service.model

import java.time.LocalDateTime

data class ReportEntity(
    val id: String,
    val type: ReportType,
    var status: ReportStatus,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class ReportType { SESSIONS, CHARGERS }
enum class ReportStatus { IN_PROGRESS, COMPLETED, FAILED }
