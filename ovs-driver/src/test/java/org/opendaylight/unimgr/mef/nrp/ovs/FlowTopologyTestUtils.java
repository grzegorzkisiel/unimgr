/*
 * Copyright (c) 2017 Cisco Systems Inc and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.unimgr.mef.nrp.ovs;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.Uri;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.*;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.link.attributes.DestinationBuilder;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.link.attributes.SourceBuilder;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.Topology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.TopologyBuilder;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.TopologyKey;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Link;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.LinkBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 19.04.17.
 */
public class FlowTopologyTestUtils {
    private static final TopologyId flowTopologyId = new TopologyId(new Uri("flow:1"));
    private static final InstanceIdentifier<Topology> FLOW_TOPO_IID = InstanceIdentifier
            .create(NetworkTopology.class)
            .child(Topology.class, new TopologyKey(flowTopologyId));
    private static final String prefix = "openflow:";

    /**
     * Creates flow topology with link nodes (Links between ovs).
     */
    public static void createFlowTopology(DataBroker dataBroker, List<Link> links){
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setTopologyId(flowTopologyId);
        topologyBuilder.setLink(links);
        Topology topology = topologyBuilder.build();
        DataStoreTestUtils.write(topology,FLOW_TOPO_IID,dataBroker);
    }

    public static Link createLink(String sourceNode, Long sourcePort, String destNode, Long destPort){
        LinkBuilder linkBuilder = new LinkBuilder();
        String sourcePortName = prefix + sourceNode + ":" + sourcePort.toString();
        String destPortName =  prefix + destNode + ":" + destPort;

        linkBuilder.setLinkId(new LinkId(sourcePortName));

        SourceBuilder sourceBuilder = new SourceBuilder();
        sourceBuilder.setSourceTp(new TpId(sourcePortName));
        sourceBuilder.setSourceNode(new NodeId(prefix+sourceNode));
        linkBuilder.setSource(sourceBuilder.build());

        DestinationBuilder destinationBuilder = new DestinationBuilder();
        destinationBuilder.setDestTp(new TpId(destPortName));
        destinationBuilder.setDestNode(new NodeId(prefix+destNode));
        linkBuilder.setDestination(destinationBuilder.build());

        return linkBuilder.build();
    }

}