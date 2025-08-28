package com.example.charging.service.rabbitmq

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitConfig {

    companion object {
        const val EXCHANGE = "auth.exchange"
        const val REQUEST_QUEUE = "auth.requests"
        const val RESULT_QUEUE = "auth.results"
        const val REQUEST_ROUTING = "auth.request"
        const val RESULT_ROUTING = "auth.result"
    }

    @Bean
    fun messageConverter(): MessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = messageConverter()
        return rabbitTemplate
    }

    @Bean
    fun exchange() = DirectExchange(EXCHANGE)

    @Bean(name = ["requestQueue"])
    fun requestQueue() = Queue(REQUEST_QUEUE)

    @Bean(name = ["resultQueue"])
    fun resultQueue() = Queue(RESULT_QUEUE)

    @Bean
    fun bindRequest(@Qualifier("requestQueue") q: Queue, ex: DirectExchange): Binding =
        BindingBuilder.bind(q).to(ex).with(REQUEST_ROUTING)

    @Bean
    fun bindResult(@Qualifier("resultQueue") q: Queue, ex: DirectExchange): Binding =
        BindingBuilder.bind(q).to(ex).with(RESULT_ROUTING)

}

//const val REQUEST_QUEUE = "auth.requests"
//const val RESULT_QUEUE = "auth.results"
//const val EXCHANGE_NAME = "auth.exchange"
//const val REQUEST_ROUTING = "auth.request"
//const val RESULT_ROUTING = "auth.result"
//
//@Configuration
//class RabbitConfig {
//    @Bean
//    fun exchange() = DirectExchange(EXCHANGE_NAME)
//
//    @Bean(name = ["requestQueue"])
//    fun requestQueue() = Queue(REQUEST_QUEUE)
//
//    @Bean(name = ["resultQueue"])
//    fun resultQueue() = Queue(RESULT_QUEUE)
//
//    @Bean
//    fun bindRequest(@Qualifier("requestQueue") q: Queue, ex: DirectExchange): Binding =
//        BindingBuilder.bind(q).to(ex).with(REQUEST_ROUTING)
//
//    @Bean
//    fun bindResult(@Qualifier("resultQueue") q: Queue, ex: DirectExchange): Binding =
//        BindingBuilder.bind(q).to(ex).with(RESULT_ROUTING)
//}
