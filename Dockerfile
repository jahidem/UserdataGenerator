# First stage: build the application
FROM maven:3.9.8-eclipse-temurin-21 AS build
COPY . /app
WORKDIR /app
RUN mvn package -DskipTests

# Second stage: create a slim image
FROM openjdk:21-jdk-slim
EXPOSE 8000
COPY --from=build /app/target/UserdataGenerator-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]