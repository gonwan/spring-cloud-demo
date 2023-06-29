FROM eclipse-temurin:8-jre-alpine
RUN apk update && apk upgrade && apk add curl netcat-openbsd
RUN mkdir -p /usr/local/@project.artifactId@
ADD maven/@project.build.finalName@.jar /usr/local/@project.artifactId@/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
