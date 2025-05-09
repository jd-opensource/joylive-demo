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
package com.jd.live.agent.demo.dubbo.v2_7.provider.service;

import com.jd.live.agent.demo.dubbo.v2_7.provider.config.EchoConfig;
import com.jd.live.agent.demo.exception.BreakableException;
import com.jd.live.agent.demo.exception.RetryableException;
import com.jd.live.agent.demo.response.LiveLocation;
import com.jd.live.agent.demo.response.LiveResponse;
import com.jd.live.agent.demo.response.LiveTrace;
import com.jd.live.agent.demo.response.LiveTransmission;
import com.jd.live.agent.demo.service.SleepService;
import com.jd.live.agent.demo.util.CpuBusyUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.ThreadLocalRandom;

@DubboService(group = "DEFAULT_GROUP", interfaceClass = SleepService.class)
public class Dubbo27Service implements SleepService {

    private final String applicationName;

    private final EchoConfig config;

    @Value("${echo.suffix}")
    private String echoSuffix;

    @Value("${mock.cpuPercent:0.2}")
    private double cpuPercent;

    public Dubbo27Service(@Value("${spring.application.name}") String applicationName, EchoConfig config) {
        this.applicationName = applicationName;
        this.config = config;
    }

    @Override
    public LiveResponse echo(String str) {
        int sleepTime = config.getSleepTime();
        if (sleepTime > 0) {
            if (config.getRandomTime() > 0) {
                sleepTime = sleepTime + ThreadLocalRandom.current().nextInt(config.getRandomTime());
            }
            CpuBusyUtil.busyCompute(sleepTime);
        }
        String value = str + "-sleepTime-" + config.getSleepTime() + "-cpuPercent-" + cpuPercent;
        String suffix = config.getSuffix();
        if (suffix != null && !suffix.isEmpty()) {
            value = value + "-" + suffix;
        }
        return createResponse(value);
    }

    @Override
    public LiveResponse status(int code) {
        if (code == 600) {
            if (ThreadLocalRandom.current().nextInt(2) == 0) {
                throw new RetryableException("Code:" + code);
            }
        } else if (code >= 500) {
            throw new BreakableException("Code:" + code);
        }
        return createResponse(code);
    }

    @Override
    public LiveResponse sleep(int millis) {
        if (millis > 0) {
            CpuBusyUtil.busyCompute(millis);
        }
        return createResponse(null);
    }

    private LiveResponse createResponse(Object data) {
        RpcContext context = RpcContext.getContext();
        return new LiveResponse(data).addFirst(new LiveTrace(applicationName, LiveLocation.build(),
                LiveTransmission.build("attachment", context::getAttachment)));
    }

}
