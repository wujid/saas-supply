FROM adoptopenjdk/openjdk11:latest

MAINTAINER wjd

WORKDIR /supply

ADD ./target/*.jar app.jar

ENV LANG C.UTF-8

ENV TIMEZONE Asia/Shanghai

ENV JAVA_OPTS=""

ENTRYPOINT java ${JAVA_OPTS} -jar app.jar

EXPOSE 8080