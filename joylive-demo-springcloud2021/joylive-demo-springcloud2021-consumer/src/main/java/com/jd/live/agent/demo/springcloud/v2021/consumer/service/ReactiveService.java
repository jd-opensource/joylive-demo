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
package com.jd.live.agent.demo.springcloud.v2021.consumer.service;

import com.jd.live.agent.demo.response.LiveResponse;
import com.jd.live.agent.demo.service.HelloService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

@Service
public class ReactiveService implements HelloService {

    private final WebClient cloudWebClient;

    private final WebClient restWebClient;

    public ReactiveService(WebClient.Builder cloudWebClientBuilder,
                           WebClient.Builder restWebClientBuilder) {
        this.cloudWebClient = cloudWebClientBuilder.baseUrl("http://service-provider-reactive").build();
        this.restWebClient = restWebClientBuilder.build();
    }

    @Override
    public LiveResponse echo(String str) {
        return cloudWebClient.get()
                .uri("/echo/" + str)
                .retrieve()
                .bodyToMono(LiveResponse.class).block();
    }

    @Override
    public LiveResponse status(int code) {
        return cloudWebClient.get()
                .uri("/status/" + code)
                .retrieve()
                .bodyToMono(LiveResponse.class).block();
    }

    public String state(int code, int time) {
        return cloudWebClient.get()
                .uri("/state/" + code + "/sleep/" + time)
                .retrieve()
                .bodyToMono(String.class).block();
    }

    public String proxy(String url) {
        URI uri = URI.create(url);
        String result = restWebClient.get().uri(uri).retrieve().bodyToMono(String.class).block();
        return result;
    }
}
