FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/comment-service.jar /app/comment-service.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app/comment-service.jar"]
