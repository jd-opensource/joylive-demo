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
package com.jd.live.agent.demo.springcloud.v2020.provider.config;

import com.jd.live.agent.demo.response.LiveLocation;
import com.jd.live.agent.demo.response.LiveResponse;
import com.jd.live.agent.demo.response.LiveTrace;
import com.jd.live.agent.demo.response.LiveTransmission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class RouterConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions.route()
                .GET("/echo-func/{str}", request -> echoFunc(request))
                .GET("/status-func/{code}", request -> statusFunc(request))
                .build();
    }

    private ServerResponse statusFunc(ServerRequest request) {
        Integer code = Integer.valueOf(request.pathVariable("code"));
        return ServerResponse.status(code).body(LiveResponse.builder().code(code).data(code).trace(trace(request)).build());
    }

    private ServerResponse echoFunc(ServerRequest request) {
        return ServerResponse.ok().body(LiveResponse.builder().code(200).data(request.pathVariable("str")).trace(trace(request)).build());
    }

    private LiveTrace trace(ServerRequest request) {
        return LiveTrace.builder()
                .service(applicationName)
                .location(LiveLocation.build())
                .transmission(LiveTransmission.build("header", name -> request.headers().firstHeader(name)))
                .build();
    }
}
