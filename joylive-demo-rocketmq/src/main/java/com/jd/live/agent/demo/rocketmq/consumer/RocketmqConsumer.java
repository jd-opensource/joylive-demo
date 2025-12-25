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
package com.jd.live.agent.demo.rocketmq.consumer;

import com.jd.live.agent.demo.response.LiveLocation;
import com.jd.live.agent.demo.response.LiveResponse;
import com.jd.live.agent.demo.response.LiveTrace;
import com.jd.live.agent.demo.response.LiveTransmission;
import com.jd.live.agent.demo.rocketmq.service.ConsumerService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@RocketMQMessageListener(topic = "${rocketmq.topic}", consumerGroup = "${rocketmq.consumer.group}")
public class RocketmqConsumer implements RocketMQReplyListener<MessageExt, LiveResponse> {

    private final ConsumerService consumerService;

    @Value("${spring.application.name}")
    private String applicationName;

    public RocketmqConsumer(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @Override
    public LiveResponse onMessage(MessageExt message) {
        Map<String, String> properties = message.getProperties();
        try {
            LiveResponse response = consumerService.echo(new String(message.getBody(), StandardCharsets.UTF_8));
            response.addFirst(new LiveTrace(applicationName + "-consumer", LiveLocation.build(),
                    LiveTransmission.build("properties", properties::get)));
            return response;
        } catch (Throwable e) {
            return new LiveResponse(LiveResponse.ERROR, e.getMessage());
        }
    }
}
