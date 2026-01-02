package com.example.auth.service.reporting.pipeline

import com.example.auth.service.config.ReportingProperties
import com.example.auth.service.model.Report
import com.example.auth.service.reporting.ports.ReportGeneratorPort
import org.springframework.stereotype.Component
import java.io.File

@Component
class CsvReportGenerator(
    private val properties: ReportingProperties,
) : ReportGeneratorPort {

    private val outputDir = properties.dir
    override fun generate(report: Report) {
        val file = File("$outputDir/${report.id}.csv")
        file.parentFile.mkdirs()

        file.printWriter().use { out ->
            out.println("station_id,driver_token,decision,timestamp")
            out.println("${report.stationId},${report.driverToken},${report.decision},${report.createdAt}")
        }
    }

}