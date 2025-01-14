FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/notification-service.jar /app/notification-service.jar
EXPOSE 8061
ENTRYPOINT ["java", "-jar", "/app/notification-service.jar"]
