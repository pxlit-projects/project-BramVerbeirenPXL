# Gebruik een OpenJDK-image als basis
FROM openjdk:17-jdk-slim

# Zet de werkdirectory
WORKDIR /app

# Kopieer de jar naar de container
COPY target/discovery-service.jar /app/discovery-service.jar

# Exposeer poort 8061
EXPOSE 8061

# Voer de jar uit
ENTRYPOINT ["java", "-jar", "/app/discovery-service.jar"]
