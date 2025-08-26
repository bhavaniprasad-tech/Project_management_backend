# ==============================
# Stage 1: Build the Spring Boot app using Maven
# ==============================

# Use an official Maven image with JDK 21 installed
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set the working directory inside the container to /app
WORKDIR /app

# Copy all project files from the host machine to the container's /app directory
COPY pom.xml .
RUN mvn dependency:go-offline

# Run Maven to clean and package the Spring Boot application
# -DskipTests means skip running tests to speed up the build
COPY src ./src
RUN mvn clean package -DskipTests

# ==============================
# Stage 2: Create a lightweight image for running the app
# ==============================

# Use a smaller JDK image to keep the final image size small
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged jar file from the build stage into this final image
# /app/target/*.jar means any jar file in target folder
COPY --from=build /app/target/projectmanagementsystem-0.0.1-SNAPSHOT.jar .

# Expose port 8080
EXPOSE 8080

# Define the entry point to run the jar file
ENTRYPOINT ["java", "-jar", "/app/projectmanagementsystem-0.0.1-SNAPSHOT.jar"]
