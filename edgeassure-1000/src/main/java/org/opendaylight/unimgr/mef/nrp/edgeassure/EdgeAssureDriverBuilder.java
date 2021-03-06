/*
 * Copyright (c) 2016 Microsemi and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.unimgr.mef.nrp.edgeassure;

import java.util.List;
import java.util.Optional;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.MountPointService;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.unimgr.mef.nrp.api.ActivationDriver;
import org.opendaylight.unimgr.mef.nrp.api.ActivationDriverBuilder;
import org.opendaylight.unimgr.mef.nrp.api.EndPoint;
import org.opendaylight.unimgr.mef.nrp.common.FixedServiceNaming;
import org.opendaylight.unimgr.mef.nrp.common.ResourceNotAvailableException;
import org.opendaylight.yang.gen.v1.urn.mef.yang.nrp._interface.rev170712.NrpConnectivityServiceAttrs;
import org.opendaylight.yang.gen.v1.urn.mef.yang.tapi.common.rev170712.Uuid;

/**
 * Fake driver builder;
 * @author sean.condon@microsemi.com
 */
public class EdgeAssureDriverBuilder implements ActivationDriverBuilder {

    private final FixedServiceNaming namingProvider;
    private final EdgeAssureActivator edgeAssureActivator;

    EdgeAssureDriverBuilder(DataBroker dataBroker, MountPointService mountService) {
        this.namingProvider = new FixedServiceNaming();
        edgeAssureActivator = new EdgeAssureActivator(dataBroker, mountService);
    }

    @Override
    public Optional<ActivationDriver> driverFor(BuilderContext context) {
        final ActivationDriver driver = new ActivationDriver() {
            List<EndPoint> endPoints;
            String serviceId;

            @Override
            public void commit() {
                //ignore for the moment
            }

            @Override
            public void rollback() {
                //ignore for the moment
            }

            @Override
            public void initialize(List<EndPoint> endPoints, String serviceId, NrpConnectivityServiceAttrs context) {
                this.endPoints = endPoints;
                this.serviceId = serviceId;
            }

            @Override
            public void activate() throws TransactionCommitFailedException, ResourceNotAvailableException {
                edgeAssureActivator.activate(endPoints,serviceId);
            }

            @Override
            public void deactivate() throws ResourceNotAvailableException, TransactionCommitFailedException {
                edgeAssureActivator.deactivate(endPoints,serviceId);
            }

            @Override
            public int priority() {
                return 0;
            }
        };

        return Optional.of(driver);

    }

    @Override
    public Uuid getNodeUuid() {
        return null;
    }
}
