package com.example.auth.reporting.ports

import com.example.auth.model.Report
import com.example.auth.model.ReportEntity
import com.example.auth.repository.SpringDataReportRepository
import org.springframework.stereotype.Component

@Component
class ReportRepositoryAdapter (
    private val jpa: SpringDataReportRepository
) : ReportRepositoryPort {

    override fun save(report: Report) {
        jpa.save(ReportEntity.fromDomain(report))
    }

    override fun findAll(): List<Report> =
        jpa.findAll().map { it.toDomain() }
}