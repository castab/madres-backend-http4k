# Dockerfile
FROM gradle:9.3.1-jdk25 AS build

WORKDIR /app

# Copy gradle files first for caching
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

# Download dependencies
RUN gradle dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build the application
RUN gradle shadowJar --no-daemon

# Runtime stage
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/build/libs/*-all.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the application
# IMPORTANT: Use exec form to ensure proper signal handling
ENTRYPOINT ["java", "-jar", "app.jar"]