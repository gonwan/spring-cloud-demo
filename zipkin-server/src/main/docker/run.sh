#!/bin/sh

echo "********************************************************"
echo "Waiting for Configuration Server to start on port: $CONFIG_SERVER_PORT"
echo "********************************************************"
while ! `nc -z config-server $CONFIG_SERVER_PORT`; do sleep 3; done
echo "*******  Configuration Server has started"

echo "********************************************************"
echo "Starting Zipkin Server                                  "
echo "Using profile: $PROFILE                                 "
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT     \
     -Dspring.profiles.active=$PROFILE                                      \
     -Dspring.cloud.config.enabled=true                                     \
     -Dspring.config.import=optional:configserver:$CONFIG_SERVER_URI        \
     -jar /usr/local/@project.artifactId@/@project.build.finalName@.jar
