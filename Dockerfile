FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .

COPY ./src/ src/

WORKDIR /app

RUN mvn install

FROM openjdk:17

COPY --from=build /app/target/studadvice-0.0.1-SNAPSHOT.jar app/studadvice-0.0.1-SNAPSHOT.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "studadvice-0.0.1-SNAPSHOT.jar"]