/*
 *
 *  * Copyright 2018-present Open Networking Foundation
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 *
 */

package org.onosproject.Pofflowtest;

import org.onlab.packet.Ethernet;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.floodlightpof.protocol.OFMatch20;
import org.onosproject.floodlightpof.protocol.action.OFAction;
import org.onosproject.floodlightpof.protocol.table.OFFlowTable;
import org.onosproject.floodlightpof.protocol.table.OFTableType;
import org.onosproject.net.DeviceId;
import org.onosproject.net.HostId;
import org.onosproject.net.PortNumber;
import org.onosproject.net.device.DeviceAdminService;
import org.onosproject.net.flow.DefaultFlowRule;
import org.onosproject.net.flow.DefaultTrafficSelector;
import org.onosproject.net.flow.DefaultTrafficTreatment;
import org.onosproject.net.flow.FlowRule;
import org.onosproject.net.flow.FlowRuleService;
import org.onosproject.net.flow.TrafficSelector;
import org.onosproject.net.flow.TrafficTreatment;
import org.onosproject.net.flow.criteria.Criteria;
import org.onosproject.net.flow.criteria.Criterion;
import org.onosproject.net.flow.instructions.DefaultPofActions;
import org.onosproject.net.flow.instructions.DefaultPofInstructions;
import org.onosproject.net.group.GroupService;
import org.onosproject.net.packet.InboundPacket;
import org.onosproject.net.packet.PacketContext;
import org.onosproject.net.packet.PacketProcessor;
import org.onosproject.net.packet.PacketService;
import org.onosproject.net.table.DefaultFlowTable;
import org.onosproject.net.table.FlowTable;
import org.onosproject.net.table.FlowTableId;
import org.onosproject.net.table.FlowTableService;
import org.onosproject.net.table.FlowTableStore;
import org.onosproject.net.topology.PathService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Skeletal ONOS application component.
 */
@Component(immediate = true)
public class Pofflowtest {

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected FlowTableStore flowTableStore;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected FlowRuleService flowRuleService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected FlowTableStore tableStore;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected FlowTableService flowTableService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected CoreService coreService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected DeviceAdminService deviceService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected GroupService groupService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected PacketService packetService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected PathService pathService;

    private final Logger log = LoggerFactory.getLogger(getClass());
    private ApplicationId appId;

    int controller_port = (int) PortNumber.CONTROLLER.toLong();
    private DeviceId deviceId1;
    private DeviceId deviceId2;
    private byte global_smalltable_id_1 [] =new byte[6] ;    // used for pof
    //field id
    public final short SIP = 12;

    private byte tableId1 = 0;
    private  byte tableId2 = 0;

    private ScheduledExecutorService executor;
    private PacketProcessor packetProcessor =new ReactivePacketProcessor();

    @Activate
    protected void activate() {
        log.info("started");
        appId = coreService.registerApplication("org.onosproject.pofflowtest");
        pofTestStart();
        packetService.addProcessor(packetProcessor, PacketProcessor.director(4));
    }

    @Deactivate
    protected void deactivate() {
        pofTestStop();
        packetService.removeProcessor(packetProcessor);
    }

    public void pofTestStart() {
        log.info("org.onosproject.flowtest.started");

        deviceId1 = DeviceId.deviceId("pof:0000000000000001");
        deviceId2 = DeviceId.deviceId("pof:0000000000000002");

        tableId1 = sendPofFlowTable(deviceId1, "FirstEntryTable1");
        tableId2 = sendPofFlowTable(deviceId2, "FirstEntryTable2");

        global_smalltable_id_1[0] = tableId1;
        global_smalltable_id_1[1] = tableId2;

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //install_pof_no_int_output_flow_rule(deviceId1, tableId1, "0a000001", controller_port, 30);
        //install_pof_no_int_output_flow_rule(deviceId1, tableId1, "0a000004", 1, 31);
        //install_pof_packet_in_flow_rule(deviceId1, global_smalltable_id_1[0],"0a000001", 0, 5);//FIXME what is the reason
        //install_pof_packet_in_flow_rule(deviceId1, global_smalltable_id_1[0],"0a000002", 0, 5);//FIXME what is the reason
        //install_pof_packet_in_flow_rule(deviceId2, global_smalltable_id_1[1],"0a000001", 0, 5);//FIXME what is the reason
        //install_pof_packet_in_flow_rule(deviceId2, global_smalltable_id_1[1],"0a000002", 0, 5);//FIXME what is the reason
    }

    public void pofTestStop() {
        remove_pof_flow_table(deviceId1, global_smalltable_id_1[0]);
        remove_pof_flow_table(deviceId2, global_smalltable_id_1[1]);
        log.info("org.onosproject.flowtest Stopped");
    }

