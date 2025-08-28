# Stage 1: Build JAR
FROM maven:3.8.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run JAR
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
ARG JAR_NAME=auth.service-0.0.1-SNAPSHOT.jar
#ARG JAR_FILE=build/libs/*.jar
COPY --from=builder /app/target/${JAR_NAME} app.jar
COPY src/main/resources/application-docker.yaml ./config/application.yml
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:./config/application.yml"]

#FROM eclipse-temurin:17-jre
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]