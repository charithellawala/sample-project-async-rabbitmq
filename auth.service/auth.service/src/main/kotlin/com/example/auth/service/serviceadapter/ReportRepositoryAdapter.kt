package com.example.auth.service.serviceadapter

import com.example.auth.service.model.Report
import com.example.auth.service.model.ReportEntity
import com.example.auth.service.reporting.ports.ReportRepositoryPort
import com.example.auth.service.repository.SpringDataReportRepository
import org.springframework.stereotype.Component

@Component
class ReportRepositoryAdapter (
    private val jpa: SpringDataReportRepository
) : ReportRepositoryPort {

    override fun save(report: Report) {
        jpa.save(ReportEntity.Companion.fromDomain(report))
    }

    override fun findAll(): List<Report> =
        jpa.findAll().map { it.toDomain() }
}