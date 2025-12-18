package com.example.auth.authservice

import com.example.auth.reporting.events.AuthorizationCompletedEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async

@Configuration
class ReportingEventListener (
    private val reportService: ReportService
) {

    @Async
    @EventListener
    fun onAuthorizationCompleted(event: AuthorizationCompletedEvent) {
        reportService.generateReport(event)
    }
}