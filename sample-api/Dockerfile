FROM public.ecr.aws/docker/library/openjdk:21-jdk-slim AS build

RUN set -ex && \
    apt-get update && \
    apt-get install -y wget

# Download dependencies
RUN date && \
    mkdir -p /opt/dd-jar && \
    wget --progress=bar:force:noscroll https://dtdg.co/latest-java-tracer && \
    mv latest-java-tracer /opt/dd-jar/dd-java-agent.jar

## RUNTIME
FROM public.ecr.aws/docker/library/openjdk:21-jdk-slim AS runtime
LABEL maintainer="skafld. <developers@skafld.com>"
ENV JAR_FILE=sample-api.jar

ARG DOCKER_IMAGE_TAG

ENV DOCKER_IMAGE_TAG=${DOCKER_IMAGE_TAG}

# Create Home Folder
ENV APP_HOME="/usr/share/skafld"
RUN mkdir -p $APP_HOME

# Set Workdir
WORKDIR $APP_HOME

# DataDog Agent
COPY --from=build /opt/dd-jar/dd-java-agent.jar /usr/share/skafld/dd-java-agent.jar

# Java Variables
ENV JAVA_OPTS="-Ddd.version=$DOCKER_IMAGE_TAG -Duser.timezone=UTC -Djava.security.egd=file:/dev/./urandom"
ENV JVM_ARGS=""
ENV LANG=C.UTF-8

# Expose Java and Management ports
EXPOSE 8080 8001

COPY target/$JAR_FILE $APP_HOME/$JAR_FILE
COPY datadog.properties $APP_HOME/datadog.properties

ENTRYPOINT java $JVM_ARGS $JAVA_OPTS -jar $APP_HOME/$JAR_FILE
