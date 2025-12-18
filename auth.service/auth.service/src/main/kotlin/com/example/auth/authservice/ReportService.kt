package com.example.auth.authservice

import com.example.auth.model.ReportEntity
import com.example.auth.reporting.events.AuthorizationCompletedEvent

interface ReportService {

    fun createReport(report: ReportEntity) : ReportEntity
    fun getAllReports() : List<ReportEntity>
    fun generateReport(event: AuthorizationCompletedEvent)
}