package com.operatrack.operatrack_api.repositories.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.operatrack.operatrack_api.model.Stock;
import com.operatrack.operatrack_api.repositories.entities.StockEntity;

@Mapper(componentModel = "spring")
public interface StockEntityMapper {

    @Mapping(target = "operations", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    StockEntity toEntity(Stock stock);

    default Stock toDomain(StockEntity entity) {
        return new Stock(entity.getId(), entity.getName(), entity.getTickerSymbol(), entity.getCurrentPrice());
    }
}
