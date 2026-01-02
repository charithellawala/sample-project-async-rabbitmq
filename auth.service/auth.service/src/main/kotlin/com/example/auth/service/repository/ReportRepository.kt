package com.example.auth.service.repository

import com.example.auth.service.model.ReportEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReportRepository: JpaRepository<ReportEntity, UUID> {
    fun save(report: ReportEntity): ReportEntity
    override fun findAll(): List<ReportEntity>
}