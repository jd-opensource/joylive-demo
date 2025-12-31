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
package com.jd.live.agent.demo.springcloud.v2024.gateway.config;

import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.HttpProtocol;

import java.time.Duration;

@Configuration
class GatewayConfiguration {

	@Bean
	HttpClientCustomizer http3HttpClientCustomizer() {
		return httpClient ->
				httpClient
						// Configure HTTP/3 protocol
						.protocol(HttpProtocol.HTTP3)
						// Configure HTTP/3 settings
						.http3Settings(spec -> spec.idleTimeout(Duration.ofSeconds(5))
								.maxData(10_000_000)
								.maxStreamDataBidirectionalLocal(1_000_000));
	}
}