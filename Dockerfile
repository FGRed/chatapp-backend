FROM maven:openjdk:18-slim-bullseye AS build
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:18-slim-bullseye
COPY --from=build /target/chatapp-backend-0.0.1-SNAPSHOT.jar chat-app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","chat-app.jar"]
