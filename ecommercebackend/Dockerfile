# ====== BUILD STAGE ======
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies (for faster caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build the JAR
COPY src ./src
# Build the JAR (skip tests to speed up build)
RUN mvn clean package -DskipTests

# ====== RUN STAGE ======
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080 5005

# Run the application
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]
