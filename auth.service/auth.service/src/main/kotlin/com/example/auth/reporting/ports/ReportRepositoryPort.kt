package com.example.auth.reporting.ports

import com.example.auth.model.Report

interface ReportRepositoryPort {
    fun save(report: Report)
    fun findAll(): List<Report>
}