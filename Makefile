version=1.6.0-$(shell git rev-parse --short HEAD)
#version=1.0.0
repo ?= hub.jdcloud.com/jmsf

clean:
	mvn clean -f ../pom.xml

build:
	mvn package -f ../pom.xml -DskipTests=true package

image-joylive-demo-springboot2021-consumer:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-springboot2021-consumer:${version}-amd64 ./joylive-demo-springcloud2021/joylive-demo-springboot2021-consumer
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-springboot2021-consumer:${version}-arm64 ./joylive-demo-springcloud2021/joylive-demo-springboot2021-consumer

push-joylive-demo-springboot2021-consumer:
	docker push ${repo}/joylive-demo-springboot2021-consumer:${version}-amd64
	docker push ${repo}/joylive-demo-springboot2021-consumer:${version}-arm64
	docker manifest create ${repo}/joylive-demo-springboot2021-consumer:${version} \
	  ${repo}/joylive-demo-springboot2021-consumer:${version}-amd64 \
	  ${repo}/joylive-demo-springboot2021-consumer:${version}-arm64
	docker manifest push ${repo}/joylive-demo-springboot2021-consumer:${version}

image-joylive-demo-springboot2021-provider:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-springboot2021-provider:${version}-amd64 ./joylive-demo-springcloud2021/joylive-demo-springboot2021-provider
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-springboot2021-provider:${version}-arm64 ./joylive-demo-springcloud2021/joylive-demo-springboot2021-provider

push-joylive-demo-springboot2021-provider:
	docker push ${repo}/joylive-demo-springboot2021-provider:${version}-amd64
	docker push ${repo}/joylive-demo-springboot2021-provider:${version}-arm64
	docker manifest create ${repo}/joylive-demo-springboot2021-provider:${version} \
	  ${repo}/joylive-demo-springboot2021-provider:${version}-amd64 \
	  ${repo}/joylive-demo-springboot2021-provider:${version}-arm64
	docker manifest push ${repo}/joylive-demo-springboot2021-provider:${version}

image-joylive-demo-springcloud2021-consumer:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-springcloud2021-consumer:${version}-amd64 ./joylive-demo-springcloud2021/joylive-demo-springcloud2021-consumer
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-springcloud2021-consumer:${version}-arm64 ./joylive-demo-springcloud2021/joylive-demo-springcloud2021-consumer

push-joylive-demo-springcloud2021-consumer:
	docker push ${repo}/joylive-demo-springcloud2021-consumer:${version}-amd64
	docker push ${repo}/joylive-demo-springcloud2021-consumer:${version}-arm64
	docker manifest create ${repo}/joylive-demo-springcloud2021-consumer:${version} \
	  ${repo}/joylive-demo-springcloud2021-consumer:${version}-amd64 \
	  ${repo}/joylive-demo-springcloud2021-consumer:${version}-arm64
	docker manifest push ${repo}/joylive-demo-springcloud2021-consumer:${version}

image-joylive-demo-springcloud2021-provider:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-springcloud2021-provider:${version}-amd64 ./joylive-demo-springcloud2021/joylive-demo-springcloud2021-provider
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-springcloud2021-provider:${version}-arm64 ./joylive-demo-springcloud2021/joylive-demo-springcloud2021-provider

push-joylive-demo-springcloud2021-provider:
	docker push ${repo}/joylive-demo-springcloud2021-provider:${version}-amd64
	docker push ${repo}/joylive-demo-springcloud2021-provider:${version}-arm64
	docker manifest create ${repo}/joylive-demo-springcloud2021-provider:${version} \
	  ${repo}/joylive-demo-springcloud2021-provider:${version}-amd64 \
	  ${repo}/joylive-demo-springcloud2021-provider:${version}-arm64
	docker manifest push ${repo}/joylive-demo-springcloud2021-provider:${version}

image-joylive-demo-springcloud2021-provider-reactive:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-springcloud2021-provider-reactive:${version}-amd64 ./joylive-demo-springcloud2021/joylive-demo-springcloud2021-provider-reactive
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-springcloud2021-provider-reactive:${version}-arm64 ./joylive-demo-springcloud2021/joylive-demo-springcloud2021-provider-reactive

push-joylive-demo-springcloud2021-provider-reactive:
	docker push ${repo}/joylive-demo-springcloud2021-provider-reactive:${version}-amd64
	docker push ${repo}/joylive-demo-springcloud2021-provider-reactive:${version}-arm64
	docker manifest create ${repo}/joylive-demo-springcloud2021-provider-reactive:${version} \
	  ${repo}/joylive-demo-springcloud2021-provider-reactive:${version}-amd64 \
	  ${repo}/joylive-demo-springcloud2021-provider-reactive:${version}-arm64
	docker manifest push ${repo}/joylive-demo-springcloud2021-provider-reactive:${version}

