/*
 * Copyright 2021-present Open Networking Foundation
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
package org.onosproject.kubevirtnetworking.api;

import org.onosproject.event.ListenerService;
import org.onosproject.kubevirtnetworking.api.KubevirtNetwork.Type;

import java.util.Set;

/**
 * Service for interacting with the inventory of kubevirt network.
 */
public interface KubevirtNetworkService
        extends ListenerService<KubevirtNetworkEvent, KubevirtNetworkListener> {

    /**
     * Returns the kubevirt network with the supplied network identifier.
     *
     * @param networkId network identifier
     * @return kubevirt network
     */
    KubevirtNetwork network(String networkId);

    /**
     * Returns all kubevirt networks registered in the service.
     *
     * @return set of kubevirt networks
     */
    Set<KubevirtNetwork> networks();

    /**
     * Returns the kubevirt networks with the given network type.
     *
     * @param type virtual network type
     * @return set of kubevirt networks
     */
    Set<KubevirtNetwork> networks(Type type);
}
