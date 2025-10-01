package com.example.infrastructure.pm.stack

import software.amazon.awscdk.*
import software.amazon.awscdk.services.amazonmq.CfnBroker
import software.amazon.awscdk.services.ec2.InstanceClass
import software.amazon.awscdk.services.ec2.InstanceSize
import software.amazon.awscdk.services.ec2.Vpc.Builder.create
import software.amazon.awscdk.services.ecs.AwsLogDriverProps
import software.amazon.awscdk.services.ecs.CloudMapNamespaceOptions
import software.amazon.awscdk.services.ecs.Cluster
import software.amazon.awscdk.services.ecs.ContainerDefinitionOptions
import software.amazon.awscdk.services.ecs.ContainerImage
import software.amazon.awscdk.services.ecs.FargateService
import software.amazon.awscdk.services.ecs.FargateTaskDefinition
import software.amazon.awscdk.services.ecs.LogDriver
import software.amazon.awscdk.services.ecs.PortMapping
import software.amazon.awscdk.services.ecs.Protocol
import software.amazon.awscdk.services.logs.LogGroup
import software.amazon.awscdk.services.logs.RetentionDays
import software.amazon.awscdk.services.rds.*
import software.amazon.awscdk.services.route53.CfnHealthCheck
import kotlin.toString


class LocalStack(scope: App, id: String, props: StackProps) : Stack(scope, id, props) {

    val vpc = create(this,"chargepoint-vpc")
        .vpcName("LocalVpc")
        .maxAzs(2)
        .build()

    val db = DatabaseInstance.Builder.create(this, "LocalDbInstance-ChargePoint")
        .engine(DatabaseInstanceEngine.postgres(
            PostgresInstanceEngineProps.builder()
                .version(software.amazon.awscdk.services.rds.PostgresEngineVersion.VER_17_2)
                .build()))
        .vpc(vpc)
        .instanceType(software.amazon.awscdk.services.ec2.InstanceType.of(
            InstanceClass.BURSTABLE2, InstanceSize.MICRO))
        .allocatedStorage(15)
        .databaseName("charging-management") // matches docker-compose
        .credentials(Credentials.fromUsername("postgres",             CredentialsFromUsernameOptions.builder()
            .password(SecretValue.unsafePlainText("changemeinprod!"))
            .build()))
        .removalPolicy(RemovalPolicy.DESTROY)
        .build()

    val dbHealthCheck = CfnHealthCheck.Builder.create(this, "health-check-dbInstance-chargePoint")
        .healthCheckConfig(
            CfnHealthCheck.HealthCheckConfigProperty.builder()
                .type("TCP")
                //.resourcePath("/")
                //.fullyQualifiedDomainName(db.dbInstanceEndpointAddress)
                .port(Token.asNumber(db.getDbInstanceEndpointPort()))
                .ipAddress(db.dbInstanceEndpointAddress)
                .requestInterval(30)
                .failureThreshold(3)
                .build()
        )
        .build()

    val rabbitMqBroker = CfnBroker.Builder.create(this, "LocalRabbitMQBroker")
        .brokerName("local-rabbitmq")
        .engineType("RabbitMQ")
        .engineVersion("3.10.10")
        .hostInstanceType("mq.t3.micro")
        .users(listOf(
            CfnBroker.UserProperty.builder()
                .username("guest")
                .password("guest")
                .build()
        ))
        .deploymentMode("SINGLE_INSTANCE")
        .autoMinorVersionUpgrade(true)
        .publiclyAccessible(true) // <-- Required property
        .build()

    val ecsCluster = Cluster.Builder.create(this, "LocalEcsCluster-ChargePoint")
        .vpc(vpc)
        .defaultCloudMapNamespace(CloudMapNamespaceOptions.builder()
            .name("local-ecs-cluster-chargepoint").build())
        .build()

    val chargingService: FargateService
    val authService: FargateService

    init {
        chargingService = createFargateService(
            id = "ChargingService",
            imageName = "charging-service:latest",
            ports = listOf(8082),
            db = db,
            additionalEnvVars = mapOf(
                "SPRING_RABBITMQ_HOST" to Fn.select(0, rabbitMqBroker.attrAmqpEndpoints)
            )
        )

        authService = createFargateService(
            id = "AuthService",
            imageName = "auth-service:latest",
            ports = listOf(8084),
            db = null,
            additionalEnvVars = mapOf(
                "SPRING_RABBITMQ_HOST" to Fn.select(0, rabbitMqBroker.attrAmqpEndpoints)
            )
        )

        chargingService.node.addDependency(db)
        chargingService.node.addDependency(rabbitMqBroker)
        authService.node.addDependency(rabbitMqBroker)

    }

    private fun createFargateService(
        id: String,
        imageName: String,
        ports: List<Int>,
        db: DatabaseInstance?,
        additionalEnvVars: Map<String, String>
    ): FargateService {
        val taskDefinition = FargateTaskDefinition.Builder.create(this, "${id}Task")
            .cpu(256)
            .memoryLimitMiB(512)
            .build()

        val envVars = mutableMapOf<String, String>()

        db?.let {
            envVars["SPRING_DATASOURCE_URL"] = "jdbc:postgresql://${it.dbInstanceEndpointAddress}:${it.dbInstanceEndpointPort}/charging-management"
            envVars["SPRING_DATASOURCE_USERNAME"] = "postgres"
            envVars["SPRING_DATASOURCE_PASSWORD"] = it.secret?.secretValueFromJson("password")?.toString() ?: ""
            envVars["SPRING_JPA_HIBERNATE_DDL_AUTO"] = "update"
            envVars["SPRING_SQL_INIT_MODE"] = "always"
            envVars["SPRING_DATASOURCE_HIKARI_INITIALIZATION_FAIL_TIMEOUT"] = "60000"
        }

        // Add RabbitMQ or other custom environment variables
        envVars.putAll(additionalEnvVars)

        val containerOptions = ContainerDefinitionOptions.builder()
            .image(ContainerImage.fromRegistry(imageName))
            .portMappings(ports.map { port ->
                PortMapping.builder()
                    .containerPort(port)
                    .hostPort(port)
                    .protocol(Protocol.TCP)
                    .build()
            })
            .logging(
                LogDriver.awsLogs(
                    AwsLogDriverProps.builder()
                        .logGroup(
                            LogGroup.Builder.create(this, "${id}LogGroup")
                                .logGroupName("/ecs/$imageName")
                                .removalPolicy(RemovalPolicy.DESTROY)
                                .retention(RetentionDays.ONE_DAY)
                                .build()
                        )
                        .streamPrefix(imageName)
                        .build()
                )
            )
            .environment(envVars)
            .build()

        taskDefinition.addContainer("${imageName}Container", containerOptions)

        return FargateService.Builder.create(this, id)
            .cluster(ecsCluster)
            .taskDefinition(taskDefinition)
            .assignPublicIp(false)
            .serviceName(imageName)
            .build()
    }



}