image-joylive-demo-springcloud2021-order:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-springcloud2021-order:${version}-amd64 ./joylive-demo-springcloud2021/joylive-demo-springcloud2021-order
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-springcloud2021-order:${version}-arm64 ./joylive-demo-springcloud2021/joylive-demo-springcloud2021-order

push-joylive-demo-springcloud2021-order:
	docker push ${repo}/joylive-demo-springcloud2021-order:${version}-amd64
	docker push ${repo}/joylive-demo-springcloud2021-order:${version}-arm64
	docker manifest create ${repo}/joylive-demo-springcloud2021-order:${version} \
	  ${repo}/joylive-demo-springcloud2021-order:${version}-amd64 \
	  ${repo}/joylive-demo-springcloud2021-order:${version}-arm64
	docker manifest push ${repo}/joylive-demo-springcloud2021-order:${version}

image-joylive-demo-springcloud2021-gateway:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-springcloud2021-gateway:${version}-amd64 ./joylive-demo-springcloud2021/joylive-demo-springcloud2021-gateway
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-springcloud2021-gateway:${version}-arm64 ./joylive-demo-springcloud2021/joylive-demo-springcloud2021-gateway

push-joylive-demo-springcloud2021-gateway:
	docker push ${repo}/joylive-demo-springcloud2021-gateway:${version}-amd64
	docker push ${repo}/joylive-demo-springcloud2021-gateway:${version}-arm64
	docker manifest create ${repo}/joylive-demo-springcloud2021-gateway:${version} \
	  ${repo}/joylive-demo-springcloud2021-gateway:${version}-amd64 \
	  ${repo}/joylive-demo-springcloud2021-gateway:${version}-arm64
	docker manifest push ${repo}/joylive-demo-springcloud2021-gateway:${version}

image-joylive-demo-springcloud2023-consumer:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-springcloud2023-consumer:${version}-amd64 ./joylive-demo-springcloud2023/joylive-demo-springcloud2023-consumer
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-springcloud2023-consumer:${version}-arm64 ./joylive-demo-springcloud2023/joylive-demo-springcloud2023-consumer

push-joylive-demo-springcloud2023-consumer:
	docker push ${repo}/joylive-demo-springcloud2023-consumer:${version}-amd64
	docker push ${repo}/joylive-demo-springcloud2023-consumer:${version}-arm64
	docker manifest create ${repo}/joylive-demo-springcloud2023-consumer:${version} \
	  ${repo}/joylive-demo-springcloud2023-consumer:${version}-amd64 \
	  ${repo}/joylive-demo-springcloud2023-consumer:${version}-arm64
	docker manifest push ${repo}/joylive-demo-springcloud2023-consumer:${version}

image-joylive-demo-springcloud2023-provider:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-springcloud2023-provider:${version}-amd64 ./joylive-demo-springcloud2023/joylive-demo-springcloud2023-provider
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-springcloud2023-provider:${version}-arm64 ./joylive-demo-springcloud2023/joylive-demo-springcloud2023-provider

push-joylive-demo-springcloud2023-provider:
	docker push ${repo}/joylive-demo-springcloud2023-provider:${version}-amd64
	docker push ${repo}/joylive-demo-springcloud2023-provider:${version}-arm64
	docker manifest create ${repo}/joylive-demo-springcloud2023-provider:${version} \
	  ${repo}/joylive-demo-springcloud2023-provider:${version}-amd64 \
	  ${repo}/joylive-demo-springcloud2023-provider:${version}-arm64
	docker manifest push ${repo}/joylive-demo-springcloud2023-provider:${version}

image-joylive-demo-springcloud2024-consumer:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-springcloud2024-consumer:${version}-amd64 ./joylive-demo-springcloud2024/joylive-demo-springcloud2024-consumer
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-springcloud2024-consumer:${version}-arm64 ./joylive-demo-springcloud2024/joylive-demo-springcloud2024-consumer

push-joylive-demo-springcloud2024-consumer:
	docker push ${repo}/joylive-demo-springcloud2024-consumer:${version}-amd64
	docker push ${repo}/joylive-demo-springcloud2024-consumer:${version}-arm64
	docker manifest create ${repo}/joylive-demo-springcloud2024-consumer:${version} \
	  ${repo}/joylive-demo-springcloud2024-consumer:${version}-amd64 \
	  ${repo}/joylive-demo-springcloud2024-consumer:${version}-arm64
	docker manifest push ${repo}/joylive-demo-springcloud2024-consumer:${version}