    public byte sendPofFlowTable(DeviceId deviceId, String table_name) {
        byte globeTableId = (byte) tableStore.getNewGlobalFlowTableId(deviceId, OFTableType.OF_MM_TABLE);
        log.info("globeTableId is {}",globeTableId);//FIXME globeTableId=-1
        byte tableId = tableStore.parseToSmallTableId(deviceId, globeTableId);
        log.info("smalltableId is {}",tableId);

        OFMatch20 srcIP = new OFMatch20();
        srcIP.setFieldId((short) SIP);
        srcIP.setFieldName("srcIP");
        srcIP.setOffset((short) 208);
        srcIP.setLength((short) 32);

        ArrayList<OFMatch20> match20List = new ArrayList<>();
        match20List.add(srcIP);

        OFFlowTable ofFlowTable = new OFFlowTable();
        ofFlowTable.setTableId(tableId);
        ofFlowTable.setTableName(table_name);
        ofFlowTable.setMatchFieldList(match20List);
        ofFlowTable.setMatchFieldNum((byte) 1);
        ofFlowTable.setTableSize(20);
        ofFlowTable.setTableType(OFTableType.OF_MM_TABLE);
        ofFlowTable.setCommand(null);
        ofFlowTable.setKeyLength((short) 32);

        FlowTable.Builder flowtable = DefaultFlowTable.builder()
                .withFlowTable(ofFlowTable)
                .forTable(tableId)
                .forDevice(deviceId)
                .fromApp(appId);

        flowTableService.applyFlowTables(flowtable.build());

        log.info("table<{}> applied to device<{}> successfully", tableId, deviceId.toString());

        return tableId;

    }

    /**
     * install simple flow entries
     * @param deviceId specific pof_switch
     * @param tableId smalltableid
     * @param srcIP specific packet sip
     * @param outport The port to forward the packet
     * @param priority flow entry priority
     */
    public void install_pof_no_int_output_flow_rule(DeviceId deviceId, byte tableId, String srcIP, int outport, int priority) {
        // match field build
        TrafficSelector.Builder trafficSelector = DefaultTrafficSelector.builder();
        ArrayList<Criterion> matchList = new ArrayList<>();
        matchList.add(Criteria.matchOffsetLength(SIP, (short) 208, (short) 32, srcIP, "FFFFFFFF"));
        trafficSelector.add(Criteria.matchOffsetLength(matchList));;

        //action field build
        TrafficTreatment.Builder trafficeTreatm = DefaultTrafficTreatment.builder();
        List<OFAction> actions = new ArrayList<>();
        OFAction action_output = DefaultPofActions.output((short) 0, (short) 0, (short) 0, outport).action();
        actions.add(action_output);
        trafficeTreatm.add(DefaultPofInstructions.applyActions(actions));
        log.info("action_output: {}.", actions);

        //apply
        long newFlowEntryId = flowTableStore.getNewFlowEntryId(deviceId,tableId);
        FlowRule.Builder flowRule = DefaultFlowRule.builder()
                .forDevice(deviceId)
                .forTable(tableId)
                .withSelector(trafficSelector.build())
                .withTreatment(trafficeTreatm.build())
                .withPriority(priority)
                .withCookie(newFlowEntryId)
                .makePermanent();
        flowRuleService.applyFlowRules(flowRule.build());
        log.info("Test no INT: apply to deviceId<{}> tableId<{}>, entryId=<{}>", deviceId.toString(), tableId, newFlowEntryId);
    }

    /**
     * install simple pktin flow entries
     * @param deviceId deviceid
     * @param tableId table id
     * @param reason unknown
     * @param priority flow entry priority
     */
    public void install_pof_packet_in_flow_rule(DeviceId deviceId, byte tableId, String srcIP, int reason, int priority) {
        // match field build
        TrafficSelector.Builder trafficSelector = DefaultTrafficSelector.builder();
        ArrayList<Criterion> matchList = new ArrayList<>();
        matchList.add(Criteria.matchOffsetLength(SIP, (short) 208, (short) 32, srcIP, "FFFFFFFF"));
        trafficSelector.add(Criteria.matchOffsetLength(matchList));;

        //action field build
        TrafficTreatment.Builder trafficeTreatm = DefaultTrafficTreatment.builder();
        List<OFAction> actions = new ArrayList<>();
        OFAction action_packetin = DefaultPofActions.packetIn(reason).action();
        actions.add(action_packetin);
        trafficeTreatm.add(DefaultPofInstructions.applyActions(actions));

        //apply
        long newFlowEntryId = flowTableStore.getNewFlowEntryId(deviceId,tableId);
        FlowRule.Builder flowRule = DefaultFlowRule.builder()
                .forDevice(deviceId)
                .forTable(tableId)
                .withSelector(trafficSelector.build())
                .withTreatment(trafficeTreatm.build())
                .withPriority(priority)
                .withCookie(newFlowEntryId)
                .makePermanent();
        flowRuleService.applyFlowRules(flowRule.build());
        log.info("Test no INT: apply to deviceId<{}> tableId<{}>, entryId=<{}>", deviceId.toString(), tableId, newFlowEntryId);
    }

