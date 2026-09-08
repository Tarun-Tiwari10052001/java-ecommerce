# --- Build stage ---
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /build

# Cache dependencies separately from source so code changes don't re-download the world.
COPY pom.xml .
RUN mvn -q -B dependency:go-offline

COPY src ./src
RUN mvn -q -B clean package -DskipTests

# --- Runtime stage ---
FROM eclipse-temurin:11-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl \
    && addgroup -S app && adduser -S app -G app

COPY --from=build /build/target/*.war app.war
RUN chown app:app app.war
USER app

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=5s --start-period=40s --retries=5 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.war"]
