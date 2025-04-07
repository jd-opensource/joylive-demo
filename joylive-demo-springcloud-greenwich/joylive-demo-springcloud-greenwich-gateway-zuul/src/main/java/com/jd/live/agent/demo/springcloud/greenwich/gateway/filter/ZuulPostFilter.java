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
package com.jd.live.agent.demo.springcloud.greenwich.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.live.agent.demo.response.LiveLocation;
import com.jd.live.agent.demo.response.LiveResponse;
import com.jd.live.agent.demo.response.LiveTrace;
import com.jd.live.agent.demo.response.LiveTransmission;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class ZuulPostFilter extends ZuulFilter {

    @Value("${spring.application.name}")
    private String applicationName;

    private final ObjectMapper objectMapper;

    public ZuulPostFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        try (InputStream responseStream = ctx.getResponseDataStream()) {
            String responseBody = StreamUtils.copyToString(responseStream, StandardCharsets.UTF_8);
            LiveResponse liveResponse = objectMapper.readValue(responseBody, LiveResponse.class);
            addTrace(liveResponse, ctx.getResponse());
            responseBody = objectMapper.writeValueAsString(liveResponse);
            ctx.setResponseBody(responseBody);
        } catch (IOException ignored) {
        }
        return null;
    }

    private void addTrace(LiveResponse liveResponse, HttpServletResponse response) {
        liveResponse.addFirst(new LiveTrace(applicationName, LiveLocation.build(),
                LiveTransmission.build("header", response::getHeader)));
    }

}
