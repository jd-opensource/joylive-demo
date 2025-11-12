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
import com.jd.live.agent.demo.springcloud.v2021.provider.config.EchoConfig;
import com.jd.live.agent.demo.util.CpuBusyUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@Tag(name = "EchoController", description = "Echo service controller providing various testing and echo functionalities")
public class EchoController {

    private final String applicationName;

    private final EchoConfig config;

    private final static Logger logger = LoggerFactory.getLogger(EchoController.class);

    @Value("${echo.suffix}")
    private String echoSuffix;

    @Value("${mock.cpuPercent:0.2}")
    private double cpuPercent;

    public EchoController(@Value("${spring.application.name}") String applicationName, EchoConfig config) {
        this.applicationName = applicationName;
        this.config = config;
    }

    @GetMapping("/echo/{str}")
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
    public LiveResponse echo(@PathVariable String str,
                             @RequestParam(required = false) Integer time,
                             @RequestParam(required = false, defaultValue = "tester") String name,
                             HttpServletRequest request) {
        int sleepTime = time != null && time > 0 ? time : config.getSleepTime();
        if (sleepTime > 0) {
            if (config.getRandomTime() > 0) {
                sleepTime = sleepTime + ThreadLocalRandom.current().nextInt(config.getRandomTime());
            }
            CpuBusyUtil.busyCompute(sleepTime);
        }
        String value = str + "-sleepTime-" + sleepTime + "-cpuPercent-" + cpuPercent + "-name-" + name;
        String suffix = config.getSuffix();
        if (suffix != null && !suffix.isEmpty()) {
            value = value + "-" + suffix;
        }
        LiveResponse response = new LiveResponse(value);
        configure(request, response);
        if (logger.isDebugEnabled()) {
            logger.info("echo str: {}, time: {}", str, System.currentTimeMillis());
        }
        return response;
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
    public LiveResponse status(@PathVariable int code, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(code);
        LiveResponse lr = new LiveResponse(code, null, code);
        configure(request, lr);
        if (logger.isDebugEnabled()) {
            logger.info("status code: {}, time: {}", code, System.currentTimeMillis());
        }
        return lr;
    }

    @RequestMapping(value = "/sleep/{millis}", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
    @Operation(
            summary = "Sleep Test",
            description = "Simulates delay processing for specified time, used for testing timeout and performance",
            parameters = {
                    @Parameter(name = "millis", description = "Delay time in milliseconds", required = true, example = "1000")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully completed delay processing",
                    content = @Content(schema = @Schema(implementation = LiveResponse.class))),
    })
    public LiveResponse sleep(@PathVariable int millis, HttpServletRequest request, HttpServletResponse response) {
        if (millis > 0) {
            CpuBusyUtil.busyCompute(millis);
        }
        LiveResponse lr = new LiveResponse(200, null, millis);
        configure(request, lr);
        return lr;
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
    public LiveResponse exception(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.info("exception at time: {}", System.currentTimeMillis());
        }
        throw new RuntimeException("RuntimeException happened!");
    }

    @RequestMapping(value = "/state/{code}/sleep/{time}", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
    @Operation(
            summary = "Status Code and Sleep Combination Test",
            description = "Combined test interface for status code and delay time, used for testing complex response scenarios",
            parameters = {
                    @Parameter(name = "code", description = "HTTP status code", required = true, example = "200"),
                    @Parameter(name = "time", description = "Processing time in milliseconds", required = true, example = "1000")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed",
                    content = @Content(schema = @Schema(implementation = String.class))),
    })
    public String state(@PathVariable int code, @PathVariable int time, HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
        if (logger.isDebugEnabled()) {
            logger.info("state code: {}, sleep time: {}, date: {}", code, time, System.currentTimeMillis());
        }
        if (code <= 0) {
            throw new RuntimeException("RuntimeException happened!");
        }
        if (code > 600) {
            response.setStatus(500);
        } else {
            response.setStatus(code);
        }
        double result = 0;
        if (time > 0) {
            long cpuTime = (long) (time * cpuPercent);
            result = CpuBusyUtil.busyCompute(cpuTime);
            Thread.sleep(time - cpuTime);
        }
        LiveResponse lr = new LiveResponse(code, "code:" + code + ", result: " + result, code);
        configure(request, lr);
        return lr.toString();
    }

    private void configure(HttpServletRequest request, LiveResponse response) {
        response.addFirst(new LiveTrace(applicationName, LiveLocation.build(),
                LiveTransmission.build("header", request::getHeader)));
    }

}
