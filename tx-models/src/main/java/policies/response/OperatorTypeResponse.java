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


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import org.eclipse.tractusx.irs.edc.client.policy.OperatorType;

import java.util.NoSuchElementException;
import java.util.stream.Stream;


@JsonSerialize(
        using = ToStringSerializer.class
)
@Getter
public enum OperatorTypeResponse {
    EQ("eq", "Equals to"),

    NEQ("neq", "Not equal to"),

    LT("lt", "Less than"),

    GT("gt", "Greater than"),

    IN("in", "In"),

    LTEQ("lteq", "Less than or equal to"),

    GTEQ("gteq", "Greater than or equal to"),

    ISA("isA", "Is a"),

    HASPART("hasPart", "Has part"),

    ISPARTOF("isPartOf", "Is part of"),

    ISONEOF("isOneOf", "Is one of"),

    ISALLOF("isAllOf", "Is all of"),

    ISNONEOF("isNoneOf", "Is none of");

    final String code;

    final String label;

    OperatorTypeResponse(String code, String label) {
        this.code = code;
        this.label = label;
    }

    @JsonCreator
    public static OperatorTypeResponse fromValue(String value) {
        String operator;
        if (value.startsWith("odrl:")) {
            operator = value.substring(5);
        } else {
            operator = value;
        }

        return (OperatorTypeResponse) Stream.of(values()).filter((operatorType) -> {
            return operatorType.code.equals(operator);
        }).findFirst().orElseThrow(() -> {
            return new NoSuchElementException("Unsupported OperatorType: " + operator);
        });
        }

}
