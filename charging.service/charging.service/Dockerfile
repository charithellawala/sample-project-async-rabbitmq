# Stage 1: Build JAR
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run JAR
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
ARG JAR_NAME=charging.service-0.0.1-SNAPSHOT.jar
COPY --from=builder /app/target/${JAR_NAME} app.jar
COPY src/main/resources/application.yaml ./config/application.yml
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:./config/application.yml"]

