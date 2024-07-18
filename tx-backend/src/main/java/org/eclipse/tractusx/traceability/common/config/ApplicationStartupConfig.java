/********************************************************************************
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package org.eclipse.tractusx.traceability.common.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.traceability.assets.application.base.service.AssetBaseService;
import org.eclipse.tractusx.traceability.assets.domain.base.model.AssetBase;
import org.eclipse.tractusx.traceability.contracts.application.service.ContractService;
import org.eclipse.tractusx.traceability.contracts.domain.model.ContractAgreement;
import org.eclipse.tractusx.traceability.contracts.domain.model.ContractType;
import org.eclipse.tractusx.traceability.notification.application.contract.model.CreateNotificationContractRequest;
import org.eclipse.tractusx.traceability.notification.application.contract.model.NotificationMethod;
import org.eclipse.tractusx.traceability.notification.application.contract.model.NotificationType;
import org.eclipse.tractusx.traceability.notification.domain.contract.EdcNotificationContractService;
import org.eclipse.tractusx.traceability.policies.domain.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.eclipse.tractusx.traceability.common.config.ApplicationProfiles.NOT_INTEGRATION_TESTS;
import static org.eclipse.tractusx.traceability.notification.domain.contract.EdcNotificationContractService.NOTIFICATION_CONTRACTS;


@Slf4j
@Component
@Profile(NOT_INTEGRATION_TESTS)
public class ApplicationStartupConfig {
    private final PolicyRepository policyRepository;
    private final AssetBaseService asPlannedService;
    private final AssetBaseService asBuiltService;
    private final ContractService contractService;

    @Autowired
    public ApplicationStartupConfig(PolicyRepository policyRepository,
                                    @Qualifier("assetAsBuiltServiceImpl") AssetBaseService asBuiltService,
                                    @Qualifier("assetAsPlannedServiceImpl") AssetBaseService asPlannedService,
                                    EdcNotificationContractService edcNotificationContractService,
                                    ContractService contractService) {
        this.policyRepository = policyRepository;
        this.asPlannedService = asPlannedService;
        this.asBuiltService = asBuiltService;
        this.edcNotificationContractService = edcNotificationContractService;
        this.contractService = contractService;
    }

    private final EdcNotificationContractService edcNotificationContractService;


    @EventListener(ApplicationReadyEvent.class)
    public void registerIrsPolicy() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                policyRepository.createPolicyBasedOnAppConfig();
            } catch (Exception exception) {
                log.error("Failed to create Irs Policies: ", exception);
            }
        });

        executor.shutdown();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createNotificationContracts() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            log.info("on ApplicationReadyEvent create notification contracts.");
            try {
                NOTIFICATION_CONTRACTS.forEach(edcNotificationContractService::createNotificationContract);
            } catch (Exception exception) {
                log.error("Failed to create notification contracts: ", exception);
            }
            log.info("on ApplicationReadyEvent notification contracts created.");
        });
        executor.shutdown();
    }

}
