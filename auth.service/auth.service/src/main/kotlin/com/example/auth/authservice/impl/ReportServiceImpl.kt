package com.example.auth.authservice.impl

import com.example.auth.authservice.ReportService
import com.example.auth.model.Report
import com.example.auth.model.ReportEntity
import com.example.auth.reporting.events.AuthorizationCompletedEvent
import com.example.auth.reporting.ports.ReportGeneratorPort
import com.example.auth.repository.ReportRepository
import org.springframework.stereotype.Service

@Service
class ReportServiceImpl(val reportRepo: ReportRepository,
                        private val generator: ReportGeneratorPort
) : ReportService {

    override fun createReport(report: ReportEntity): ReportEntity {
        return reportRepo.save(report)
    }

    override fun generateReport(event: AuthorizationCompletedEvent) {
        val report = Report(
            stationId = event.stationId,
            driverToken = event.driverToken,
            decision = event.decision
        )

        // Persist in DB
        reportRepo.save(ReportEntity.fromDomain(report))

        // Generate CSV or any other output
        generator.generate(report)
    }

    override fun getAllReports(): List<ReportEntity> {
        return reportRepo.findAll()
    }


}