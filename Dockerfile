# ===== Stage 1: Build =====
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy dependency manifests first for layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copy source and build
COPY src ./src
RUN mvn package -DskipTests -Dmaven.test.skip=true -q

# ===== Stage 2: Runtime =====
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /app/target/*.jar app.jar

RUN mkdir -p logs && chown -R appuser:appgroup /app
USER appuser

EXPOSE ${PORT:-8080}

ENTRYPOINT ["java", "-jar", "app.jar"]
