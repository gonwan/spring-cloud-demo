FROM adoptopenjdk/openjdk8:alpine-jre
RUN apk update && apk upgrade && apk add curl netcat-openbsd
RUN mkdir -p /usr/local/@project.artifactId@
ADD @project.build.finalName@.jar /usr/local/@project.artifactId@/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
