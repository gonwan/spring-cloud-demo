#!/bin/sh

echo "********************************************************"
echo "Waiting for Eureka Server to start on port: $EUREKA_SERVER_PORT"
echo "********************************************************"
while ! `nc -z eureka-server $EUREKA_SERVER_PORT`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo "Starting Configuration Service with Eureka URI: $EUREKA_SERVER_URI";
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom                                \
     -Deureka.client.enabled=true                                           \
     -Deureka.client.serviceUrl.defaultZone=$EUREKA_SERVER_URI              \
     -jar /usr/local/@project.artifactId@/@project.build.finalName@.jar
