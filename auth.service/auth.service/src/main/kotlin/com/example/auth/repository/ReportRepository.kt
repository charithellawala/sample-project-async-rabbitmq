package com.example.auth.repository

import com.example.auth.model.Report
import com.example.auth.model.ReportEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ReportRepository: JpaRepository<ReportEntity, UUID> {
    fun save(report: ReportEntity): ReportEntity
    override fun findAll(): List<ReportEntity>
}