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
package com.jd.live.agent.demo.springcloud.v2025.consumer.service;

import com.jd.live.agent.demo.response.LiveResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-provider")
public interface FeignService {

    @GetMapping("/echo/{str}")
    LiveResponse echo(@PathVariable("str") String str);

    @GetMapping("/status/{code}")
    LiveResponse status(@PathVariable("code") int code);

    @GetMapping("/state/{code}/sleep/{time}")
    String state(@PathVariable("code") int code, @PathVariable("time") int time);


}
