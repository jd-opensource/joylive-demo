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
package com.jd.live.agent.demo.springcloud.v2025.gateway.filter;

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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
            int code = throwable instanceof ResponseStatusException ? ((ResponseStatusException) throwable).getStatusCode().value() : LiveResponse.ERROR;
            LiveResponse liveResponse = new LiveResponse(code, throwable.getMessage());
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
                body = Flux.from(body).buffer().map(buffers -> readBody(buffers));
            } else if (body instanceof Mono) {
                body = Mono.from(body).map(buffers -> readBody(buffers));
            }
            return super.writeWith(body);
        }

        /**
         * Reads content from multiple DataBuffers by joining them into a single buffer.
         *
         * @param buffers the list of DataBuffers to read from
         * @return a new DataBuffer containing the joined content
         */
        private DataBuffer readBody(List<? extends DataBuffer> buffers) {
            DataBufferFactory factory = bufferFactory();
            return readBody(factory, factory.join(buffers));
        }

        /**
         * Reads content from a DataBuffer using the default buffer factory.
         *
         * @param buffer the DataBuffer to read from
         * @return a new DataBuffer containing the content
         */
        private DataBuffer readBody(DataBuffer buffer) {
            return readBody(bufferFactory(), buffer);
        }

        /**
         * Reads content from a DataBuffer using the default buffer factory.
         *
         * @param buffer the DataBuffer to read from
         * @return a new DataBuffer containing the content
         */
        private DataBuffer readBody(DataBufferFactory factory, DataBuffer buffer) {
            byte[] content = new byte[buffer.readableByteCount()];
            try {
                buffer.read(content);
            } finally {
                // must release the buffer
                DataBufferUtils.release(buffer);
            }
            return factory.wrap(append(content));
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
                if (array.length > 0 && array[0] == '{' && array[array.length - 1] == '}') {
                    try {
                        return objectMapper.readValue(array, LiveResponse.class);
                    } catch (Exception e) {
                        return new LiveResponse(LiveResponse.ERROR, new String(array));
                    }
                } else if (array.length > 0) {
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
