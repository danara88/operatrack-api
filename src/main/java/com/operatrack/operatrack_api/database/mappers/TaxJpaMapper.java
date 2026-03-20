package com.operatrack.operatrack_api.database.mappers;

import com.operatrack.operatrack_api.database.entities.TaxEntity;
import com.operatrack.operatrack_api.model.Tax;

public class TaxJpaMapper {

    private TaxJpaMapper() {}

    public static Tax toDomain(TaxEntity entity) {
        return new Tax(
                entity.getId(),
                entity.getInstitutionName(),
                entity.getTaxRate()
        );
    }

    public static TaxEntity toEntity(Tax tax) {
        return TaxEntity.builder()
                .id(tax.getId())
                .institutionName(tax.getInstitutionName())
                .taxRate(tax.getTaxRate())
                .build();
    }
}
