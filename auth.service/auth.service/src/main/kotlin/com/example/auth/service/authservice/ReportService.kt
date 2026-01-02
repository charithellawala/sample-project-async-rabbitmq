package com.example.auth.service.authservice

import com.example.auth.service.model.ReportEntity
import com.example.auth.service.reporting.events.AuthorizationCompletedEvent


interface ReportService {
    fun createReport(report: ReportEntity) : ReportEntity
    fun getAllReports() : List<ReportEntity>
    fun generateReport(event: AuthorizationCompletedEvent)
}