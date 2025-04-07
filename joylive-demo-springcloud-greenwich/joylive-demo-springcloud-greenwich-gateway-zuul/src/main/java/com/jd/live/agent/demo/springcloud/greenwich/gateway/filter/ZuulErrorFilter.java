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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ZuulErrorFilter extends ZuulFilter {

    @Value("${spring.application.name}")
    private String applicationName;

    private final ObjectMapper objectMapper;

    public ZuulErrorFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getThrowable() != null;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        if (throwable != null) {
            try {
                LiveResponse liveResponse = new LiveResponse(LiveResponse.ERROR, throwable.getMessage());
                addTrace(liveResponse, ctx.getResponse());
                String responseBody = objectMapper.writeValueAsString(liveResponse);
                ctx.remove("throwable");
                ctx.setResponseStatusCode(200);
                ctx.setResponseBody(responseBody);
                ctx.sendZuulResponse();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    private void addTrace(LiveResponse liveResponse, HttpServletResponse response) {
        liveResponse.addFirst(new LiveTrace(applicationName, LiveLocation.build(),
                LiveTransmission.build("header", response::getHeader)));
    }

}
