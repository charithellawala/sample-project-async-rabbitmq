package com.example.auth.service.authservice.impl

import com.example.auth.service.authservice.ReportService
import com.example.auth.service.model.Report
import com.example.auth.service.model.ReportEntity
import com.example.auth.service.reporting.events.AuthorizationCompletedEvent
import com.example.auth.service.reporting.ports.ReportGeneratorPort
import com.example.auth.service.repository.ReportRepository
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
        reportRepo.save(ReportEntity.fromDomain(report))
        // Generate CSV or any other output
        generator.generate(report)
    }

    override fun getAllReports(): List<ReportEntity> {
        return reportRepo.findAll()
    }


}