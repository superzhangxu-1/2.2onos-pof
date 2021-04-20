/*
 * Copyright 2020-present Open Networking Foundation
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

package org.onosproject.net.pi.impl;

import com.google.common.collect.Maps;
import org.onosproject.net.flow.FlowRule;
import org.onosproject.net.pi.model.PiPipeconf;
import org.onosproject.net.pi.runtime.PiHandle;
import org.onosproject.net.pi.runtime.PiTableEntry;
import org.onosproject.net.pi.service.PiFlowRuleTranslator;
import org.onosproject.net.pi.service.PiTranslatedEntity;
import org.onosproject.net.pi.service.PiTranslationException;

import java.util.Map;
import java.util.Optional;

/**
 * Adapter implementation of PiFlowRuleTranslator.
 */
public class PiFlowRuleTranslatorAdapter implements PiFlowRuleTranslator {

    private final Map<PiHandle, PiTranslatedEntity<FlowRule, PiTableEntry>> store = Maps.newHashMap();

    @Override
    public PiTableEntry translate(FlowRule original, PiPipeconf pipeconf) throws PiTranslationException {
        // NOTE Passing device equals to null is meant only for testing purposes
        return PiFlowRuleTranslatorImpl.translate(original, pipeconf, null);
    }

    @Override
    public void learn(PiHandle handle, PiTranslatedEntity<FlowRule, PiTableEntry> entity) {
        store.put(handle, entity);
    }

    @Override
    public Optional<PiTranslatedEntity<FlowRule, PiTableEntry>> lookup(PiHandle handle) {
        return Optional.ofNullable(store.get(handle));
    }

    @Override
    public void forget(PiHandle handle) {
        store.remove(handle);
    }
}
