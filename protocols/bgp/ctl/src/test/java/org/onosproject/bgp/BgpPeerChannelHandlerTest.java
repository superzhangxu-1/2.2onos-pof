/*
 * Copyright 2015-present Open Networking Foundation
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
package org.onosproject.bgp;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.onosproject.bgpio.protocol.ver4.BgpKeepaliveMsgVer4;
import org.onosproject.bgpio.protocol.ver4.BgpOpenMsgVer4;
import org.onosproject.bgpio.types.BgpHeader;
import org.onosproject.bgpio.types.BgpValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BgpPeerChannelHandlerTest extends SimpleChannelHandler {
    private static final Logger log = LoggerFactory.getLogger(BgpPeerChannelHandlerTest.class);
    public static final int OPEN_MSG_MINIMUM_LENGTH = 29;
    public static final byte[] MARKER = new byte[] {(byte) 0xff, (byte) 0xff,
        (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
        (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
        (byte) 0xff, (byte) 0xff};
    public static final BgpHeader DEFAULT_OPEN_HEADER = new BgpHeader(MARKER,
            (short) OPEN_MSG_MINIMUM_LENGTH, (byte) 0X01);
    LinkedList<BgpValueType> capabilityTlv = new LinkedList<>();
    public byte version;
    public short asNumber;
    public short holdTime;
    public int bgpId;
    public boolean isLargeAsCapabilitySet;

    final BgpOpenMsgVer4 openMessage = new BgpOpenMsgVer4();
    ChannelHandlerContext savedCtx;

    /**
     * Constructor to initialize all variables of BGP Open message.
     *
     * @param version BGP version in open message
     * @param asNumber AS number in open message
     * @param holdTime hold time in open message
     * @param bgpId BGP identifier in open message
     * @param capabilityTlv capabilities in open message
     */
    public BgpPeerChannelHandlerTest(byte version,
            short asNumber,
            short holdTime,
            int bgpId,
            boolean isLargeAsCapabilitySet,
            LinkedList<BgpValueType> capabilityTlv) {
        this.version = version;
        this.asNumber = asNumber;
        this.holdTime = holdTime;
        this.bgpId = bgpId;
        this.isLargeAsCapabilitySet = isLargeAsCapabilitySet;
        this.capabilityTlv = capabilityTlv;
    }

    /**
     * closes the channel.
     */
    void closeChannel() {
        savedCtx.getChannel().close();
    }

    /**
     * Select update message buffer with VPN.
     *
     * @param select number to select update message buffer
     * @return packet dump in byte array
     */
    byte[] selectUpdateMessageVpn(int select) {
        switch (select) {
        case 5:
            /**
             * Node NLRI with VPN - MpReach.
             */
            byte[] updateMsg = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, 0x5E, 0x02, 0x00, 0x00, //withdrawn len
                    0x00, 0x47, //path attribute len
                    0x04, 0x01, 0x01, 0x02, //origin
                    0x40, 0x02, 0x04, 0x02, 0x01, (byte) 0xfd, (byte) 0xe9, //as_path
                    (byte) 0x80, 0x04, 0x04, 0x00, 0x00, 0x00, 0x00, //med
                    (byte) 0x80, 0x0e, 0x32, 0x40, 0x04, (byte) 0x80, //mpreach with safi = 80 vpn
                    0x04, 0x04, 0x00, 0x00, 0x01, //nexthop
                    0x00, //reserved
                    0x00, 0x01, 0x00, 0x25, //type ND LEN
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A,
                    0x02,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x01, 0x00, 0x00, 0x10,
                    0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x09, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02}; //node nlri
        return updateMsg;
        case 6:
            /**
             * Node NLRI MpReach and MpUnReach different with same VPN.
             */
            updateMsg = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, (byte) 0x8D, 0x02, 0x00, 0x00, //withdrawn len
                    0x00, 0x76, //path attribute len
                    0x04, 0x01, 0x01, 0x02, //origin
                    0x40, 0x02, 0x04, 0x02, 0x01, (byte) 0xfd, (byte) 0xe9, //as_path
                    (byte) 0x80, 0x04, 0x04, 0x00, 0x00, 0x00, 0x00, //med
                    (byte) 0x80, 0x0e, 0x32, 0x40, 0x04, (byte) 0x80, //mpreach with safi = 80 vpn
                    0x04, 0x04, 0x00, 0x00, 0x01, //nexthop
                    0x00, //reserved
                    0x00, 0x01, 0x00, 0x25, //type ND LEN
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A,
                    0x02,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x01, 0x00, 0x00, 0x10,
                    0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x02, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02, //node nlri
                    (byte) 0x80, 0x0f, 0x2C,  0x40, 0x04, (byte) 0x80, //mpUnreach with safi = 80 VPN
                    0x00, 0x01, 0x00, 0x25, //type n len
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A,
                    0x02,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x01, 0x00, 0x00, 0x10,
                    0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x04, (byte) 0xae, //AutonomousSystemTlv
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02}; //BGPLSIdentifierTlv
            return updateMsg;
        case 7:
            /**
             * Node NLRI with same MpReach and MpUnReach with VPN.
             */
            updateMsg = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, (byte) 0x8D, 0x02, 0x00, 0x00, //withdrawn len
                    0x00, 0x76, //path attribute len
                    0x04, 0x01, 0x01, 0x02, //origin
                    0x40, 0x02, 0x04, 0x02, 0x01, (byte) 0xfd, (byte) 0xe9, //as_path
                    (byte) 0x80, 0x04, 0x04, 0x00, 0x00, 0x00, 0x00, //med
                    (byte) 0x80, 0x0e, 0x32, 0x40, 0x04, (byte) 0x80, //mpreach with safi = 80 vpn
                    0x04, 0x04, 0x00, 0x00, 0x01, //nexthop
                    0x00, //reserved
                    0x00, 0x01, 0x00, 0x25, //type ND LEN
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A,
                    0x02,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x01, 0x00, 0x00, 0x10,
                    0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x09, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02, //node nlri
                    (byte) 0x80, 0x0f, 0x2C,  0x40, 0x04, (byte) 0x80, //mpUnreach with safi = 80 VPN
                    0x00, 0x01, 0x00, 0x25, //type n len
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A,
                    0x02,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x01, 0x00, 0x00, 0x10,
                    0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x09, (byte) 0xae, //AutonomousSystemTlv
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02}; //BGPLSIdentifierTlv
            return updateMsg;
        case 17:
            /**
             * Node NLRI with VPN - MpReach.
             */
            updateMsg = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, 0x5E, 0x02, 0x00, 0x00, //withdrawn len
                    0x00, 0x47, //path attribute len
                    0x04, 0x01, 0x01, 0x02, //origin
                    0x40, 0x02, 0x04, 0x02, 0x01, (byte) 0xfd, (byte) 0xe9, //as_path
                    (byte) 0x80, 0x04, 0x04, 0x00, 0x00, 0x00, 0x00, //med
                    (byte) 0x80, 0x0e, 0x32, 0x40, 0x04, (byte) 0x80, //mpreach with safi = 80 vpn
                    0x04, 0x04, 0x00, 0x00, 0x01, //nexthop
                    0x00, //reserved
                    0x00, 0x01, 0x00, 0x25, //type ND LEN
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0A,
                    0x02,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x01, 0x00, 0x00, 0x10,
                    0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x09, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02}; //node nlri
        return updateMsg;
        default: return null;
        }
    }

    /**
     * Select update message buffer without VPN.
     *
     * @param select number to select update message buffer
     * @return packet dump in byte array
     */
    byte[] selectUpdateMsg(int select) {
        switch (select) {
        case 1:
            /**
             * Node NLRI - MpReach.
             */
            byte[] updateMsg = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, 0x56, 0x02, 0x00, 0x00, //withdrawn len
                    0x00, 0x3F, 0x04, 0x01, 0x01, 0x02, //origin
                    0x40, 0x02, 0x04, 0x02, 0x01, (byte) 0xfd, (byte) 0xe9, //as_path
                    (byte) 0x80, 0x04, 0x04, 0x00, 0x00, 0x00, 0x00, //med
                    (byte) 0x80, 0x0e, 0x2A, 0x40, 0x04, 0x47, //mpreach with safi = 71
                    0x04, 0x04, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01, 0x00, 0x1D, 0x02,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x10,
                    0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x09, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02}; //node nlri
            return updateMsg;
        case 2:
            /**
             * Node NLRI with same MpReach and MpUnReach.
             */
            updateMsg = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, (byte) 0x7D, 0x02, 0x00, 0x00, //withdrawn len
                    0x00, 0x66, 0x04, 0x01, 0x01, 0x02, 0x40, 0x02, 0x04, 0x02, 0x01, (byte) 0xfd, (byte) 0xe9,
                    (byte) 0x80, 0x04, 0x04, 0x00, 0x00, 0x00, 0x00, //med
                    (byte) 0x80, 0x0e, 0x2A, 0x40, 0x04, 0x47, //mpreach with safi = 71
                    0x04, 0x04, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01, 0x00, 0x1D, //type n len
                    0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x01, 0x00, 0x00, 0x10, 0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x09, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02, //BGPLSIdentifierTlv
                    (byte) 0x80, 0x0f, 0x24,  0x40, 0x04, 0x47, //mpUnreach with safi = 71
                    0x00, 0x01, 0x00, 0x1D, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x01, 0x00, 0x00, 0x10, 0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x09, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02, //BGPLSIdentifierTlv
                    };
            return updateMsg;
        case 3:
            /**
             * Link NLRI - MpReach.
             */
            updateMsg = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, (byte) 0x8B, 0x02, 0x00, 0x04,
                    0x18, 0x0a, 0x01, 0x01, 0x00, 0x70, //path attribute len
                    0x04, 0x01, 0x01, 0x00, 0x40, 0x02, 0x04, 0x02, 0x01, (byte) 0xfd, (byte) 0xe9, //as_path
                    (byte) 0x80, 0x04, 0x04, 0x00, 0x00, 0x00, 0x10, //med
                    (byte) 0x80, 0x0e, 0x5B, 0x40, 0x04, 0x47, //mpreach safi 71
                    0x04, 0x04, 0x00, 0x00, 0x01, 0x00, 0x00, 0x02, 0x00, 0x4E, 0x02,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x1b, //local node
                    0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x08, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02,
                    0x02, 0x03, 0x00, 0x07, 0x19, 0x00, (byte) 0x95, 0x02, 0x50, 0x21, 0x03,
                    0x01, 0x01, 0x00, 0x1a, 0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x08, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02,
                    0x02, 0x03, 0x00, 0x06, 0x19, 0x00, (byte) 0x95, 0x02, 0x50, 0x21, //link nlri
                    0x01, 0x03, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02
            };
            return updateMsg;
        case 4:
            /**
             * Prefix NLRI - MpReach.
             */
            updateMsg = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, (byte) 0xd6, 0x02, 0x00, 0x04,
                    0x18, 0x0a, 0x01, 0x01, 0x00, (byte) 0xbb, //path attribute len
                    0x04, 0x01, 0x01, 0x00, 0x40, 0x02, 0x04, 0x02, 0x01, (byte) 0xfd, (byte) 0xe9, //as_path
                    (byte) 0x80, 0x04, 0x04, 0x00, 0x00, 0x00, 0x12, //med
                    (byte) 0x90, 0x0e, 0x00, (byte) 0xa5, 0x40, 0x04, 0x47, //mpreach
                    0x04, 0x04, 0x00, 0x00, 0x01, 0x00, 0x00, 0x03, 0x00, 0x30,
                    0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x1a, //local node
                    0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x08, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02, 0x02, 0x03, 0x00, 0x06, 0x19, 0x21, 0x68, 0x07,
                    0x70, 0x01, 0x01, 0x09, 0x00, 0x05, 0x20, (byte) 0xc0, (byte) 0xa8, 0x4d, 0x01, //prefix des
                    0x00, 0x03, 0x00, 0x30, 0x02,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x1a,
                    0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x08, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02,
                    0x02, 0x03, 0x00, 0x06, 0x19, 0x00, (byte) 0x95, 0x02, 0x50, 0x21,
                    0x01, 0x09, 0x00, 0x05, 0x20, 0x15, 0x15, 0x15, 0x15, 0x00, 0x03, 0x00, 0x30,
                    0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x01, 0x00, 0x00, 0x1a, 0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x08, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02,
                    0x02, 0x03, 0x00, 0x06, 0x02, 0x20, 0x22, 0x02, 0x20, 0x22,
                    0x01, 0x09, 0x00, 0x05, 0x20, 0x16, 0x16, 0x16, 0x16}; // prefix nlri
        return updateMsg;
        case 8:
            /**
             * Node NLRI with different MpReach and MpUnReach with IsIsPseudonode.
             */
                        updateMsg = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00,
                                (byte) 0x91, 0x02, 0x00, 0x00, 0x00, 0x7A, //path attribute len
                                0x04, 0x01, 0x01, 0x02, 0x40, 0x02, 0x04, 0x02, 0x01, (byte) 0xfd, (byte) 0xe9,
                                (byte) 0x80, 0x04, 0x04, 0x00, 0x00, 0x00, 0x00, //med
                                (byte) 0x80, 0x0e, 0x34, 0x40, 0x04, 0x47, //mpreach with safi = 71
                                0x04, 0x04, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01, 0x00,
                                0x27, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                                0x01, 0x00, 0x00, 0x1a, 0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x08, (byte) 0xae,
                                0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02, 0x02, 0x03,
                                0x00, 0x06, 0x19, 0x00, (byte) 0x95, 0x01, (byte) 0x90, 0x58,
                                (byte) 0x80, 0x0f, 0x2E,  0x40, 0x04, 0x47, 0x00, 0x01, 0x00, 0x27, //mpUnreach
                                0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                                0x01, 0x00, 0x00, 0x1a, 0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x09, (byte) 0xae,
                                0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02, //BGPLSIdentifierTlv
                                0x02, 0x03, 0x00, 0x06, 0x19, 0x00, (byte) 0x99, 0x01, (byte) 0x99, 0x58};
                        return updateMsg;
        case 9:
            /**
             * Node NLRI with same MpReach and MpUnReach.
             */
            updateMsg = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, (byte) 0x7D, 0x02, 0x00, 0x00, //withdrawn len
                    0x00, 0x66, 0x04, 0x01, 0x01, 0x02, //origin
                    0x40, 0x02, 0x04, 0x02, 0x01, (byte) 0xfd, (byte) 0xe9, //as_path
                    (byte) 0x80, 0x04, 0x04, 0x00, 0x00, 0x00, 0x00, //med
                    (byte) 0x80, 0x0e, 0x2A, 0x40, 0x04, 0x47, //mpreach with safi = 71
                    0x04, 0x04, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01, 0x00, 0x1D, //type n len
                    0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x01, 0x00, 0x00, 0x10, 0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x09, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02, //BGPLSIdentifierTlv
                    (byte) 0x80, 0x0f, 0x24,  0x40, 0x04, 0x47, //mpUnreach with safi = 71
                    0x00, 0x01, 0x00, 0x1D, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x01, 0x00, 0x00, 0x10, 0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x09, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02, //BGPLSIdentifierTlv
                    };
            return updateMsg;
        case 10:
            /**
             * Node NLRI with different MpReach and MpUnReach.
             */
            updateMsg = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, (byte) 0x7D, 0x02, 0x00, 0x00, //withdrawn len
                    0x00, 0x66, 0x04, 0x01, 0x01, 0x02, //origin
                    0x40, 0x02, 0x04, 0x02, 0x01, (byte) 0xfd, (byte) 0xe9, //as_path
                    (byte) 0x80, 0x04, 0x04, 0x00, 0x00, 0x00, 0x00, //med
                    (byte) 0x80, 0x0e, 0x2A, 0x40, 0x04, 0x47, //mpreach with safi = 71
                    0x04, 0x04, 0x00, 0x00, 0x01, 0x00, //reserved
                    0x00, 0x01, 0x00, 0x1D, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x01, 0x00, 0x00, 0x10, 0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x09, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02, //BGPLSIdentifierTlv
                    (byte) 0x80, 0x0f, 0x24,  0x40, 0x04, 0x47, //mpUnreach with safi = 71
                    0x00, 0x01, 0x00, 0x1D, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x01, 0x00, 0x00, 0x10,  0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x08, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02, //BGPLSIdentifierTlv
                    };
            return updateMsg;
        case 12:
            /**
             * Node NLRI with same MpReach and MpUnReach with IsIsPseudonode.
             */
          updateMsg = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, (byte) 0x91, 0x02, 0x00, 0x00, //withdrawn len
                    0x00, 0x7A, 0x04, 0x01, 0x01, 0x02, //origin
                    0x40, 0x02, 0x04, 0x02, 0x01, (byte) 0xfd, (byte) 0xe9, //as_path
                    (byte) 0x80, 0x04, 0x04, 0x00, 0x00, 0x00, 0x00, //med
                    (byte) 0x80, 0x0e, 0x34, 0x40, 0x04, 0x47, //mpreach with safi = 71
                    0x04, 0x04, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01, 0x00, 0x27, //type n len
                    0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x01, 0x00, 0x00, 0x1a, 0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x09, (byte) 0xae,
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02, //BGPLSIdentifierTlv
                    0x02, 0x03, 0x00, 0x06, 0x19, 0x00, (byte) 0x99, 0x01, (byte) 0x99, 0x58,
                    (byte) 0x80, 0x0f, 0x2E,  0x40, 0x04, 0x47, //mpUnreach with safi = 71
                    0x00, 0x01, 0x00, 0x27, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x01, 0x00, 0x00, 0x1a, 0x02, 0x00, 0x00, 0x04, 0x00, 0x00, 0x09, (byte) 0xae, //AutonomousSystemTlv
                    0x02, 0x01, 0x00, 0x04, 0x02, 0x02, 0x02, 0x02, //BGPLSIdentifierTlv
                    0x02, 0x03, 0x00, 0x06, 0x19, 0x00, (byte) 0x99, 0x01, (byte) 0x99, 0x58};
          return updateMsg;
      default:        return null;
        }
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx,
                                 ChannelStateEvent channelEvent) throws InterruptedException {
        this.savedCtx = ctx;

        BgpOpenMsgVer4 openMsg = new BgpOpenMsgVer4(DEFAULT_OPEN_HEADER,
                this.version,
                this.asNumber,
                this.holdTime,
                this.bgpId,
                this.capabilityTlv);
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        openMsg.writeTo(buffer);
        ctx.getChannel().write(buffer);

        TimeUnit.MILLISECONDS.sleep(100);

        BgpKeepaliveMsgVer4 keepaliveMsg = new BgpKeepaliveMsgVer4();
        ChannelBuffer buffer1 = ChannelBuffers.dynamicBuffer();
        keepaliveMsg.writeTo(buffer1);
        ctx.getChannel().write(buffer1);

        if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.1", 0).getAddress())) {
            return;
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.9", 0).getAddress())) {
            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMsg(1));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.33", 0).getAddress())) {
            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMsg(2));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.10", 0).getAddress())) {
            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMsg(3));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.20", 0).getAddress())) {

            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMsg(4));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.30", 0).getAddress())) {

            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMessageVpn(5));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.40", 0).getAddress())) {

            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMessageVpn(6));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.50", 0).getAddress())) {

            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMessageVpn(7));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.60", 0).getAddress())) {

            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMsg(8));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.70", 0).getAddress())) {

            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMsg(9));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.80", 0).getAddress())) {

            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMsg(10));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.90", 0).getAddress())) {

            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMsg(8));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.92", 0).getAddress())) {

            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMsg(12));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.91", 0).getAddress())) {

            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMsg(1));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.9", 0).getAddress())) {

            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMsg(1));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.99", 0).getAddress())) {

            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMsg(1));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.94", 0).getAddress())) {

            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMsg(1));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.35", 0).getAddress())) {

            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMessageVpn(17));
            ctx.getChannel().write(bufferUp);
        } else if (((InetSocketAddress) ctx.getChannel().getLocalAddress()).getAddress().equals(
                new InetSocketAddress("127.0.0.95", 0).getAddress())) {

            ChannelBuffer bufferUp = ChannelBuffers.dynamicBuffer();
            bufferUp.writeBytes(selectUpdateMsg(1));
            ctx.getChannel().write(bufferUp);
        }
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx,
                                    ChannelStateEvent channelEvent) {
        //Do Nothing
    }
}
