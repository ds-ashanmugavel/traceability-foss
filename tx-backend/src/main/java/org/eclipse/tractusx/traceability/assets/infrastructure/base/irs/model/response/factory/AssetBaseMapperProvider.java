package org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response.factory;

import lombok.RequiredArgsConstructor;
import org.eclipse.tractusx.irs.component.Relationship;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response.IrsSubmodel;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response.mapping.asbuilt.AsBuiltDetailMapper;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response.mapping.asplanned.AsPlannedDetailMapper;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response.mapping.relationship.RelationshipMapper;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response.mapping.submodel.SubmodelMapper;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response.mapping.submodel.relationship.SubmodelRelationshipMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AssetBaseMapperProvider {
    private final List<SubmodelMapper> baseMappers;
    private final List<SubmodelRelationshipMapper> submodelRelationshipMapper;
    private final List<RelationshipMapper> relationshipMappers;
    private final List<AsPlannedDetailMapper> asPlannedDetailMappers;
    private final List<AsBuiltDetailMapper> asBuiltDetailMappers;

    public Optional<SubmodelMapper> getMainSubmodelMapper(IrsSubmodel irsSubmodel) {
        return baseMappers.stream().filter(assetBaseMapper -> assetBaseMapper.validMapper(irsSubmodel)).findFirst();
    }

    public Optional<SubmodelRelationshipMapper> getRelationshipSubmodelMapper(IrsSubmodel irsSubmodel) {
        return submodelRelationshipMapper.stream().filter(assetBaseMapper -> assetBaseMapper.validMapper(irsSubmodel)).findFirst();
    }

    public Optional<RelationshipMapper> getRelationshipMapper(Relationship relationship) {
        return relationshipMappers.stream().filter(relationshipMapper -> relationshipMapper.validMapper(relationship)).findFirst();
    }

    public Optional<AsPlannedDetailMapper> getAsPlannedDetailMapper(IrsSubmodel irsSubmodel) {
        return asPlannedDetailMappers.stream().filter(asPlannedDetailMapper -> asPlannedDetailMapper.validMapper(irsSubmodel)).findFirst();
    }

    public Optional<AsBuiltDetailMapper> getAsBuiltDetailMapper(IrsSubmodel irsSubmodel) {
        return asBuiltDetailMappers.stream().filter(asBuiltDetailMapper -> asBuiltDetailMapper.validMapper(irsSubmodel)).findFirst();
    }
}
