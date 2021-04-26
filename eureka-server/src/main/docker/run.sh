#!/bin/sh
echo "********************************************************"
echo "Starting Eureka Server                                  "
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -jar /usr/local/@project.artifactId@/@project.build.finalName@.jar
