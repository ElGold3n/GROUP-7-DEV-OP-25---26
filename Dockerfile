# --- Stage 1: Build with Maven ---
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies first (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 2: Runtime image ---
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the fat JAR from the build stage
COPY --from=builder /app/target/world-reporting-system-1.0.0-jar-with-dependencies.jar app.jar

# Run the app
CMD ["java", "-jar", "app.jar"]
