FROM openjdk:21-jdk-slim

ADD target/movie-rating-service*.jar /app/

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

EXPOSE 8080
WORKDIR /app
CMD ["java", "-jar", "movie-rating-service-0.0.1-SNAPSHOT.jar"]
