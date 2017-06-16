Installatiehandleiding
=========

*Opmerking*: Dit is een initiele versie die nog niet uitgebreid is getest

Deze handleiding is gebaseerd op een Ubuntu Server 16.04 of hoger

1. Installeer de benodigde packages:
``
sudo apt-get update
sudo apt-get install tomcat8 nginx letsencrypt
``

2. Installeer een mqtt broker
a. Mosquitto: ``sudo apt-get install mosquitto``
OF
b. EMQTT: ``sudo apt-get install lksctp-tools && wget http://emqtt.io/downloads/latest/ubuntu16_04-deb && dpkg -i ubuntu16_04-deb``

3. Maak de volgende mappenstructuur aan:

``/opt/displaydirect/backup
/opt/displaydirect/config
/opt/displaydirect/data``

4. Bewerk de crontab, en voeg de volgende twee regels toe (bij voorkeur net andere tijden)

``
23 25 * * *  cd /opt/displaydirect/data/ && wget -N --accept=gz -r http://kv7.openov.nl/GOVI/KV7kalender/ -l 1 -e robots=off && wget -N --accept=gz -r http://kv7.openov.nl/GOVI/KV7planning/ -l 1 -e robots=off
22 00 * * *  cd /opt/displaydirect/data/ && wget -N --accept="PassengerStopAssignmentExport*" -r http://data.ndovloket.nl/haltes/ -l 1 -e robots=off && find data.ndovloket.nl/haltes/* -mtime +1 -exec rm {} \;
``

5. Voer beide update commando's eerst handmatig uit
cd /opt/displaydirect/data/ && wget -N --accept=gz -r http://kv7.openov.nl/GOVI/KV7kalender/ -l 1 -e robots=off && wget -N --accept=gz -r http://kv7.openov.nl/GOVI/KV7planning/ -l 1 -e robots=off
cd /opt/displaydirect/data/ && wget -N --accept="PassengerStopAssignmentExport*" -r http://data.ndovloket.nl/haltes/ -l 1 -e robots=off

6. Zorg voor drie war bestanden: dashboard.war, distribution.war en virtual_screen.war. Dit kan bijvoorbeeld door de broncode te bouwen met commando ``./gradlew war``. Plaats deze op de server in ``/var/lib/tomcat8/webapps/``
export SERVER=root@51.15.74.251
scp distribution/build/libs/distribution-0.2-SNAPSHOT.war $SERVER:/tmp/distribution.war
scp virtual_screen/build/libs/virtual_screen-0.2-SNAPSHOT.war $SERVER:/tmp/virtual_screen.war
scp dashboard/build/libs/dashboard-0.2-SNAPSHOT.war $SERVER:/tmp/dashboard.war

7. Bewerk de tomcat8 defaults in ``/etc/default/tomcat8``:
``
JAVA_OPTS="-Djava.awt.headless=true -Xmx10G -Xms4G -XX:+UseConcMarkSweepGC -Darchaius.configurationSource.additionalUrls=file:///opt/displaydirect/config/configuration.properties"
``

7. Voeg de volgende NGINX configuraties toe:
``
server {
        listen [::]:80;
        listen 80;
        listen 443 ssl;

        server_name dev.opendris.nl;
        root /var/www/direct;

        ssl_certificate /etc/letsencrypt/live/dev.opendris.nl/fullchain.pem;
        ssl_certificate_key /etc/letsencrypt/live/dev.opendris.nl/privkey.pem;

        proxy_cache off;

        gzip            on;
        gzip_min_length 1000;
        gzip_proxied    expired no-cache no-store private auth;
        gzip_types      text/plain application/xml application/json application/x-javascript text/xml text/css;
        gzip_vary       on;
        gzip_buffers 4 32k;
        gzip_comp_level 2;

        location ~ /.well-known/acme-challenge {
                root /var/www/letsencrypt;
                allow all;
        }


        location / {
         proxy_set_header X-Forwarded-Host $host;
                proxy_set_header X-Forwarded-Server $host;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_pass http://127.0.0.1:8080/distribution/;

        }

}
``
``
server {
        listen [::]:80;
        listen 80;
        listen 443 ssl;

        server_name screen.dev.opendris.nl;
        root /var/www/direct;

        ssl_certificate /etc/letsencrypt/live/dev.opendris.nl/fullchain.pem;
        ssl_certificate_key /etc/letsencrypt/live/dev.opendris.nl/privkey.pem;

        proxy_cache off;

        gzip            on;
        gzip_min_length 1000;
        gzip_proxied    expired no-cache no-store private auth;
        gzip_types      text/plain application/xml application/json application/x-javascript text/xml text/css;
        gzip_vary       on;
        gzip_buffers 4 32k;
        gzip_comp_level 2;

        location ~ /.well-known/acme-challenge {
                root /var/www/letsencrypt;
                allow all;
        }


        location / {
         proxy_set_header X-Forwarded-Host $host;
                proxy_set_header X-Forwarded-Server $host;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_pass http://127.0.0.1:8080/virtual_screen/;

        }

}
``
``
server {
        listen [::]:80;
        listen 80;
        listen 443 ssl;

        server_name dashboard.dev.opendris.nl;
        root /var/www/direct;

        ssl_certificate /etc/letsencrypt/live/dev.opendris.nl/fullchain.pem;
        ssl_certificate_key /etc/letsencrypt/live/dev.opendris.nl/privkey.pem;

        proxy_cache off;

        gzip            on;
        gzip_min_length 1000;
        gzip_proxied    expired no-cache no-store private auth;
        gzip_types      text/plain application/xml application/json application/x-javascript text/xml text/css;
        gzip_vary       on;
        gzip_buffers 4 32k;
        gzip_comp_level 2;

        location ~ /.well-known/acme-challenge {
                root /var/www/letsencrypt;
                allow all;
        }


        location / {
         proxy_set_header X-Forwarded-Host $host;
                proxy_set_header X-Forwarded-Server $host;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_pass http://127.0.0.1:8080/dashboard/;

        }

}
``

8. Configuratie kan hier worden aangepast ``/opt/displaydirect/config/configuration.properties``