image-joylive-demo-springcloud2024-provider:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-springcloud2024-provider:${version}-amd64 ./joylive-demo-springcloud2024/joylive-demo-springcloud2024-provider
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-springcloud2024-provider:${version}-arm64 ./joylive-demo-springcloud2024/joylive-demo-springcloud2024-provider

push-joylive-demo-springcloud2024-provider:
	docker push ${repo}/joylive-demo-springcloud2024-provider:${version}-amd64
	docker push ${repo}/joylive-demo-springcloud2024-provider:${version}-arm64
	docker manifest create ${repo}/joylive-demo-springcloud2024-provider:${version} \
	  ${repo}/joylive-demo-springcloud2024-provider:${version}-amd64 \
	  ${repo}/joylive-demo-springcloud2024-provider:${version}-arm64
	docker manifest push ${repo}/joylive-demo-springcloud2024-provider:${version}

image-joylive-demo-rocketmq:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-rocketmq:${version}-amd64 ./joylive-demo-rocketmq
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-rocketmq:${version}-arm64 ./joylive-demo-rocketmq

push-joylive-demo-rocketmq:
	docker push ${repo}/joylive-demo-rocketmq:${version}-amd64
	docker push ${repo}/joylive-demo-rocketmq:${version}-arm64
	docker manifest create ${repo}/joylive-demo-rocketmq:${version} \
	  ${repo}/joylive-demo-rocketmq:${version}-amd64 \
	  ${repo}/joylive-demo-rocketmq:${version}-arm64
	docker manifest push ${repo}/joylive-demo-rocketmq:${version}

image-joylive-demo-grpc-consumer:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-grpc-consumer:${version}-amd64 ./joylive-demo-grpc/joylive-demo-grpc-consumer
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-grpc-consumer:${version}-arm64 ./joylive-demo-grpc/joylive-demo-grpc-consumer

push-joylive-demo-grpc-consumer:
	docker push ${repo}/joylive-demo-grpc-consumer:${version}-amd64
	docker push ${repo}/joylive-demo-grpc-consumer:${version}-arm64
	docker manifest create ${repo}/joylive-demo-grpc-consumer:${version} \
	  ${repo}/joylive-demo-grpc-consumer:${version}-amd64 \
	  ${repo}/joylive-demo-grpc-consumer:${version}-arm64
	docker manifest push ${repo}/joylive-demo-grpc-consumer:${version}

image-joylive-demo-grpc-provider:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-grpc-provider:${version}-amd64 ./joylive-demo-grpc/joylive-demo-grpc-provider
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-grpc-provider:${version}-arm64 ./joylive-demo-grpc/joylive-demo-grpc-provider

push-joylive-demo-grpc-provider:
	docker push ${repo}/joylive-demo-grpc-provider:${version}-amd64
	docker push ${repo}/joylive-demo-grpc-provider:${version}-arm64
	docker manifest create ${repo}/joylive-demo-grpc-provider:${version} \
	  ${repo}/joylive-demo-grpc-provider:${version}-amd64 \
	  ${repo}/joylive-demo-grpc-provider:${version}-arm64
	docker manifest push ${repo}/joylive-demo-grpc-provider:${version}

image-joylive-demo-dubbo2.7-consumer:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-dubbo2.7-consumer:${version}-amd64 ./joylive-demo-dubbo2.7/joylive-demo-dubbo2.7-consumer
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-dubbo2.7-consumer:${version}-arm64 ./joylive-demo-dubbo2.7/joylive-demo-dubbo2.7-consumer

push-joylive-demo-dubbo2.7-consumer:
	docker push ${repo}/joylive-demo-dubbo2.7-consumer:${version}-amd64
	docker push ${repo}/joylive-demo-dubbo2.7-consumer:${version}-arm64
	docker manifest create ${repo}/joylive-demo-dubbo2.7-consumer:${version} \
	  ${repo}/joylive-demo-dubbo2.7-consumer:${version}-amd64 \
	  ${repo}/joylive-demo-dubbo2.7-consumer:${version}-arm64
	docker manifest push ${repo}/joylive-demo-dubbo2.7-consumer:${version}

image-joylive-demo-dubbo2.7-consumer-zookeeper:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-dubbo2.7-consumer-zookeeper:${version}-amd64 ./joylive-demo-dubbo2.7/joylive-demo-dubbo2.7-consumer-zookeeper
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-dubbo2.7-consumer-zookeeper:${version}-arm64 ./joylive-demo-dubbo2.7/joylive-demo-dubbo2.7-consumer-zookeeper

