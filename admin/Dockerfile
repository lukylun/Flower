FROM openjdk:17-ea-11-jdk-slim

VOLUME /tmp

COPY build/libs/admin-1.0.jar AdminService.jar

ENTRYPOINT ["java", "-jar", "AdminService.jar"]
