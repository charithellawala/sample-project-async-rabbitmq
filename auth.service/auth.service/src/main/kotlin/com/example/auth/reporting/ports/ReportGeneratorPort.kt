package com.example.auth.reporting.ports

import com.example.auth.model.Report

interface ReportGeneratorPort {
    fun generate(report: Report)
}