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
package org.onosproject.kubevirtnetworking.cli;

import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.kubevirtnode.api.KubevirtNode;
import org.onosproject.kubevirtnode.api.KubevirtNodeAdminService;

import static java.lang.Thread.sleep;
import static org.onosproject.kubevirtnode.api.KubevirtNode.Type.WORKER;
import static org.onosproject.kubevirtnode.api.KubevirtNodeState.COMPLETE;
import static org.onosproject.kubevirtnode.api.KubevirtNodeState.INIT;

/**
 * Re-installs flow rules for KubeVirt networking.
 */
@Service
@Command(scope = "onos", name = "kubevirt-sync-rules",
        description = "Re-installs flow rules for KubeVirt networking")
public class KubevirtSyncRulesCommand extends AbstractShellCommand {

    private static final long SLEEP_MS = 5000; // we wait 5s for init each node
    private static final long TIMEOUT_MS = 10000; // we wait 10s

    private static final String SUCCESS_MSG = "Successfully synchronize flow rules for node %s!";
    private static final String FAIL_MSG = "Failed to synchronize flow rules for node %s.";

    @Override
    protected void doExecute() throws Exception {
        // All handlers in this application reacts the node complete event and
        // tries to re-configure flow rules for the complete node.
        KubevirtNodeAdminService nodeAdminService = get(KubevirtNodeAdminService.class);
        if (nodeAdminService == null) {
            error("Failed to re-install flow rules for OpenStack networking.");
            return;
        }

        nodeAdminService.completeNodes(WORKER).forEach(node ->
                syncRulesBaseForNode(nodeAdminService, node));

        print("Successfully requested re-installing flow rules.");
    }

    private void syncRulesBaseForNode(KubevirtNodeAdminService service, KubevirtNode node) {
        KubevirtNode updated = node.updateState(INIT);
        service.updateNode(updated);

        boolean result = true;
        long timeoutExpiredMs = System.currentTimeMillis() + TIMEOUT_MS;

        while (service.node(node.hostname()).state() != COMPLETE) {
            long  waitMs = timeoutExpiredMs - System.currentTimeMillis();

            try {
                sleep(SLEEP_MS);
            } catch (InterruptedException e) {
                error("Exception caused during node synchronization...");
            }

            if (service.node(node.hostname()).state() == COMPLETE) {
                break;
            } else {
                service.updateNode(updated);
                print("Failed to synchronize flow rules, retrying...");
            }

            if (waitMs <= 0) {
                result = false;
                break;
            }
        }

        if (result) {
            print(SUCCESS_MSG, node.hostname());
        } else {
            error(FAIL_MSG, node.hostname());
        }
    }
}
