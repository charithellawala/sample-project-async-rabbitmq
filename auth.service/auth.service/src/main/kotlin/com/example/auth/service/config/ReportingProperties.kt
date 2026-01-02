package com.example.auth.service.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.reporting")
data class ReportingProperties(
    val dir: String
)