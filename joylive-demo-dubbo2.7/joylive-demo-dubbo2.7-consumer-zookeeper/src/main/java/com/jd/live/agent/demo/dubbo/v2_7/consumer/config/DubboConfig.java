package com.jd.live.agent.demo.dubbo.v2_7.consumer.config;

import com.jd.live.agent.demo.service.SleepService;
import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class DubboConfig {

    @Bean
    public SleepService sleepService() {
        ReferenceConfig<SleepService> reference = new ReferenceConfig<>();
        reference.setInterface(SleepService.class);
        reference.setGroup("DEFAULT_GROUP");
        reference.setCheck(false);
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("echo");
        methodConfig.setTimeout(60000);
        reference.setMethods(Collections.singletonList(methodConfig));
        return reference.get();
    }

    @Bean
    public GenericService genericService() {
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setInterface("com.jd.live.agent.demo.service.SleepService");
        reference.setGroup("DEFAULT_GROUP");
        reference.setGeneric("true");
        reference.setCheck(false);
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("echo");
        methodConfig.setTimeout(60000);
        reference.setMethods(Collections.singletonList(methodConfig));
        return reference.get();
    }
}