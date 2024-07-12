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

import { Component, Input } from '@angular/core';
import { Notification } from '@shared/model/notification.model';
import { NotificationAction } from '@shared/modules/notification/notification-action.enum';
import { NotificationProcessingService } from '@shared/service/notification-processing.service';

@Component({
  selector: 'app-notification-overview',
  templateUrl: './notification-overview.component.html',
  styleUrls: [
    '../card-list/card-list.component.scss',
    '../../modules/notification/notification-tab/notification-tab.component.scss',
    './notification-overview.component.scss',
  ],
})
export class NotificationOverviewComponent {
  @Input() notification: Notification;
  @Input() showNotification = true;

  constructor(public notificationProcessingService: NotificationProcessingService) {
  }

  protected readonly NotificationAction = NotificationAction;
}
