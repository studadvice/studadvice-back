## Studadvice-back

## Membres
- Churchill Atchedji
- Isidore Amevigbe
- Zakaria El Khayari
- Nada Zine

<!-- TOC -->
* [Studadvice-back](#projet--promotion)
    * [1/ Sujet](#1-sujet)
    * [2/ Technologies et outils](#2-technologies-et-outils)
    * [3/ Récupération du projet](#3-récupération-du-projet)
    * [4/ Exécution du projet](#4-exécution-du-projet)
        * [4.1/ Éxécution du projet avec Docker](#41-éxécution-du-projet-avec-docker)
        * [4.2/ Éxécution du projet sans Docker](#42-éxécution-du-projet-sans-docker)
    * [5/ Informations sur le projet](#5-informations-sur-le-projet)
        * [5.1/ Architecture](#52-architecture)
        * [5.2/ Choix](#53-choix)
<!-- TOC -->


### 1/ Sujet

Ce projet représente la partie backend de l'application Stud'Advice, conçue spécifiquement pour assister les étudiants dans la recherche des aides et des bons plans disponibles pour eux 
ainsi que dans la gestion des diverses démarches administratives essentielles à leur statut d'étudiant. 
Le projet représentant la partie frontend se trouve ici (https://github.com/studadvice/studadvice-front) et le backoffice ici (https://github.com/studadvice/studadvice-backoffice).

### 2/ Technologies et libraries

- Spring Boot : Employé en tant que framework principal au sein de notre partie backend de l'application.
- MongoDB : Sélectionné en tant que base de données de l'application en raison de son efficacité dans la gestion des données de l'application Stud'Advice. Cette solution s'avère particulièrement adaptée, car elle permet de représenter de manière souple des documents reflétant les démarches administratives et les opportunités avantageuses pour les étudiants.
- FireBase & Spring-security :  Utilisé pour l'authentification en combinaison avec Spring-Security.
- Docker : Adopté pour la conteneurisation.
- Lombok : Utilisé afin de réduire la verbosité du code Java en générant automatiquement certains éléments du code source principalement les getters et setters dans notre cas.
- Springdoc-openapi : Utilisé pour générer le swagger de notre API.
- Mockito : Utilisé pour les tests unitaires, Mockito permet de simuler le comportement des dépendances et d'assurer la qualité du code en vérifiant son bon fonctionnement dans des scénarios divers.
- SonarCloud : Intégré pour l'analyse statique du code, il nous assure une qualité de code continue en identifiant et en signalant les éventuelles vulnérabilités, bugs, et autres problèmes de qualité.

### 3/ Récupération du projet

Pour obtenir une copie du projet, suivez ces étapes simples dans votre terminal. Tout d'abord, positionnez-vous dans le répertoire où vous souhaitez enregistrer le projet, puis exécutez la commande correspondante à votre méthode de récupération préférée :

Pour récupérer le projet via *SSH*

```sh
git clone git@github.com:studadvice/studadvice-back.git
```

Pour récupérer le projet via *HTTPS*

```sh
git clone https://github.com/studadvice/studadvice-back.git
```

### Prérequis

Avant de procéder à l'exécution de votre projet, il faut obligatoirement configurer la variable d'environnement GOOGLE_APPLICATION_CREDENTIALS sur votre machine. 
Cette variable doit contenir le chemin absolu vers le fichier service-account.json. C'est un fichier de configuration fourni par Firebase qui contient les clés d'authentification nécessaires pour accéder aux services cloud, tels que Firebase Authentication utilisé par notre application.
Ce fichier est essentiel pour permettre à notre application d'interagir de manière sécurisée avec les services Firebase.

#### Obtenir le fichier service-account.json

Pour récupérer le fichier service-account.json, suivez ces étapes dans la console Firebase :

- Accédez à la console Firebase via le lien suivant : https://console.firebase.google.com/.
- Sélectionnez le projet StudAdvice correspondant à l'application.
- Accédez à la section "Paramètres" dans le menu de gauche.
- Dans la sous-section "Utilisateurs et autorisations", sélectionnez "Comptes de service".
- Choisissez l'option "Générer une nouvelle clé privée".

- Un fichier au format JSON sera alors automatiquement téléchargé, il correspond au service-account.json recherché.

#### Définir la variable d'environnement : 

Ouvrez votre terminal et exécutez la commande suivante, en remplaçant le chemin avec le chemin absolu vers votre fichier service-account.json :

- Sur Linux/Mac :
```sh
export GOOGLE_APPLICATION_CREDENTIALS=/chemin/vers/votre/fichier/service-account.json
```

```sh
echo $GOOGLE_APPLICATION_CREDENTIALS
```
Vous devriez voir le chemin absolu vers votre fichier service-account.json.


- Sur Windows (Command Prompt) :
```sh
set GOOGLE_APPLICATION_CREDENTIALS=C:\chemin\vers\votre\fichier\service-account.json
```

```sh
echo $GOOGLE_APPLICATION_CREDENTIALS%
```
Vous devriez voir le chemin absolu vers votre fichier service-account.json.

### 4/ Éxécution du projet

#### 4.1/ Éxécution du projet avec Docker
Si Docker est disponible sur votre système, vous pouvez lancer l'infrastructure en vous positionnant à la racine du projet. Exécutez la commande suivante pour construire et démarrer les conteneurs :

```
cd docker; docker-compose up --build
```

L'API sera disponible sur le port 8080 et sa documentation sur http://localhost:8080/swagger-ui/index.html#/

#### 4.2/ Éxécution du projet sans Docker

Si Docker n'est pas installé sur votre système, veuillez suivre les étapes ci-dessous pour configurer l'environnement nécessaire au bon fonctionnement de l'application :

- Java 17 : Si vous n'avez pas Docker, installez Java 17 ou une version ultérieure en suivant les instructions disponibles sur le lien suivant (https://www.java.com/fr/download/help/download_options_fr.html).

- Maven : Assurez-vous d'avoir Maven installé sur votre machine en suivant les directives fournies sur la page suivante ((https://maven.apache.org/install.html).

- MongoDB : Si MongoDB n'est pas installé en tant que service sur votre système, suivez les instructions d'installation disponibles sur le lien suivant (https://www.mongodb.com/docs/manual/installation/).
Si MongoDB n'est pas installé en tant que service, vous devez lancer une instance de la base de données avant d'exécuter l'application.

Une fois MongoDB en cours d'exécution, allez à la racine du projet dans une nouvelle fenêtre de terminal et tapez la commande suivante pour lancer l'application

```
mvn spring-boot:run
```

L'API sera disponible sur le port 8080 et sa documentation sur http://localhost:8080/swagger-ui/index.html#/

### 5/ Informations sur le projet

#### 5.1/ Architecture

