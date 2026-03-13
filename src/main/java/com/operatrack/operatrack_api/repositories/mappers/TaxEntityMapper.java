package com.operatrack.operatrack_api.repositories.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.operatrack.operatrack_api.model.Tax;
import com.operatrack.operatrack_api.repositories.entities.TaxEntity;

@Mapper(componentModel = "spring")
public interface TaxEntityMapper {

    @Mapping(target = "operations", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TaxEntity toEntity(Tax tax);

    default Tax toDomain(TaxEntity entity) {
        return new Tax(entity.getId(), entity.getInstitutionName(), entity.getTaxRate());
    }
}
