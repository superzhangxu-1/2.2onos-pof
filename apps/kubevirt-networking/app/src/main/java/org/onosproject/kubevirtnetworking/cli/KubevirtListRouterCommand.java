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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.kubevirtnetworking.api.KubevirtRouter;
import org.onosproject.kubevirtnetworking.api.KubevirtRouterService;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.onosproject.kubevirtnetworking.api.Constants.CLI_FLAG_LENGTH;
import static org.onosproject.kubevirtnetworking.api.Constants.CLI_IP_ADDRESSES_LENGTH;
import static org.onosproject.kubevirtnetworking.api.Constants.CLI_IP_ADDRESS_LENGTH;
import static org.onosproject.kubevirtnetworking.api.Constants.CLI_MARGIN_LENGTH;
import static org.onosproject.kubevirtnetworking.api.Constants.CLI_NAME_LENGTH;
import static org.onosproject.kubevirtnetworking.util.KubevirtNetworkingUtil.genFormatString;
import static org.onosproject.kubevirtnetworking.util.KubevirtNetworkingUtil.prettyJson;

/**
 * Lists kubevirt routers.
 */
@Service
@Command(scope = "onos", name = "kubevirt-routers",
        description = "Lists all kubevirt routers")
public class KubevirtListRouterCommand extends AbstractShellCommand {

    @Override
    protected void doExecute() throws Exception {
        KubevirtRouterService service = get(KubevirtRouterService.class);
        List<KubevirtRouter> routers = Lists.newArrayList(service.routers());
        routers.sort(Comparator.comparing(KubevirtRouter::name));

        String format = genFormatString(ImmutableList.of(CLI_NAME_LENGTH,
                CLI_FLAG_LENGTH, CLI_IP_ADDRESSES_LENGTH, CLI_IP_ADDRESS_LENGTH, CLI_NAME_LENGTH));

        if (outputJson()) {
            print("%s", json(routers));
        } else {
            print(format, "Name", "SNAT", "Internal", "External", "GatewayNode");

            for (KubevirtRouter router : routers) {
                Set<String> internalCidrs = router.internal();
                Set<String> externalIps = router.external().keySet();

                String internal = internalCidrs.size() == 0 ? "[]" : internalCidrs.toString();
                String external = externalIps.size() == 0 ? "[]" : externalIps.toString();
                String gwNode = router.electedGateway() == null ? "N/A" : router.electedGateway();

                print(format, StringUtils.substring(router.name(), 0,
                        CLI_NAME_LENGTH - CLI_MARGIN_LENGTH),
                        StringUtils.substring(String.valueOf(router.enableSnat()), 0,
                                CLI_FLAG_LENGTH - CLI_MARGIN_LENGTH),
                        StringUtils.substring(internal, 0,
                                CLI_IP_ADDRESSES_LENGTH - CLI_MARGIN_LENGTH),
                        StringUtils.substring(external, 0,
                                CLI_IP_ADDRESS_LENGTH - CLI_MARGIN_LENGTH),
                        StringUtils.substring(gwNode, 0,
                                CLI_NAME_LENGTH - CLI_MARGIN_LENGTH)
                );
            }
        }
    }

    private String json(List<KubevirtRouter> routers) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode result = mapper.createArrayNode();

        for (KubevirtRouter router : routers) {
            result.add(jsonForEntity(router, KubevirtRouter.class));
        }

        return prettyJson(mapper, result.toString());
    }
}
