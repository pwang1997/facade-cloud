# Use a base image with Maven and Java
FROM maven:3.8.4-openjdk-17-slim AS build
# Set the working directory
WORKDIR /app

# Copy the entire project (including source code) into the container
COPY . .

# Build the JAR file
RUN mvn -Dmaven.test.skip clean package

# Create a new image with only the JAR file
FROM openjdk:17-jdk-alpine AS runner

ENV SPRING_PROFILES_ACTIVE=prod

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY ./src/main/resources/application-prod.properties /app/application-prod.properties

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]

