package com.example.auth.service.reporting.ports

import com.example.auth.service.model.Report

interface ReportGeneratorPort {
    fun generate(report: Report)
}