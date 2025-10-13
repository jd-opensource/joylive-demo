package com.jd.live.agent.demo.dubbo.v2_7.consumer.config;

import com.jd.live.agent.demo.service.AsyncSleepService;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboConfig {

    @Bean
    public AsyncSleepService sleepService() {
        ReferenceConfig<AsyncSleepService> reference = new ReferenceConfig<>();
        reference.setInterface(AsyncSleepService.class);
        reference.setGroup("DEFAULT_GROUP");
        reference.setCheck(false);
        reference.setTimeout(10000);
        return reference.get();
    }

    @Bean
    public GenericService genericService() {
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setInterface("com.jd.live.agent.demo.service.AsyncSleepService");
        reference.setGroup("DEFAULT_GROUP");
        reference.setGeneric("true");
        reference.setCheck(false);
        reference.setTimeout(10000);
        return reference.get();
    }
}