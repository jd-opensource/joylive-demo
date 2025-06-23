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
package com.jd.live.agent.demo.multilive.repository;

import com.jd.live.agent.demo.multilive.entity.Workspace;
import com.jd.live.agent.governance.policy.live.LiveSpace;
import com.jd.live.agent.governance.policy.live.db.LiveDatabaseSpec;
import com.jd.live.agent.governance.policy.service.Service;

import java.util.List;

/**
 * Repository pattern interface for accessing live (real-time) distributed resources.
 */
public interface LiveRepository {
    /**
     * Finds a service by its unique name.
     *
     * @param name service identifier
     * @return Service instance or null if not found
     */
    Service getService(String name);

    /**
     * Retrieves a live space by ID.
     * @param id space identifier
     * @return LiveSpace instance or null if not found
     */
    LiveSpace getLiveSpace(String id);

    /**
     * Gets database specifications for a live resource.
     *
     * @param id spec identifier
     * @return LiveDatabaseSpec or null if not found
     */
    LiveDatabaseSpec getLiveDatabaseSpec(String id);

    /**
     * Lists all active workspaces.
     * @return immutable list of Workspace (empty if none)
     */
    List<Workspace> getLiveSpaces();
}
