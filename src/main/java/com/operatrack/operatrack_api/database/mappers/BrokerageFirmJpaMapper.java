package com.operatrack.operatrack_api.database.mappers;

import com.operatrack.operatrack_api.database.entities.BrokerageFirmEntity;
import com.operatrack.operatrack_api.model.BrokerageFirm;

public class BrokerageFirmJpaMapper {

    private BrokerageFirmJpaMapper() {}

    public static BrokerageFirm toDomain(BrokerageFirmEntity entity) {
        return new BrokerageFirm(
                entity.getId(),
                entity.getInstitutionName(),
                entity.getTaxRate()
        );
    }

    public static BrokerageFirmEntity toEntity(BrokerageFirm brokerageFirm) {
        return BrokerageFirmEntity.builder()
                .id(brokerageFirm.getId())
                .institutionName(brokerageFirm.getInstitutionName())
                .taxRate(brokerageFirm.getTaxRate())
                .build();
    }
}