    /**
     * remove all flowentries in the specific flowtable
     * @param deviceId deviceIs
     * @param tableId small table Id
     */
    public void remove_pof_flow_table(DeviceId deviceId, byte tableId){
        flowRuleService.removeFlowRulesById(appId);
        flowTableService.removeFlowTablesByTableId(deviceId, FlowTableId.valueOf(tableId));
    }

    /**
     * util tools.
     */
    public String short2HexStr(short shortNum) {
        StringBuilder hex_str = new StringBuilder();
        byte[] b = new byte[2];
        b[1] = (byte) (shortNum & 0xff);
        b[0] = (byte) ((shortNum >> 8) & 0xff);

        return bytes_to_hex_str(b);
    }

    public String byte2HexStr(byte byteNum) {
        String hex = Integer.toHexString(   byteNum & 0xff);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex;
    }

    public String funcByteHexStr(DeviceId deviceId) {
        String device = deviceId.toString().substring(18, 20);
        byte dpid = Integer.valueOf(device).byteValue();
        int k = 2, b = 1;
        byte y = (byte) (k * dpid + b);   // simple linear function
        return byte2HexStr(y);
    }

    public String bytes_to_hex_str(byte[] b) {
        StringBuilder hex_str = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xff);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hex_str.append(hex);
        }
        return hex_str.toString();
    }

    /**
     * Implementation package handler
     */
    private class ReactivePacketProcessor implements PacketProcessor{
        int flag=1;
        @Override
        public void process(PacketContext packetContext) {
            if (packetContext.isHandled()){
                return;
            }
            //InBound packet extracted from packetContext
            InboundPacket inboundPkt = packetContext.inPacket();
            Ethernet ethernet = inboundPkt.parsed();

            //Find source and destination hosts
            HostId srcId = HostId.hostId(ethernet.getSourceMAC());
            HostId dstId = HostId.hostId(ethernet.getDestinationMAC());
            log.info("HostId srcId {} and {}",srcId, dstId);

            log.info("default path {}",pathService.getPaths(srcId,dstId));
            log.info("the incoming eth is {} \ninboundPkt {}", ethernet.getEtherType(),inboundPkt);
            if (null == ethernet || ethernet.getEtherType() == ethernet.TYPE_LLDP
                                 || ethernet.getEtherType() == Ethernet.TYPE_BSN) {
                return;
            }

            /*log.info("Inboundpacket is {}  " + "ethernet(parsed) is {}   " + "   unparsed is {}" + "   Outbound is {}",
                     inboundPkt,inboundPkt.parsed(), inboundPkt.unparsed().array(),packetContext.outPacket());*/

            //log.info("inport  = {}",inboundPkt.receivedFrom().port());
            PortNumber fromPort = inboundPkt.receivedFrom().port();
            DeviceId fromDeviceId = inboundPkt.receivedFrom().deviceId();
           // log.info("======from devicesID:{}, fromPort:{}=====",fromDeviceId,fromPort);

            //Outbound packet build
            List<OFAction> actionsOutbound = new ArrayList<OFAction>();
            if(fromPort.toLong() == 1) {
                actionsOutbound.add(DefaultPofActions.output((short) 0, (short) 0, (short) 0, 2).action());
                //log.info("1");
            }else {
                actionsOutbound.add(DefaultPofActions.output((short) 0, (short) 0, (short) 0, 1).action());
                //log.info("2");
            }
            packetContext.treatmentBuilder().add(DefaultPofInstructions.applyActions(actionsOutbound));
            packetContext.send();

            //log.info("Inbound packet is {}",inboundPkt);

            if(flag == 1) {
                install_pof_no_int_output_flow_rule(deviceId1, global_smalltable_id_1[0], "0a000001", 2, 11);
                install_pof_no_int_output_flow_rule(deviceId1, global_smalltable_id_1[0], "0a000002", 1, 11);
                install_pof_no_int_output_flow_rule(deviceId2, global_smalltable_id_1[1], "0a000001", 1, 12);
                install_pof_no_int_output_flow_rule(deviceId2, global_smalltable_id_1[1], "0a000002", 2, 12);
                flag=0;
            }
        }
    }


    }
