FROM ghcr.io/meshware/ubuntu-oracle-jdk:11.0.21
USER root

ENV TZ Asia/Shanghai

WORKDIR /export/servers/
COPY ./target/joylive-demo-springcloud2023-order-redis.jar /export/servers/

VOLUME ["/export/servers/logs", "/export/servers/config"]
EXPOSE 18081
ENTRYPOINT ["/bin/bash", "-c", "java ${JAVA_OPTS} ${SGM_OPTS} ${JOYLIVE_OPTS} -jar /export/servers/joylive-demo-springcloud2023-order-redis.jar"]