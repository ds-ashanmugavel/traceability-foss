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

package org.eclipse.tractusx.traceability.assets.application.asplanned.rest;

import assets.importpoc.ErrorResponse;
import assets.request.SearchableAssetsRequest;
import assets.response.asplanned.AssetAsPlannedResponse;
import assets.response.base.request.UpdateAssetRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.eclipse.tractusx.traceability.assets.application.asbuilt.mapper.QualityTypeMapper;
import org.eclipse.tractusx.traceability.assets.application.asplanned.mapper.AssetAsPlannedFieldMapper;
import org.eclipse.tractusx.traceability.assets.application.asplanned.mapper.AssetAsPlannedResponseMapper;
import org.eclipse.tractusx.traceability.assets.application.base.mapper.OwnerTypeMapper;
import org.eclipse.tractusx.traceability.assets.application.base.request.GetDetailInformationRequest;
import org.eclipse.tractusx.traceability.assets.application.base.request.SyncAssetsRequest;
import org.eclipse.tractusx.traceability.assets.application.base.service.AssetBaseService;
import org.eclipse.tractusx.traceability.common.model.BaseRequestFieldMapper;
import org.eclipse.tractusx.traceability.common.model.PageResult;
import org.eclipse.tractusx.traceability.common.request.OwnPageable;
import org.eclipse.tractusx.traceability.common.request.SearchCriteriaRequestParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERVISOR', 'ROLE_USER')")
@Tag(name = "AssetsAsPlanned")
@RequestMapping(path = "/assets/as-planned", produces = "application/json", consumes = "application/json")
public class AssetAsPlannedController {

    private final AssetBaseService assetService;
    private final BaseRequestFieldMapper fieldMapper;

    public AssetAsPlannedController(
            @Qualifier("assetAsPlannedServiceImpl") AssetBaseService assetService,
            AssetAsPlannedFieldMapper fieldMapper) {
        this.assetService = assetService;
        this.fieldMapper = fieldMapper;
    }

    @Operation(operationId = "sync",
            summary = "Synchronizes assets from IRS",
            tags = {"AssetsAsPlanned"},
            description = "The endpoint synchronizes the assets from irs.",
            security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created."),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authorization failed.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),

            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "415",
                    description = "Unsupported media type",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/sync")
    public void sync(@Valid @RequestBody SyncAssetsRequest syncAssetsRequest) {
        assetService.synchronizeAssetsAsync(syncAssetsRequest.globalAssetIds());
    }
    @Operation(operationId = "AssetsAsPlanned",
            summary = "Get assets by pagination",
            tags = {"AssetsAsPlanned"},
            description = "The endpoint returns a paged result of assets.",
            security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returns the paged result found for Asset", content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(
                    schema = @Schema(implementation = AssetAsPlannedResponse.class),
                    arraySchema = @Schema(
                            description = "AssetsAsPlanned",
                            implementation = AssetAsPlannedResponse.class,
                            additionalProperties = Schema.AdditionalPropertiesValue.FALSE
                    ),
                    maxItems = Integer.MAX_VALUE,
                    minItems = 0)
    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authorization failed.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),

            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "415",
                    description = "Unsupported media type",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("")
    public PageResult<AssetAsPlannedResponse> assets(OwnPageable pageable, SearchCriteriaRequestParam filter) {
        return AssetAsPlannedResponseMapper.from(assetService.getAssets(OwnPageable.toPageable(pageable, fieldMapper), filter.toSearchCriteria(fieldMapper)));
    }

    @Operation(operationId = "searchable-values",
            summary = "Get searchable values for a fieldName",
            tags = {"Assets"},
            description = "The endpoint returns searchable values for given fieldName.",
            security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returns searchable values for given fieldName.", content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(
                    schema = @Schema(
                            description = "SearchableValues",
                            implementation = String.class,
                            additionalProperties = Schema.AdditionalPropertiesValue.FALSE
                    ),
                    maxItems = Integer.MAX_VALUE,
                    minItems = 0)
    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authorization failed.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),

            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "415",
                    description = "Unsupported media type",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("searchable-values")
    public List<String> searchableValues(@Valid @RequestBody SearchableAssetsRequest request) {
        return assetService.getSearchableValues(fieldMapper.mapRequestFieldName(request.fieldName()),
                request.startWith(), request.size(), OwnerTypeMapper.from(request.owner()), request.inAssetIds());
    }

    @Operation(operationId = "assetById",
            summary = "Get asset by id",
            tags = {"AssetsAsPlanned"},
            description = "The endpoint returns an asset filtered by id .",
            security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returns the assets found",
            content = {@Content(schema = @Schema(implementation = AssetAsPlannedResponse.class))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authorization failed.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),

            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "415",
                    description = "Unsupported media type",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/{assetId}")
    public AssetAsPlannedResponse getAssetById(@PathVariable("assetId") String assetId) {
        return AssetAsPlannedResponseMapper.from(assetService.getAssetById(assetId));
    }


    @Operation(operationId = "assetByChildIdAndAssetId",
            summary = "Get asset by child id",
            tags = {"AssetsAsPlanned"},
            description = "The endpoint returns an asset filtered by child id.",
            security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returns the asset by childId",
            content = {@Content(schema = @Schema(implementation = AssetAsPlannedResponse.class))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authorization failed.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),

            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "415",
                    description = "Unsupported media type",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/*/children/{childId}")
    public AssetAsPlannedResponse getAssetByChildId(@PathVariable("childId") String childId) {
        return AssetAsPlannedResponseMapper.from(assetService.getAssetByChildId(childId));
    }

    @Operation(operationId = "updateAsset",
            summary = "Updates asset",
            tags = {"AssetsAsPlanned"},
            description = "The endpoint updates asset by provided quality type.",
            security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returns the updated asset",
            content = {@Content(schema = @Schema(implementation = AssetAsPlannedResponse.class))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authorization failed.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),

            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "415",
                    description = "Unsupported media type",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping("/{assetId}")
    public AssetAsPlannedResponse updateAsset(@PathVariable("assetId") String assetId, @Valid @RequestBody UpdateAssetRequest updateAssetRequest) {
        return AssetAsPlannedResponseMapper.from(
                assetService.updateQualityType(assetId, QualityTypeMapper.toDomain(updateAssetRequest.qualityType())));
    }

    @Operation(operationId = "getDetailInformation",
            summary = "Searches for assets by ids.",
            tags = {"AssetsAsPlanned"},
            description = "The endpoint searchs for assets by id and returns a list of them.",
            security = @SecurityRequirement(name = "oAuth2", scopes = "profile email"))
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returns the paged result found for Asset", content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(
                    schema = @Schema(
                            description = "AssetsAsPlanned",
                            implementation = AssetAsPlannedResponse.class,
                            additionalProperties = Schema.AdditionalPropertiesValue.FALSE
                    ),
                    maxItems = Integer.MAX_VALUE,
                    minItems = 0)
    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authorization failed.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),

            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "415",
                    description = "Unsupported media type",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/detail-information")
    public List<AssetAsPlannedResponse> getDetailInformation(@Valid @RequestBody GetDetailInformationRequest getDetailInformationRequest) {
        return AssetAsPlannedResponseMapper.from(
                assetService.getAssetsById(getDetailInformationRequest.assetIds())
        );
    }
}
