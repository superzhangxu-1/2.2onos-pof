/*
 * Copyright 2014-present Open Networking Laboratory
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
package org.onosproject.net.table;

import org.onosproject.net.DeviceId;

import java.util.Collection;

@Deprecated
/**
 * Class used with the flow subsystem to process per device batches.
 *
 * @deprecated in Drake release - no longer a public API
 */
public class FlowTableBatchOperation
    extends BatchOperation<FlowTableBatchEntry> {

    /**
     * This id is used to cary to id of the original FlowOperations and
     * track where this batch operation came from. The id is unique cluster wide.
     *此id用于查询原始FlowOperations的id，并跟踪此批操作来自何处。该id在整个集群范围内是唯一的。
     */
    private final long id;
    private final DeviceId deviceId;

    public FlowTableBatchOperation(Collection<FlowTableBatchEntry> operations,
                                   DeviceId deviceId, long flowOperationId) {
        super(operations);
        this.id = flowOperationId;
        this.deviceId = deviceId;
    }

    public DeviceId deviceId() {
        return this.deviceId;
    }

    public long id() {
        return id;
    }
}
