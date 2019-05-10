FROM openjdk:8-jre-alpine
RUN apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/authentication-service
ADD @project.build.finalName@.jar /usr/local/authentication-service/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
