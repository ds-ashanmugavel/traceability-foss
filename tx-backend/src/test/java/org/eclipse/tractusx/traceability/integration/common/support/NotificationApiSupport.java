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

package org.eclipse.tractusx.traceability.integration.common.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lombok.RequiredArgsConstructor;
import notification.request.EditNotificationRequest;
import notification.request.StartNotificationRequest;
import notification.response.NotificationResponse;
import org.eclipse.tractusx.traceability.common.model.PageResult;
import org.eclipse.tractusx.traceability.common.request.OwnPageable;
import org.eclipse.tractusx.traceability.common.request.PageableFilterRequest;
import org.eclipse.tractusx.traceability.common.request.SearchCriteriaRequestParam;
import org.hamcrest.Matchers;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.eclipse.tractusx.traceability.common.security.JwtRole.SUPERVISOR;

@Component
@RequiredArgsConstructor
public class NotificationApiSupport {

    private final AssetsSupport assetsSupport;
    private final ObjectMapper objectMapper;

    public int createNotificationRequest_withDefaultAssetsStored(Header authHeader, StartNotificationRequest startNotificationRequest) throws JsonProcessingException {

        assetsSupport.defaultAssetsStored();

        // when
        Response response = given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(startNotificationRequest))
                .header(authHeader)
                .when()
                .post("/api/notifications")
                .then()
                .statusCode(201)
                .body("id", Matchers.isA(Number.class))
                .extract()
                .response();

        return response.path("id");

    }

    public void editNotificationRequest(Header authHeader, EditNotificationRequest editNotificationRequest, int notificationId) throws JsonProcessingException {

        // when
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(editNotificationRequest))
                .header(authHeader)
                .when()
                .put("/api/notifications/" + notificationId + "/edit")
                .then()
                .statusCode(204);
    }

    public PageResult<NotificationResponse> getNotificationsRequest(Header authHeader){
        Response response = given()
                .header(authHeader)
                .body(new PageableFilterRequest(new OwnPageable(0, 10, Collections.emptyList()), new SearchCriteriaRequestParam(List.of("channel,EQUAL,SENDER,AND"))))
                .contentType(ContentType.JSON)
                .when()
                .post("/api/notifications/filter")
                .then()
                .statusCode(200)
                .extract().response();

        return response.as(new TypeRef<PageResult<NotificationResponse>>() {});

    }
}
