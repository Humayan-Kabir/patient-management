# Stage1: Build(cached dependencies)
FROM maven:3.8.3-eclipse-temurin-17 AS builder

WORKDIR /app

# 1. Copy POM first for dependency caching
COPY pom.xml .

# 2. Cache dependencies (host-managed, not in image)
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B

# 3. Build application
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package

# Stage2: Runtime
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

