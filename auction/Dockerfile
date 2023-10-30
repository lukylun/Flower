FROM openjdk:17-ea-11-jdk-slim

VOLUME /tmp

COPY build/libs/auction-1.0.jar AuctionService.jar

ENTRYPOINT ["java", "-jar", "AuctionService.jar"]
