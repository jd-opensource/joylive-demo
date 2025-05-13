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
package com.jd.live.agent.demo.dubbo.v2_7.consumer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.live.agent.demo.response.LiveLocation;
import com.jd.live.agent.demo.response.LiveResponse;
import com.jd.live.agent.demo.response.LiveTrace;
import com.jd.live.agent.demo.response.LiveTransmission;
import com.jd.live.agent.demo.service.SleepService;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class EchoController {

    @Resource
    private ObjectMapper objectMapper;

    @Value("${spring.application.name}")
    private String applicationName;

    @Resource
    private SleepService sleepService;

    @Resource
    private GenericService genericService;

    @GetMapping("/echo/{str}")
    public LiveResponse echo(@PathVariable String str, HttpServletRequest request) {
        LiveResponse response = sleepService.echo(str);
        addTrace(request, response);
        return response;
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/echo-generic/{str}")
    public LiveResponse echoGeneric(@PathVariable String str, HttpServletRequest request) {
        Map<String, Object> result = (Map<String, Object>) genericService.$invoke("echo",
                new String[]{"java.lang.String"},
                new Object[]{str});
        LiveResponse response = objectMapper.convertValue(result, LiveResponse.class);
        addTrace(request, response);
        return response;
    }

    @GetMapping("/status/{code}")
    public LiveResponse statusF(@PathVariable int code, HttpServletRequest request) {
        LiveResponse response = sleepService.status(code);
        addTrace(request, response);
        return response;
    }

    @GetMapping({"/sleep/{millis}"})
    public LiveResponse sleep(@PathVariable int millis, HttpServletRequest request) {
        LiveResponse response = sleepService.sleep(millis);
        addTrace(request, response);
        return response;
    }

    private void addTrace(HttpServletRequest request, LiveResponse response) {
        if (response != null) {
            response.addFirst(new LiveTrace(applicationName, LiveLocation.build(),
                    LiveTransmission.build("header", request::getHeader)));
        }
    }

}
