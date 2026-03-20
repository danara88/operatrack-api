package com.operatrack.operatrack_api.database.mappers;

import com.operatrack.operatrack_api.database.entities.OperationEntity;
import com.operatrack.operatrack_api.database.entities.StockEntity;
import com.operatrack.operatrack_api.database.entities.TaxEntity;
import com.operatrack.operatrack_api.model.Operation;

public class OperationJpaMapper {

    private OperationJpaMapper() {}

    public static Operation toDomain(OperationEntity entity) {
        return new Operation(
                entity.getId(),
                entity.getShareQuantity(),
                entity.getPurchasePrice(),
                entity.getTotalValue(),
                entity.getCapitalGain(),
                entity.getPurchaseTax(),
                entity.getSaleTax(),
                entity.getTotalTax(),
                entity.getNetEarnings(),
                entity.getPurchaseDate(),
                entity.getSaleDate(),
                entity.getStock().getId()
        );
    }

    public static OperationEntity toEntity(Operation operation, StockEntity stockEntity, TaxEntity taxEntity) {
        return OperationEntity.builder()
                .id(operation.getId())
                .shareQuantity(operation.getShareQuantity())
                .purchasePrice(operation.getPurchasePrice())
                .totalValue(operation.getTotalValue())
                .capitalGain(operation.getCapitalGain())
                .purchaseTax(operation.getPurchaseTax())
                .saleTax(operation.getSaleTax())
                .totalTax(operation.getTotalTax())
                .netEarnings(operation.getNetEarnings())
                .purchaseDate(operation.getPurchaseDate())
                .saleDate(operation.getSaleDate())
                .stock(stockEntity)
                .tax(taxEntity)
                .build();
    }
}
