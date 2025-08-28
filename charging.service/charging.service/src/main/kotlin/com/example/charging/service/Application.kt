package com.example.charging.service

import com.example.charging.service.rabbitmq.RabbitConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
//@Import(RabbitConfig::class)
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
