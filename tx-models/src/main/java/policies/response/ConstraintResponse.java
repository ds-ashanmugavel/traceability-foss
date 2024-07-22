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
package policies.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.eclipse.tractusx.irs.edc.client.policy.Constraint;
import org.eclipse.tractusx.irs.edc.client.policy.Operator;
import org.eclipse.tractusx.irs.edc.client.policy.OperatorType;
import org.eclipse.tractusx.irs.edc.client.policy.Permission;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public record ConstraintResponse(
        @Schema(
                implementation = String.class,
                example = "string"
        )
        @JsonAlias({"odrl:leftOperand"})
        String leftOperand,

        @Schema
        OperatorTypeResponse operatorTypeResponse,
        @Schema(
                implementation = String.class,
                example = "string"
        )
        @JsonAlias({"odrl:rightOperand"})
        String rightOperand) {



    public static List<Constraint> toDomain(List<ConstraintResponse> constraintResponse) {
        if (constraintResponse == null) {
            return null;
        }
        return constraintResponse.stream()
                .map(ConstraintResponse::toDomain)
                .collect(Collectors.toList());
    }
    public static Constraint toDomain(ConstraintResponse constraintResponse){
        if (constraintResponse == null) {
            return null;
        }
        Operator operator = new Operator(toDomain(constraintResponse.operatorTypeResponse));

        return new Constraint(
                constraintResponse.leftOperand(),
                operator,
                constraintResponse.rightOperand()
        );
    }

    public static OperatorType toDomain(OperatorTypeResponse operatorTypeResponse) {
        if (operatorTypeResponse == null) {
            return null;
        }

        return Arrays.stream(OperatorType.values())
                .filter(type -> type.getCode().equals(operatorTypeResponse.code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + operatorTypeResponse.code));
    }
}
