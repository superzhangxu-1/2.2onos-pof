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
package org.onosproject.kubevirtnode.api;

import com.google.common.testing.EqualsTester;
import org.junit.Before;
import org.junit.Test;
import org.onlab.packet.IpAddress;

import static junit.framework.TestCase.assertEquals;
import static org.onlab.junit.ImmutableClassChecker.assertThatClassIsImmutable;
import static org.onosproject.kubevirtnode.api.KubevirtApiConfig.Scheme.HTTP;
import static org.onosproject.kubevirtnode.api.KubevirtApiConfig.Scheme.HTTPS;
import static org.onosproject.kubevirtnode.api.KubevirtApiConfig.State.CONNECTED;


/**
 * Unit tests for DefaultKubevirtApiConfig.
 */
public final class DefaultKubevirtApiConfigTest {

    private static final KubevirtApiConfig.Scheme SCHEME_1 = HTTP;
    private static final KubevirtApiConfig.Scheme SCHEME_2 = HTTPS;

    private static final IpAddress IP_ADDRESS_1 = IpAddress.valueOf("192.168.0.200");
    private static final IpAddress IP_ADDRESS_2 = IpAddress.valueOf("192.168.0.201");

    private static final int PORT_1 = 6443;
    private static final int PORT_2 = 443;

    private static final String TOKEN_1 = "token1";
    private static final String TOKEN_2 = "token2";

    private static final String CA_CERT_DATA_1 = "caCertData1";
    private static final String CA_CERT_DATA_2 = "caCertData2";

    private static final String CLIENT_CERT_DATA_1 = "clientCertData1";
    private static final String CLIENT_CERT_DATA_2 = "clientCertData2";

    private static final String CLIENT_KEY_DATA_1 = "clientKeyData1";
    private static final String CLIENT_KEY_DATA_2 = "clientKeyData2";

    private KubevirtApiConfig config1;
    private KubevirtApiConfig sameAsConfig1;
    private KubevirtApiConfig config2;

    /**
     * Tests class immutability.
     */
    @Test
    public void testImmutability() {
        assertThatClassIsImmutable(DefaultKubevirtApiConfig.class);
    }

    /**
     * Initial setup for this unit test.
     */
    @Before
    public void setUp() {
        config1 = DefaultKubevirtApiConfig.builder()
                .scheme(SCHEME_1)
                .ipAddress(IP_ADDRESS_1)
                .port(PORT_1)
                .state(CONNECTED)
                .token(TOKEN_1)
                .caCertData(CA_CERT_DATA_1)
                .clientCertData(CLIENT_CERT_DATA_1)
                .clientKeyData(CLIENT_KEY_DATA_1)
                .build();

        sameAsConfig1 = DefaultKubevirtApiConfig.builder()
                .scheme(SCHEME_1)
                .ipAddress(IP_ADDRESS_1)
                .port(PORT_1)
                .state(CONNECTED)
                .token(TOKEN_1)
                .caCertData(CA_CERT_DATA_1)
                .clientCertData(CLIENT_CERT_DATA_1)
                .clientKeyData(CLIENT_KEY_DATA_1)
                .build();

        config2 = DefaultKubevirtApiConfig.builder()
                .scheme(SCHEME_2)
                .ipAddress(IP_ADDRESS_2)
                .port(PORT_2)
                .state(CONNECTED)
                .token(TOKEN_2)
                .caCertData(CA_CERT_DATA_2)
                .clientCertData(CLIENT_CERT_DATA_2)
                .clientKeyData(CLIENT_KEY_DATA_2)
                .build();
    }

    /**
     * Tests object equality.
     */
    @Test
    public void testEquality() {
        new EqualsTester().addEqualityGroup(config1, sameAsConfig1)
                .addEqualityGroup(config2)
                .testEquals();
    }

    /**
     * Test object construction.
     */
    @Test
    public void testConstruction() {
        KubevirtApiConfig config = config1;

        assertEquals(SCHEME_1, config.scheme());
        assertEquals(IP_ADDRESS_1, config.ipAddress());
        assertEquals(PORT_1, config.port());
        assertEquals(CONNECTED, config.state());
        assertEquals(TOKEN_1, config.token());
        assertEquals(CA_CERT_DATA_1, config.caCertData());
        assertEquals(CLIENT_CERT_DATA_1, config.clientCertData());
        assertEquals(CLIENT_KEY_DATA_1, config.clientKeyData());
    }
}
