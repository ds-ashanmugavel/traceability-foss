/********************************************************************************
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package org.eclipse.tractusx.traceability.contracts.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.traceability.contracts.application.mapper.ContractFieldMapper;
import org.eclipse.tractusx.traceability.contracts.domain.repository.ContractRepository;
import org.eclipse.tractusx.traceability.contracts.infrastructure.repository.ContractNotificationRepositoryImpl;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContractNotificationServiceImpl extends AbstractContractBaseService {

    private final ContractNotificationRepositoryImpl contractNotificationRepository;
    private final ContractFieldMapper contractFieldMapper;

    @Override
    protected ContractRepository getContractRepository() {
        return contractNotificationRepository;
    }

    @Override
    protected ContractFieldMapper getContractFieldMapper() {
        return contractFieldMapper;
    }

}
