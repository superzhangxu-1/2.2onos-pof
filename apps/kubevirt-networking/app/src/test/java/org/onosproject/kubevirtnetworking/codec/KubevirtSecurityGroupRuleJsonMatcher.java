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
package org.onosproject.kubevirtnetworking.codec;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.onlab.packet.IpPrefix;
import org.onosproject.kubevirtnetworking.api.KubevirtSecurityGroupRule;

/**
 * Hamcrest matcher for kubevirt port.
 */
public final class KubevirtSecurityGroupRuleJsonMatcher extends TypeSafeDiagnosingMatcher<JsonNode> {

    private final KubevirtSecurityGroupRule rule;

    private static final String ID = "id";
    private static final String SECURITY_GROUP_ID = "securityGroupId";
    private static final String DIRECTION = "direction";
    private static final String ETHER_TYPE = "etherType";
    private static final String PORT_RANGE_MAX = "portRangeMax";
    private static final String PORT_RANGE_MIN = "portRangeMin";
    private static final String PROTOCOL = "protocol";
    private static final String REMOTE_IP_PREFIX = "remoteIpPrefix";
    private static final String REMOTE_GROUP_ID = "remoteGroupId";

    private KubevirtSecurityGroupRuleJsonMatcher(KubevirtSecurityGroupRule rule) {
        this.rule = rule;
    }

    @Override
    protected boolean matchesSafely(JsonNode jsonNode, Description description) {
        // check rule ID
        String jsonId = jsonNode.get(ID).asText();
        String id = rule.id();
        if (!jsonId.equals(id)) {
            description.appendText("Rule ID was " + jsonId);
            return false;
        }

        // check security group ID
        String jsonSecurityGroupId = jsonNode.get(SECURITY_GROUP_ID).asText();
        String securityGroupId = rule.securityGroupId();
        if (!jsonSecurityGroupId.equals(securityGroupId)) {
            description.appendText("Security group ID was " + jsonSecurityGroupId);
            return false;
        }

        // check direction
        String jsonDirection = jsonNode.get(DIRECTION).asText();
        String direction = rule.direction();
        if (!jsonDirection.equals(direction)) {
            description.appendText("Direction was " + jsonDirection);
            return false;
        }

        // check ether type
        JsonNode jsonEtherType = jsonNode.get(ETHER_TYPE);
        if (jsonEtherType != null) {
            String etherType = rule.etherType();
            if (!jsonEtherType.asText().equals(etherType)) {
                description.appendText("EtherType was " + jsonEtherType);
                return false;
            }
        }

        // check port range max
        JsonNode jsonPortRangeMax = jsonNode.get(PORT_RANGE_MAX);
        if (jsonPortRangeMax != null) {
            int portRangeMax = rule.portRangeMax();
            if (portRangeMax != jsonPortRangeMax.asInt()) {
                description.appendText("PortRangeMax was " + jsonPortRangeMax);
                return false;
            }
        }

        // check port range min
        JsonNode jsonPortRangeMin = jsonNode.get(PORT_RANGE_MIN);
        if (jsonPortRangeMin != null) {
            int portRangeMin = rule.portRangeMin();
            if (portRangeMin != jsonPortRangeMin.asInt()) {
                description.appendText("PortRangeMin was " + jsonPortRangeMin);
                return false;
            }
        }

        // check protocol
        JsonNode jsonProtocol = jsonNode.get(PROTOCOL);
        if (jsonProtocol != null) {
            String protocol = rule.protocol();
            if (!jsonProtocol.asText().equals(protocol)) {
                description.appendText("Protocol was " + jsonProtocol);
                return false;
            }
        }

        // check remote IP prefix
        JsonNode jsonRemoteIpPrefix = jsonNode.get(REMOTE_IP_PREFIX);
        if (jsonRemoteIpPrefix != null) {
            IpPrefix remoteIpPrefix = rule.remoteIpPrefix();
            if (!jsonRemoteIpPrefix.asText().equals(remoteIpPrefix.toString())) {
                description.appendText("Remote IP prefix was " + jsonRemoteIpPrefix);
                return false;
            }
        }

        // check remote group ID
        JsonNode jsonRemoteGroupId = jsonNode.get(REMOTE_GROUP_ID);
        if (jsonRemoteGroupId != null) {
            String remoteGroupId = rule.remoteGroupId();
            if (!jsonRemoteGroupId.asText().equals(remoteGroupId)) {
                description.appendText("Remote group ID was " + jsonRemoteGroupId);
                return false;
            }
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(rule.toString());
    }

    /**
     * Factory to allocate an kubevirt security group rule matcher.
     *
     * @param rule kubevirt security group rule object we are looking for
     * @return matcher
     */
    public static KubevirtSecurityGroupRuleJsonMatcher
        matchesKubevirtSecurityGroupRule(KubevirtSecurityGroupRule rule) {
        return new KubevirtSecurityGroupRuleJsonMatcher(rule);
    }
}
