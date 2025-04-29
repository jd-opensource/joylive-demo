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
package com.jd.live.agent.demo.springcloud.v2021.product.controller;

import com.jd.live.agent.demo.response.LiveLocation;
import com.jd.live.agent.demo.response.LiveResponse;
import com.jd.live.agent.demo.response.LiveTrace;
import com.jd.live.agent.demo.response.LiveTransmission;
import com.jd.live.agent.demo.springcloud.v2021.product.entity.Product;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Value("${spring.application.name}")
    private String applicationName;

    private final MongoTemplate mono;

    public ProductController(MongoTemplate mono) {
        this.mono = mono;
    }

    @GetMapping("/{id}")
    public LiveResponse getProductById(@PathVariable Long id, HttpServletRequest request) {
        Product product = mono.findOne(Query.query(Criteria.where("id").is(id)), Product.class);
        LiveResponse response = product == null
                ? new LiveResponse(LiveResponse.NOT_FOUND, "NOT_FOUND")
                : new LiveResponse(product);
        response.addFirst(new LiveTrace(applicationName, LiveLocation.build(),
                LiveTransmission.build("header", request::getHeader)));
        return response;
    }

    @PostMapping
    public LiveResponse addProduct(@RequestBody Product product, HttpServletRequest request) {
        mono.insert(product);
        LiveResponse response = new LiveResponse(LiveResponse.SUCCESS, "SUCCESS");
        response.addFirst(new LiveTrace(applicationName, LiveLocation.build(),
                LiveTransmission.build("header", request::getHeader)));
        return response;
    }

    @PutMapping("/{id}")
    public LiveResponse updateProduct(@PathVariable Long id, @RequestBody Product product, HttpServletRequest request) {
        product.setId(id);
        UpdateResult result = mono.updateFirst(Query.query(Criteria.where("id").is(id)),
                Update.update("quantity", product.getQuantity()).set("price", product.getPrice()), Product.class);
        LiveResponse response = result.getMatchedCount() == 0
                ? new LiveResponse(LiveResponse.NOT_FOUND, "NOT_FOUND")
                : new LiveResponse(LiveResponse.SUCCESS, "SUCCESS");
        response.addFirst(new LiveTrace(applicationName, LiveLocation.build(),
                LiveTransmission.build("header", request::getHeader)));
        return response;
    }

    @DeleteMapping("/{id}")
    public LiveResponse deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        DeleteResult result = mono.remove(Query.query(Criteria.where("id").is(id)), Product.class);
        LiveResponse response = result.getDeletedCount() == 0
                ? new LiveResponse(LiveResponse.NOT_FOUND, "NOT_FOUND")
                : new LiveResponse(LiveResponse.SUCCESS, "SUCCESS");
        response.addFirst(new LiveTrace(applicationName, LiveLocation.build(),
                LiveTransmission.build("header", request::getHeader)));
        return response;
    }
}
