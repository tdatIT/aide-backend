# Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Add labels
LABEL maintainer="aide-backend"
LABEL description="Aide Backend Service"
LABEL version="1.0"

WORKDIR /workspace/app

# Copy pom.xml first to leverage Docker cache
COPY pom.xml .

# Download dependencies in a separate layer
RUN mvn dependency:go-offline

# Copy source code
COPY src src

# Build the application
RUN mvn clean package -DskipTests \
    && mkdir -p target/dependency \
    && (cd target/dependency; jar -xf ../*.jar)

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Create a non-root user
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Copy dependencies and application
COPY --from=build /workspace/app/target/dependency/BOOT-INF/lib /app/lib
COPY --from=build /workspace/app/target/dependency/META-INF /app/META-INF
COPY --from=build /workspace/app/target/dependency/BOOT-INF/classes /app

# Set proper permissions
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring

# Configure JVM options and Spring profile
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Expose the application port
EXPOSE 8080

# Set the entrypoint with optimized JVM options
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -cp app:app/lib/* com.aide.backend.AideBackendApplication"]