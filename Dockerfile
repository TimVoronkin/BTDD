# ─────────────────────────────────────────────
# STAGE 1: Build
# Uses full Maven + JDK image to compile & package
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copy dependency descriptor first (Docker layer cache optimization:
# only re-downloads deps when pom.xml changes, not on every code change)
COPY pom.xml .
RUN apk add --no-cache maven
RUN mvn -f pom.xml dependency:go-offline -B 2>/dev/null || true

# Copy source and build the JAR (skip tests — they already ran in CI)
COPY src ./src
RUN mvn -f pom.xml clean package -DskipTests -B

# ─────────────────────────────────────────────
# STAGE 2: Runtime
# Slim image — only JRE, no JDK, no Maven
# Result: ~180 MB instead of ~600 MB
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Copy only the built JAR from the build stage
COPY --from=build /app/target/booking-system-0.0.1-SNAPSHOT.jar app.jar

# Expose the port Spring Boot listens on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
