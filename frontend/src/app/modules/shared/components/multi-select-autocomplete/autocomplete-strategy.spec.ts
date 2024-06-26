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
import { Owner } from '@page/parts/model/owner.enum';
import {
  channelOfNotification,
  isAsBuilt,
} from '@shared/components/multi-select-autocomplete/autocomplete-strategy';
import { NotificationChannel, TableType } from '@shared/components/multi-select-autocomplete/table-type.model';

describe('Autocomplete Strategies', () => {

  it('should determine isAsBuilt correctly', async () => {
    expect(isAsBuilt(TableType.AS_BUILT_OWN)).toBeTrue();
    expect(isAsBuilt(TableType.AS_PLANNED_OWN)).toBeFalse();
  });

  it('should determine channel of notification', async () => {
    expect(channelOfNotification(TableType.SENT_NOTIFICATION)).toBe(NotificationChannel.SENDER);
    expect(channelOfNotification(TableType.RECEIVED_NOTIFICATION)).toBe(NotificationChannel.RECEIVER);
  });
});
