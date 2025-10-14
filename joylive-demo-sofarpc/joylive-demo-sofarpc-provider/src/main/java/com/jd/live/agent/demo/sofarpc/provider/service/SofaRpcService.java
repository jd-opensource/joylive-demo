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
package com.jd.live.agent.demo.sofarpc.provider.service;

import com.alipay.sofa.rpc.context.RpcInvokeContext;
import com.alipay.sofa.runtime.api.annotation.SofaService;
import com.alipay.sofa.runtime.api.annotation.SofaServiceBinding;
import com.jd.live.agent.demo.exception.BreakableException;
import com.jd.live.agent.demo.exception.RetryableException;
import com.jd.live.agent.demo.response.LiveLocation;
import com.jd.live.agent.demo.response.LiveResponse;
import com.jd.live.agent.demo.response.LiveTrace;
import com.jd.live.agent.demo.response.LiveTransmission;
import com.jd.live.agent.demo.service.SleepService;
import com.jd.live.agent.demo.sofarpc.provider.config.EchoConfig;
import com.jd.live.agent.demo.util.CpuBusyUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SofaRpcProviderService
 *
 * @author yuanjinzhong
 */
@SofaService(interfaceType = SleepService.class, bindings = {@SofaServiceBinding(bindingType = "bolt", serializeType = "hessian2")})
@Service
public class SofaRpcService implements SleepService {

    private final static AtomicLong COUNTER = new AtomicLong(0);

    private final String applicationName;

    private final EchoConfig config;

    @Value("${echo.suffix}")
    private String echoSuffix;

    @Value("${mock.cpuPercent:0.2}")
    private double cpuPercent;

    public SofaRpcService(@Value("${spring.application.name}") String applicationName, EchoConfig config) {
        this.applicationName = applicationName;
        this.config = config;
    }

    @Override
    public LiveResponse echo(String str) {
        sleep(config.getSleepTime(), config.getRandomTime());
        return doEcho(str);
    }

    @Override
    public LiveResponse status(int code) {
        sleep(config.getSleepTime(), config.getRandomTime());
        return doStatus(code);
    }

    @Override
    public LiveResponse sleep(int millis) {
        sleep(millis, 0);
        return createResponse(null);
    }

    private LiveResponse doEcho(String str) {
        String value = str + "-sleepTime-" + config.getSleepTime() + "-cpuPercent-" + cpuPercent;
        String suffix = config.getSuffix();
        if (suffix != null && !suffix.isEmpty()) {
            value = value + "-" + suffix;
        }
        return createResponse(value);
    }

    private LiveResponse doStatus(int code) {
        if (code == 600) {
            if (ThreadLocalRandom.current().nextInt(2) == 0) {
                throw new RetryableException("Code:" + code);
            }
        } else if (code >= 500) {
            throw new BreakableException("Code:" + code);
        }
        return createResponse(code);
    }

    private void sleep(int time, int random) {
        if (time > 0) {
            if (random > 0) {
                time = time + ThreadLocalRandom.current().nextInt(random);
            }
            CpuBusyUtil.busyCompute(time);
        }
    }

    private LiveResponse createResponse(Object data) {
        //sofa-rcp invoke-chain-pass-data ：https://www.sofastack.tech/projects/sofa-rpc/invoke-chain-pass-data/
        RpcInvokeContext context = RpcInvokeContext.getContext();
        return new LiveResponse(data).addFirst(new LiveTrace(applicationName, LiveLocation.build(),
                LiveTransmission.build("attachment", context::getRequestBaggage)));
    }
}
