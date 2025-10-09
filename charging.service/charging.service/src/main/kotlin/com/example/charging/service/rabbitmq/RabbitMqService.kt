package com.example.charging.service.rabbitmq

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class RabbitMqService (private val rabbitTemplate: RabbitTemplate) {

    fun sendAuthRequestAsTemplate(exchange: String, routingKey: String, message: Any) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message)
    }
}