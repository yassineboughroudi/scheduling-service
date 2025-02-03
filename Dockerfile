# ----------------------------
# Stage 1: Build the JAR
# ----------------------------
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copy Maven/Gradle wrapper and config
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Copy the source code
COPY src ./src

# Build the application (skip tests if you prefer fast builds)
RUN ./mvnw clean package -DskipTests

# ----------------------------
# Stage 2: Create the runtime image
# ----------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Expose the port used by the service (change as appropriate)
# e.g., 8086 for billing, 8083 for scheduling, etc.
EXPOSE 8080

# Copy the built .jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
