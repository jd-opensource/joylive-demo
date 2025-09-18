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
package com.jd.live.agent.demo.springcloud.v2021.gateway.controller;

import com.jd.live.agent.demo.response.LiveResponse;
import com.jd.live.agent.demo.util.CpuBusyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class EchoController {

    private final static Logger logger = LoggerFactory.getLogger(EchoController.class);

    @GetMapping("/echo/{str}")
    public Mono<LiveResponse> echo(@PathVariable String str,
                                   @RequestParam(required = false) Integer time,
                                   @RequestParam(required = false) String name) {
        int sleepTime = time != null && time > 0 ? time : 0;
        if (sleepTime > 0) {
            CpuBusyUtil.busyCompute(sleepTime);
        }
        LiveResponse response = new LiveResponse(str);
        if (logger.isDebugEnabled()) {
            logger.info("echo str: {}, time: {}", str, System.currentTimeMillis());
        }
        return Mono.just(response);
    }

}
