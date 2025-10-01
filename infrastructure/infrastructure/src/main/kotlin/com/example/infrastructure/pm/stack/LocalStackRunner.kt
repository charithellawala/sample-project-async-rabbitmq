package com.example.infrastructure.pm.stack

import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import software.amazon.awscdk.App
import software.amazon.awscdk.AppProps
import software.amazon.awscdk.BootstraplessSynthesizer
import software.amazon.awscdk.StackProps

@Component
class LocalStackRunner {
    @PostConstruct
    fun runStack() {
        val app = App(AppProps.builder().outdir("./cdk.out").build())
        val props = StackProps.builder()
            .synthesizer(BootstraplessSynthesizer())
            .build()
        LocalStack(app, "LocalStack", props)
        app.synth()
        println("app synthesising in progress")
    }
}