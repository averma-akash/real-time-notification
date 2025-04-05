FROM openjdk:17-jdk-slim

WORKDIR /app

# Correct the filename in COPY command
COPY target/notification-1.0.jar real-time-notification.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "real-time-notification.jar"]
