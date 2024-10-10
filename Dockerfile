FROM maven:3-amazoncorretto-18 AS build
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:18-slim
COPY --from=build /target/chatapp-backend-23.26.08.01-SNAPSHOT.jar chat-app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","chat-app.jar"]
