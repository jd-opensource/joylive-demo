FROM ghcr.io/meshware/ubuntu-oracle-jdk:11.0.21
USER root

ENV TZ Asia/Shanghai

WORKDIR /export/servers/
COPY ./target/joylive-demo-springcloud-greenwich-consumer.jar /export/servers/

VOLUME ["/export/servers/logs", "/export/servers/config"]
EXPOSE 18080
ENTRYPOINT ["/bin/bash", "-c", "java ${JAVA_OPTS} -jar /export/servers/joylive-demo-springcloud-greenwich-consumer.jar"]