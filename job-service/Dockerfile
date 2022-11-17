FROM adoptopenjdk/openjdk11:latest

MAINTAINER wjd

WORKDIR /supply

ADD ./target/*.jar app.jar

ENV JAVA_OPTS=""

ENTRYPOINT java ${JAVA_OPTS} -jar app.jar

# 暴露9999(默认)为执行器端口须与配置xxl.job.executor.port保持一致
EXPOSE 8080 9999