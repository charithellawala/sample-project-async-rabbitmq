package com.example.auth.service.controller

    import com.example.auth.service.authservice.ReportService
    import com.example.auth.service.model.ReportEntity
    import org.springframework.http.HttpStatus
    import org.springframework.http.ResponseEntity
    import org.springframework.web.bind.annotation.*

@RestController
    @RequestMapping("/reports")
    class ReportController(private val reportService: ReportService) {

        @PostMapping
        fun createReport(@RequestBody report: ReportEntity): ResponseEntity<ReportEntity> {
            val created = reportService.createReport(report)
            return ResponseEntity.status(HttpStatus.CREATED).body(created)
        }

        @GetMapping
        fun getAllReports(): ResponseEntity<List<ReportEntity>> {
            val reports = reportService.getAllReports()
            return ResponseEntity.ok(reports)
        }
    }