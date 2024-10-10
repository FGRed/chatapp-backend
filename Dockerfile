FROM openjdk:18
COPY ../target/chatapp-backend-0.0.1-SNAPSHOT.jar.original /tmp
WORKDIR /tmp
CMD ["java", "-jar", "chatapp-backend-0.0.1-SNAPSHOT.jar"]