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
package com.jd.live.agent.demo.multilive.service;

import com.jd.live.agent.demo.multilive.entity.Workspace;
import com.jd.live.agent.governance.policy.live.LiveSpace;
import com.jd.live.agent.governance.policy.live.db.LiveDatabaseSpec;
import com.jd.live.agent.governance.policy.service.Service;

import java.util.List;

/**
 * Provides access to live (real-time) services and resources in a distributed system.
 */
public interface LiveService {
    /**
     * Retrieves a service instance by name.
     *
     * @param name The service identifier
     * @return The Service instance, or null if not found
     */
    Service getService(String name);

    /**
     * Gets a live space (isolated runtime environment) by ID.
     * @param id The space identifier
     * @return The LiveSpace instance, or null if not found
     */
    LiveSpace getLiveSpace(String id);

    /**
     * Retrieves the database specification for a live resource.
     *
     * @param id The specification identifier
     * @return The LiveDatabaseSpec, or null if not found
     */
    LiveDatabaseSpec getLiveDatabaseSpec(String id);

    /**
     * Lists all available active workspaces.
     * @return Immutable list of Workspace instances (empty if none)
     */
    List<Workspace> getLiveSpaces();
}
