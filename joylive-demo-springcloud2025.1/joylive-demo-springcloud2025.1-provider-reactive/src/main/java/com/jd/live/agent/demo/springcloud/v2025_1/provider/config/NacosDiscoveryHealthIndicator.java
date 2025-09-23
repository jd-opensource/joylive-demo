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
package com.jd.live.agent.demo.springcloud.v2025_1.provider.config;

import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.boot.health.contributor.AbstractHealthIndicator;
import org.springframework.boot.health.contributor.Health;

public class NacosDiscoveryHealthIndicator extends AbstractHealthIndicator {

    /**
     * status up.
     */
    private static final String STATUS_UP = "UP";

    /**
     * status down.
     */
    private static final String STATUS_DOWN = "DOWN";

    private NacosServiceManager nacosServiceManager;

    @Deprecated
    private NamingService namingService;

    public NacosDiscoveryHealthIndicator(NacosServiceManager nacosServiceManager) {
        this.nacosServiceManager = nacosServiceManager;
    }

    @Deprecated
    public NacosDiscoveryHealthIndicator(NamingService namingService) {
        this.namingService = namingService;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        // Just return "UP" or "DOWN"
        String status = nacosServiceManager.getNamingService().getServerStatus();
        // Set the status to Builder
        builder.status(status);
        if (STATUS_UP.equals(status)) {
            builder.up();
        } else if (STATUS_DOWN.equals(status)) {
            builder.down();
        } else {
            builder.unknown();
        }
    }

}
