# ---------- BUILD STAGE ----------
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# 🔥 FIX: give execute permission
RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw clean package -DskipTests

# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 9090
ENTRYPOINT ["java","-jar","app.jar"]
