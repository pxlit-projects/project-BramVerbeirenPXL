FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/post-service.jar /app/post-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/post-service.jar"]
