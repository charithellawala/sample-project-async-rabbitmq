package com.example.auth.service.reporting.ports

import com.example.auth.service.model.Report

interface ReportRepositoryPort {
    fun save(report: Report)
    fun findAll(): List<Report>
}