package com.example.auth.service

import com.example.auth.service.config.ReportingProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(ReportingProperties::class)
class Application

fun main(args: Array<String>) {
	runApplication<com.example.auth.service.Application>(*args)
}
