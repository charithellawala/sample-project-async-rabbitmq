package com.example.auth.service.repository

import com.example.auth.service.model.ReportEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SpringDataReportRepository : JpaRepository<ReportEntity, UUID> {
}