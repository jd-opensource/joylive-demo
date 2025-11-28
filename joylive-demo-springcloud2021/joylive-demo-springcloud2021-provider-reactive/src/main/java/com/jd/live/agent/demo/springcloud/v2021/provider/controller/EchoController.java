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
package com.jd.live.agent.demo.springcloud.v2021.provider.controller;

import com.jd.live.agent.demo.response.LiveLocation;
import com.jd.live.agent.demo.response.LiveResponse;
import com.jd.live.agent.demo.response.LiveTrace;
import com.jd.live.agent.demo.response.LiveTransmission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.CountDownLatch;

@RestController
public class EchoController {

    @Value("${spring.application.name}")
    private String applicationName;

    private final CountDownLatch latch = new CountDownLatch(1);

    @Operation(
            summary = "Echo method",
            description = "Receives string parameter and returns echo response, supports custom processing time and name parameters",
            parameters = {
                    @Parameter(name = "str", description = "String to echo", required = true, example = "hello"),
                    @Parameter(name = "time", description = "Processing time in milliseconds", example = "1000"),
                    @Parameter(name = "name", description = "Caller name", example = "tester")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned echo response",
                    content = @Content(schema = @Schema(implementation = LiveResponse.class))),
    })
    @GetMapping("/echo/{str}")
    public Mono<LiveResponse> echo(@PathVariable String str, ServerWebExchange exchange) {
        return Mono.fromSupplier(() -> {
            LiveResponse response = new LiveResponse(str);
            configure(exchange, response);
            return response;
        });
    }

    @RequestMapping(value = "/status/{code}", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
    @Operation(
            summary = "Status Code Test",
            description = "Returns response with specified HTTP status code for testing different HTTP status code handling",
            parameters = {
                    @Parameter(name = "code", description = "HTTP status code", required = true, example = "200")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned specified status code",
                    content = @Content(schema = @Schema(implementation = LiveResponse.class))),
    })
    @GetMapping("/status/{code}")
    public Mono<LiveResponse> status(@PathVariable int code, ServerWebExchange exchange) {
        return Mono.defer(() -> {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            try {
                status = HttpStatus.valueOf(code);
            } catch (Exception ignored) {
            }
            exchange.getResponse().setStatusCode(status);
            LiveResponse response = new LiveResponse(code, null, code);
            configure(exchange, response);
            return Mono.just(response);
        });
    }

    @RequestMapping(value = "/exception", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
    @Operation(
            summary = "Exception Test",
            description = "Deliberately throws runtime exception for testing exception handling mechanism"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully completed delay processing",
                    content = @Content(schema = @Schema(implementation = LiveResponse.class))),
    })
    @GetMapping("/exception")
    public Mono<LiveResponse> exception(ServerWebExchange exchange) {
        return Mono.error(new RuntimeException("exception"));
    }

    private void configure(ServerWebExchange exchange, LiveResponse response) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        response.addFirst(new LiveTrace(applicationName, LiveLocation.build(),
                LiveTransmission.build("header", headers::getFirst)));
    }

}
