package com.example.auth.repository

import com.example.auth.model.ReportEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SpringDataReportRepository : JpaRepository<ReportEntity, UUID> {
}