#!/bin/bash

# Mise à jour du système
sudo apt-get update && sudo apt-get upgrade -y

# Installer Docker si ce n'est pas déjà fait
if ! [ -x "$(command -v docker)" ]; then
    sudo apt-get install docker.io -y
    sudo systemctl start docker
    sudo systemctl enable docker
fi

# Vérifier si Docker Swarm est déjà actif
swarm_active=$(docker info --format '{{.Swarm.LocalNodeState}}')
if [ "$swarm_active" != "active" ]; then
    echo "Initialisation de Docker Swarm..."
    docker swarm init
else
    echo "Docker Swarm est déjà actif."
fi

# Installer Nginx si ce n'est pas déjà fait
if ! [ -x "$(command -v nginx)" ]; then
    sudo apt-get install nginx -y
    sudo systemctl start nginx
    sudo systemctl enable nginx
fi

# Créer un dossier pour la configuration Nginx
sudo mkdir -p /etc/nginx/conf.d

# Créer le fichier de configuration Nginx (exemplaire simple)
# Créer et configurer le fichier de configuration Nginx
sudo tee /etc/nginx/conf.d/default.conf > /dev/null <<EOF
server {
    listen 80;
    listen [::]:80;
    server_name studadvice.com www.studadvice.com;

    # Spécifier le dossier racine où se trouvent les fichiers statiques

    # Ajouter les types MIME
    include /etc/nginx/mime.types;
    default_type application/octet-stream;

    # Configuration des différentes routes

    location / {
        proxy_pass http://localhost:4200;
        proxy_http_version 1.1;
        proxy_set_header Upgrade \$http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host \$host;
        proxy_cache_bypass \$http_upgrade;
    }

    # Configuration spécifique pour le back-end (si nécessaire)

    location ~ ^/api/(.*)$ {
        proxy_pass http://localhost:8080/$1;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header Host $host;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Normalize the request URIs for /required-document
    location ~ ^/required-document(/?)(.*)$ {
        proxy_pass http://localhost:8080/required-document$1$2;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header Host $host;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Normalize the request URIs for /deals
    location ~ ^/deals(/?)(.*)$ {
        proxy_pass http://localhost:8080/deals$1$2;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header Host $host;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Normalize the request URIs for /categories
    location ~ ^/categories(/?)(.*)$ {
        proxy_pass http://localhost:8080/categories$1$2;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header Host $host;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Normalize the request URIs for /administrative-process
    location ~ ^/administrative-process(/?)(.*)$ {
        proxy_pass http://localhost:8080/administrative-process$1$2;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header Host $host;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /v3/api-docs/ {
        proxy_pass http://localhost:8080/v3/api-docs;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header Host \$http_host;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
      }

    location /v3/api-docs/swagger-config {
        proxy_pass http://localhost:8080/v3/api-docs/swagger-config;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header Host \$http_host;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }

    location ~ /.well-known/acme-challenge {
        allow all;
        root /var/www/html;
        try_files $uri =404;
    }

    location @angular {
        proxy_pass http://localhost:4200;
    }

    error_page 404 = @angular;

}
EOF

# Redémarrer Nginx pour appliquer la nouvelle configuration
sudo systemctl restart nginx

# Créer un dossier pour les données de Portainer
sudo mkdir -p /opt/portainer/data

# Déployer Portainer pour la gestion des conteneurs
sudo docker service create \
    --name portainer \
    --publish 9000:9000 \
    --replicas=1 \
    --constraint 'node.role == manager' \
    --mount type=bind,src=/var/run/docker.sock,dst=/var/run/docker.sock \
    --mount type=bind,src=/opt/portainer/data,dst=/data \
    portainer/portainer-ce

# Afficher l'état des services
sudo docker service ls