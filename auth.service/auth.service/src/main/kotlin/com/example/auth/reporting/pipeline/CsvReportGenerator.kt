package com.example.auth.reporting.pipeline

import com.example.auth.model.Report
import com.example.auth.reporting.ports.ReportGeneratorPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

@Component
class CsvReportGenerator ( @Value("\${app.reporting.dir}") private val outputDir: String
) : ReportGeneratorPort {

    override fun generate(report: Report) {
        val file = File("$outputDir/${report.id}.csv")
        file.parentFile.mkdirs()

        file.printWriter().use { out ->
            out.println("station_id,driver_token,decision,timestamp")
            out.println("${report.stationId},${report.driverToken},${report.decision},${report.createdAt}")
        }
    }

}