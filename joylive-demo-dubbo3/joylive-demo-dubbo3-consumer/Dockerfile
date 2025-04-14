FROM ghcr.io/meshware/ubuntu-oracle-jdk:11.0.21
USER root

ENV TZ Asia/Shanghai

WORKDIR /export/servers/
COPY ./target/joylive-demo-dubbo3-consumer.jar /export/servers/

VOLUME ["/export/servers/logs", "/export/servers/config"]
EXPOSE 20880
ENTRYPOINT ["/bin/bash", "-c", "java ${JAVA_OPTS} ${SGM_OPTS} ${JOYLIVE_OPTS} -jar /export/servers/joylive-demo-dubbo3-consumer.jar"]