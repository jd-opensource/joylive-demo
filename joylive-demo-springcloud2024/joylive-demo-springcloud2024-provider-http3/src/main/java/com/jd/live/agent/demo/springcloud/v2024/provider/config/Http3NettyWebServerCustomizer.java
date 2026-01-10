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
package com.jd.live.agent.demo.springcloud.v2024.provider.config;

import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import reactor.netty.http.Http3SslContextSpec;
import reactor.netty.http.HttpProtocol;

import java.time.Duration;

@Component
class Http3NettyWebServerCustomizer implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

	@Override
	public void customize(NettyReactiveWebServerFactory factory) {
		factory.addServerCustomizers(server -> {
			SslBundle sslBundle = factory.getSslBundles().getBundle("server-http3");
			Http3SslContextSpec sslContextSpec =
					Http3SslContextSpec.forServer(sslBundle.getManagers().getKeyManagerFactory(), sslBundle.getKey().getPassword());

			return server
					// Configure HTTP/3 protocol
					.protocol(HttpProtocol.HTTP3)
					// Configure HTTP/3 SslContext
					.secure(spec -> spec.sslContext(sslContextSpec))
					// Configure HTTP/3 settings
					.http3Settings(spec -> spec.idleTimeout(Duration.ofSeconds(5))
							.maxData(10_000_000)
							.maxStreamDataBidirectionalRemote(1_000_000)
							.maxStreamsBidirectional(100));
		});
	}
}