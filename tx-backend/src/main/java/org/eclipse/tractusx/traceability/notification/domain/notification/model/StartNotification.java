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

package org.eclipse.tractusx.traceability.notification.domain.notification.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import notification.request.StartNotificationRequest;
import org.eclipse.tractusx.traceability.notification.domain.base.model.NotificationSeverity;
import org.eclipse.tractusx.traceability.notification.domain.base.model.NotificationType;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
@Data
public class StartNotification {

    private String title;

    private List<String> affectedPartIds;

    private String description;

    private Instant targetDate;

    private NotificationSeverity severity;

    private NotificationType type;

    private String receiverBpn;


    public static StartNotification from(StartNotificationRequest startNotificationRequest) {
        return StartNotification.builder()
                .title(startNotificationRequest.getTitle())
                .affectedPartIds(startNotificationRequest.getAffectedPartIds())
                .description(startNotificationRequest.getDescription())
                .targetDate(startNotificationRequest.getTargetDate())
                .severity(NotificationSeverity.from(startNotificationRequest.getSeverity()))
                .type(NotificationType.from(startNotificationRequest.getType()))
                .receiverBpn(startNotificationRequest.getReceiverBpn())
                .build();
    }


}
