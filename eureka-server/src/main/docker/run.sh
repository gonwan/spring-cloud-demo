#!/bin/sh
echo "********************************************************"
echo "Starting Eureka Server                                  "
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -jar /usr/local/eureka-server/@project.build.finalName@.jar