push-joylive-demo-dubbo2.7-consumer-zookeeper:
	docker push ${repo}/joylive-demo-dubbo2.7-consumer-zookeeper:${version}-amd64
	docker push ${repo}/joylive-demo-dubbo2.7-consumer-zookeeper:${version}-arm64
	docker manifest create ${repo}/joylive-demo-dubbo2.7-consumer-zookeeper:${version} \
	  ${repo}/joylive-demo-dubbo2.7-consumer-zookeeper:${version}-amd64 \
	  ${repo}/joylive-demo-dubbo2.7-consumer-zookeeper:${version}-arm64
	docker manifest push ${repo}/joylive-demo-dubbo2.7-consumer-zookeeper:${version}

image-joylive-demo-dubbo2.7-provider:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-dubbo2.7-provider:${version}-amd64 ./joylive-demo-dubbo2.7/joylive-demo-dubbo2.7-provider
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-dubbo2.7-provider:${version}-arm64 ./joylive-demo-dubbo2.7/joylive-demo-dubbo2.7-provider

push-joylive-demo-dubbo2.7-provider:
	docker push ${repo}/joylive-demo-dubbo2.7-provider:${version}-amd64
	docker push ${repo}/joylive-demo-dubbo2.7-provider:${version}-arm64
	docker manifest create ${repo}/joylive-demo-dubbo2.7-provider:${version} \
	  ${repo}/joylive-demo-dubbo2.7-provider:${version}-amd64 \
	  ${repo}/joylive-demo-dubbo2.7-provider:${version}-arm64
	docker manifest push ${repo}/joylive-demo-dubbo2.7-provider:${version}

image-joylive-demo-dubbo2.7-provider-zookeeper:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-dubbo2.7-provider-zookeeper:${version}-amd64 ./joylive-demo-dubbo2.7/joylive-demo-dubbo2.7-provider-zookeeper
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-dubbo2.7-provider-zookeeper:${version}-arm64 ./joylive-demo-dubbo2.7/joylive-demo-dubbo2.7-provider-zookeeper

push-joylive-demo-dubbo2.7-provider-zookeeper:
	docker push ${repo}/joylive-demo-dubbo2.7-provider-zookeeper:${version}-amd64
	docker push ${repo}/joylive-demo-dubbo2.7-provider-zookeeper:${version}-arm64
	docker manifest create ${repo}/joylive-demo-dubbo2.7-provider-zookeeper:${version} \
	  ${repo}/joylive-demo-dubbo2.7-provider-zookeeper:${version}-amd64 \
	  ${repo}/joylive-demo-dubbo2.7-provider-zookeeper:${version}-arm64
	docker manifest push ${repo}/joylive-demo-dubbo2.7-provider-zookeeper:${version}

image-joylive-demo-dubbo3-consumer:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-dubbo3-consumer:${version}-amd64 ./joylive-demo-dubbo3/joylive-demo-dubbo3-consumer
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-dubbo3-consumer:${version}-arm64 ./joylive-demo-dubbo3/joylive-demo-dubbo3-consumer

push-joylive-demo-dubbo3-consumer:
	docker push ${repo}/joylive-demo-dubbo3-consumer:${version}-amd64
	docker push ${repo}/joylive-demo-dubbo3-consumer:${version}-arm64
	docker manifest create ${repo}/joylive-demo-dubbo3-consumer:${version} \
	  ${repo}/joylive-demo-dubbo3-consumer:${version}-amd64 \
	  ${repo}/joylive-demo-dubbo3-consumer:${version}-arm64
	docker manifest push ${repo}/joylive-demo-dubbo3-consumer:${version}

image-joylive-demo-dubbo3-provider:
	docker build --platform linux/amd64 -t ${repo}/joylive-demo-dubbo3-provider:${version}-amd64 ./joylive-demo-dubbo3/joylive-demo-dubbo3-provider
	docker build --platform linux/arm64 -t ${repo}/joylive-demo-dubbo3-provider:${version}-arm64 ./joylive-demo-dubbo3/joylive-demo-dubbo3-provider

push-joylive-demo-dubbo3-provider:
	docker push ${repo}/joylive-demo-dubbo3-provider:${version}-amd64
	docker push ${repo}/joylive-demo-dubbo3-provider:${version}-arm64
	docker manifest create ${repo}/joylive-demo-dubbo3-provider:${version} \
	  ${repo}/joylive-demo-dubbo3-provider:${version}-amd64 \
	  ${repo}/joylive-demo-dubbo3-provider:${version}-arm64
	docker manifest push ${repo}/joylive-demo-dubbo3-provider:${version}