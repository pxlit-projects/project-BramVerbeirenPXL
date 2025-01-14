FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/review-service.jar /app/review-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/review-service.jar"]
