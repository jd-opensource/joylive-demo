package com.jd.live.agent.demo.springcloud.v2021.provider.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI Configuration for JoyLive Demo SpringCloud 2021 Provider
 * This configuration only sets up API metadata and relies on Spring Web's existing server
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("JoyLive Demo SpringCloud 2021 Provider API")
                .version("1.0.0")
                .description("JoyLive Demo SpringCloud 2021 Provider Service API Documentation"));
        // No server configuration - SpringDoc will automatically use the existing Spring Web server
    }
}