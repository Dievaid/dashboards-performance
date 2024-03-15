FROM maven:3-amazoncorretto-17 as build-env

WORKDIR /app
COPY ./queen-problem-java .

RUN mvn verify

FROM openjdk:22-ea-17-jdk-slim-bullseye
WORKDIR /app
COPY --from=build-env /app/target .

ENTRYPOINT ["java", "-cp", "QueenProblem-1.0-SNAPSHOT-jar-with-dependencies.jar", "app.Main"]