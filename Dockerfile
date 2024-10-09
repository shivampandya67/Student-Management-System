# Dockerfile
FROM openjdk:11-jre-slim

WORKDIR /app

# Copy the JAR file into the container
COPY target/my-java-app-1.0-SNAPSHOT.jar /app/myJavaApp.jar

# Command to run the JAR file
CMD ["java", "-jar", "myJavaApp.jar"]
