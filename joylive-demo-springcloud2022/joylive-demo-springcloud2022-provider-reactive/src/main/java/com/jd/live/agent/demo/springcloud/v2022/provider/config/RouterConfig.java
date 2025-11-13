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
package com.jd.live.agent.demo.springcloud.v2022.provider.config;

import com.jd.live.agent.demo.response.LiveLocation;
import com.jd.live.agent.demo.response.LiveResponse;
import com.jd.live.agent.demo.response.LiveTrace;
import com.jd.live.agent.demo.response.LiveTransmission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class RouterConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions.route()
                .resources("/static/**", new ClassPathResource("static/"))
                .GET("/echo-func/{str}", request -> echoFunc(request))
                .GET("/status-func/{code}", request -> statusFunc(request))
                .onError(Throwable.class, this::onException)
                .build();
    }

    private Mono<ServerResponse> statusFunc(ServerRequest request) {
        Integer code = Integer.valueOf(request.pathVariable("code"));
        return ServerResponse.status(code).bodyValue(LiveResponse.builder().code(code).data(code).trace(trace(request)).build());
    }

    private Mono<ServerResponse> echoFunc(ServerRequest request) {
        return ServerResponse.ok().bodyValue(LiveResponse.builder().code(200).data(request.pathVariable("str")).trace(trace(request)).build());
    }

    private Mono<ServerResponse> onException(Throwable e, ServerRequest request) {
        HttpStatusCode status = e instanceof ErrorResponseException ? ((ErrorResponseException) e).getStatusCode() : null;
        int code = status != null ? status.value() : HttpStatus.INTERNAL_SERVER_ERROR.value();

        ServerRequest.Headers headers = request.headers();
        LiveResponse response = new LiveResponse(code, "Internal Server Error: " + e.getMessage());
        response.addFirst(new LiveTrace(applicationName, LiveLocation.build(),
                LiveTransmission.build("header", headers::firstHeader)));

        return ServerResponse.ok().bodyValue(response);
    }

    private LiveTrace trace(ServerRequest request) {
        return LiveTrace.builder()
                .service(applicationName)
                .location(LiveLocation.build())
                .transmission(LiveTransmission.build("header", name -> request.headers().firstHeader(name)))
                .build();
    }
}
