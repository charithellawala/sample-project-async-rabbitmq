package com.example.auth.controller

    import com.example.auth.authservice.ReportService
    import com.example.auth.model.ReportEntity
    import org.springframework.http.HttpStatus
    import org.springframework.http.ResponseEntity
    import org.springframework.web.bind.annotation.GetMapping
    import org.springframework.web.bind.annotation.PostMapping
    import org.springframework.web.bind.annotation.RequestBody
    import org.springframework.web.bind.annotation.RequestMapping
    import org.springframework.web.bind.annotation.RestController

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