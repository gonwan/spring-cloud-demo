#!/bin/sh

echo "********************************************************"
echo "Waiting for Eureka Server to start on port: $EUREKA_SERVER_PORT"
echo "********************************************************"
while ! `nc -z eureka-server $EUREKA_SERVER_PORT`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo "Waiting for Configuration Server to start on port: $CONFIG_SERVER_PORT"
echo "********************************************************"
while ! `nc -z config-server $CONFIG_SERVER_PORT`; do sleep 3; done
echo "*******  Configuration Server has started"

echo "********************************************************"
echo "Waiting for Zipkin to start on port: $ZIPKIN_PORT"
echo "********************************************************"
while ! `nc -z zipkin $ZIPKIN_PORT`; do sleep 10; done
echo "******* Zipkin has started"

echo "********************************************************"
echo "Starting Organization Service                           "
echo "Using profile: $PROFILE                                 "
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT     \
     -Deureka.client.enabled=true                                           \
     -Deureka.client.serviceUrl.defaultZone=$EUREKA_SERVER_URI              \
     -Dspring.cloud.config.enabled=true                                     \
     -Dspring.cloud.config.uri=$CONFIG_SERVER_URI                           \
     -Dspring.profiles.active=$PROFILE                                      \
     -Dspring.zipkin.baseUrl=$ZIPKIN_URI                                    \
     -Dspring.security.oauth2.client.provider.sc-provider.userInfoUri=$AUTH_SERVICE_URI       \
     -jar /usr/local/@project.artifactId@/@project.build.finalName@.jar
