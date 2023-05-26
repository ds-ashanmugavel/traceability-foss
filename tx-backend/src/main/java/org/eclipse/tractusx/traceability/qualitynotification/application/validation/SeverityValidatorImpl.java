/********************************************************************************
 * Copyright (c) 2022, 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 * Copyright (c) 2022, 2023 ZF Friedrichshafen AG
 * Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.tractusx.traceability.qualitynotification.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.eclipse.tractusx.traceability.qualitynotification.domain.model.QualityNotificationSeverity;

public class SeverityValidatorImpl implements ConstraintValidator<ValidSeverity, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // do not validate notNull
        if (value == null) {
            return true;
        }

        try {
            QualityNotificationSeverity[] severities = QualityNotificationSeverity.values();
            for (QualityNotificationSeverity severity : severities) {
                if (severity.getRealName().equals(value)) {
                    return true;
                }
            }
            QualityNotificationSeverity.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}