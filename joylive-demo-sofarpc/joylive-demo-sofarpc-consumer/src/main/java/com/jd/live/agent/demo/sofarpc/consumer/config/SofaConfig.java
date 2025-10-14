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
package com.jd.live.agent.demo.sofarpc.consumer.config;

import com.alipay.sofa.rpc.api.GenericService;
import com.alipay.sofa.rpc.boot.container.RegistryConfigContainer;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.jd.live.agent.demo.service.SleepService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SofaConfig {

    @Bean
    public SleepService sleepService(RegistryConfigContainer registry) {
        ConsumerConfig<SleepService> consumerConfig = new ConsumerConfig<SleepService>()
                .setInterfaceId(SleepService.class.getName())
                .setSerialization("hessian2")
                .setTimeout(50000)
                .setRepeatedReferLimit(0)
                .setRegistry(registry.getRegistryConfig());
        return consumerConfig.refer();
    }

    @Bean
    public SleepService asyncSleepService(RegistryConfigContainer registry) {
        ConsumerConfig<SleepService> consumerConfig = new ConsumerConfig<SleepService>()
                .setInterfaceId(SleepService.class.getName())
                .setSerialization("hessian2")
                .setTimeout(50000)
                .setRepeatedReferLimit(0)
                .setInvokeType("future")
                .setRegistry(registry.getRegistryConfig());
        return consumerConfig.refer();
    }

    @Bean
    public GenericService genericService(RegistryConfigContainer registry) {
        ConsumerConfig<GenericService> consumerConfig = new ConsumerConfig<GenericService>()
                .setInterfaceId(SleepService.class.getName())
                .setSerialization("hessian2")
                .setGeneric(true)
                .setTimeout(50000)
                .setRepeatedReferLimit(0)
                .setRegistry(registry.getRegistryConfig());
        return consumerConfig.refer();
    }

    @Bean
    public GenericService asyncGenericService(RegistryConfigContainer registry) {
        ConsumerConfig<GenericService> consumerConfig = new ConsumerConfig<GenericService>()
                .setInterfaceId(SleepService.class.getName())
                .setSerialization("hessian2")
                .setGeneric(true)
                .setTimeout(50000)
                .setRepeatedReferLimit(0)
                .setInvokeType("future")
                .setRegistry(registry.getRegistryConfig());
        return consumerConfig.refer();
    }

}
