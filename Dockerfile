# Build stage: uses Gradle + JDK 17 to compile, test, and package the app.
FROM gradle:8.14.3-jdk17 AS builder

# All project files will be copied and built inside /app.
WORKDIR /app

# Copy Gradle wrapper, Gradle config, build files, and source code.
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src

# Make the wrapper executable, then run tests and build the Spring Boot jar.
RUN chmod +x gradlew
RUN ./gradlew clean test bootJar --no-daemon

# Runtime stage: smaller image with only Java 17 runtime to run the jar.
FROM eclipse-temurin:17-jre-jammy AS runtime

# The application will run from /app.
WORKDIR /app

# Copy only the built jar from the builder stage into the runtime image.
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# Document that the Spring Boot app listens on port 8080.
EXPOSE 8080

# Start the application when the container launches.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
