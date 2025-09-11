/*
 * Copyright © ${year} ${owner} (${email})
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jd.live.agent.demo.springcloud.v2024.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.live.agent.demo.response.LiveLocation;
import com.jd.live.agent.demo.response.LiveResponse;
import com.jd.live.agent.demo.response.LiveTrace;
import com.jd.live.agent.demo.response.LiveTransmission;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class LiveGatewayFilter implements GlobalFilter, Ordered {

    @Value("${spring.application.name}")
    private String applicationName;

    private final ObjectMapper objectMapper;

    public LiveGatewayFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpResponse decorator = new LiveTraceDecorator(response, request);

        return chain.filter(exchange.mutate().response(decorator).build()).onErrorResume(throwable -> {
            if (response.isCommitted()) {
                return Mono.error(throwable);
            }
            LiveResponse liveResponse = new LiveResponse(LiveResponse.ERROR, throwable.getMessage());
            addTrace(liveResponse, request.getHeaders());
            try {
                byte[] data = objectMapper.writeValueAsBytes(liveResponse);
                response.getHeaders().setContentLength(data.length);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(data)));
            } catch (JsonProcessingException e) {
                return Mono.error(throwable);
            }
        });
    }

    private void addTrace(LiveResponse liveResponse, HttpHeaders headers) {
        liveResponse.addFirst(new LiveTrace(applicationName, LiveLocation.build(),
                LiveTransmission.build("header", headers::getFirst)));
    }

    /**
     * Decorates ServerHttpResponse to add live tracing functionality
     */
    private class LiveTraceDecorator extends ServerHttpResponseDecorator {
        private final ServerHttpRequest request;

        LiveTraceDecorator(ServerHttpResponse response, ServerHttpRequest request) {
            super(response);
            this.request = request;
        }

        @Override
        public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
            if (body instanceof Flux) {
                Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                    DataBufferFactory factory = bufferFactory();
                    DataBuffer join = factory.join(dataBuffers);
                    byte[] array = new byte[join.readableByteCount()];
                    join.read(array);
                    // must release the buffer
                    DataBufferUtils.release(join);
                    array = append(array);
                    return factory.wrap(array);
                }));
            }
            return super.writeWith(body);
        }

        /**
         * Appends trace data to response array
         *
         * @param array Original response array
         * @return Updated response array with trace data
         */
        private byte[] append(byte[] array) {
            HttpHeaders headers = request.getHeaders();
            HttpStatusCode status = getStatusCode();
            LiveResponse liveResponse = read(status, array);
            if (liveResponse != null) {
                try {
                    addTrace(liveResponse, headers);
                    array = objectMapper.writeValueAsBytes(liveResponse);
                    getHeaders().setContentLength(array.length);
                } catch (Throwable ignore) {
                }
            }
            return array;
        }

        /**
         * Reads and constructs LiveResponse from status and array
         *
         * @param status HTTP status
         * @param array  Response array
         * @return Constructed LiveResponse
         */
        private LiveResponse read(HttpStatusCode status, byte[] array) {
            if (status == HttpStatus.OK) {
                try {
                    return array.length > 0
                            ? objectMapper.readValue(array, LiveResponse.class)
                            : new LiveResponse(LiveResponse.SUCCESS, HttpStatus.OK.getReasonPhrase());
                } catch (Throwable e) {
                    return null;
                }
            } else {
                status = status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status;
                if (array.length > 0) {
                    return new LiveResponse(status.value(), new String(array));
                } else if (status instanceof HttpStatus) {
                    return new LiveResponse(status.value(), ((HttpStatus) status).getReasonPhrase());
                } else {
                    return new LiveResponse(status.value(), status.toString());
                }
            }
        }
    }
}
