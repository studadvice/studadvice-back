# Etape de Build avec Maven
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY ./src/ src/

# Utilisez ARG pour définir un argument de build
ARG FIREBASE_CONFIG_PATH

# Copiez le fichier de configuration Firebase dans le conteneur
# Assurez-vous que FIREBASE_CONFIG_PATH est le chemin complet du fichier sur la machine hôte
COPY ${FIREBASE_CONFIG_PATH} /app/firebase-config.json

# Utilisez ENV pour définir la variable d'environnement après le COPY
ENV GOOGLE_APPLICATION_CREDENTIALS=/app/firebase-config.json

RUN mvn install -DskipTests

# Etape de Build Final
FROM openjdk:17

COPY --from=build /app/target/studadvice-0.0.1-SNAPSHOT.jar /app/studadvice-0.0.1-SNAPSHOT.jar

# Copiez le fichier de configuration Firebase dans le conteneur
COPY --from=build /app/firebase-config.json /app/firebase-config.json

ENV GOOGLE_APPLICATION_CREDENTIALS=/app/firebase-config.json

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "studadvice-0.0.1-SNAPSHOT.jar"]
