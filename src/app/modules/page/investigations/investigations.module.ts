/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { getI18nPageProvider } from '@core/i18n';
import { SharedModule } from '@shared/shared.module';
import { TemplateModule } from '@shared/template.module';
import { InvestigationsFacade } from './core/investigations.facade';
import { InvestigationsState } from './core/investigations.state';
import { InvestigationsRoutingModule } from './investigations.routing';
import { InvestigationStatusComponent } from './presentation/investigation-status/investigation-status.component';
import { InvestigationsTabComponent } from './presentation/investigations-tab/investigations-tab.component';
import { InvestigationsComponent } from './presentation/investigations.component';

@NgModule({
  declarations: [InvestigationsComponent, InvestigationsTabComponent, InvestigationStatusComponent],
  imports: [CommonModule, TemplateModule, SharedModule, InvestigationsRoutingModule],
  providers: [InvestigationsFacade, InvestigationsState, ...getI18nPageProvider('page.investigations')],
})
export class InvestigationsModule {}
