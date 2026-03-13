package com.operatrack.operatrack_api.repositories.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.operatrack.operatrack_api.model.Operation;
import com.operatrack.operatrack_api.model.Tax;
import com.operatrack.operatrack_api.repositories.entities.OperationEntity;
import com.operatrack.operatrack_api.repositories.entities.StockEntity;
import com.operatrack.operatrack_api.repositories.entities.TaxEntity;

@Mapper(componentModel = "spring")
public interface OperationEntityMapper {

    @Mapping(source = "operation.id", target = "id")
    @Mapping(source = "operation.shareQuantity", target = "shareQuantity")
    @Mapping(source = "operation.purchasePrice", target = "purchasePrice")
    @Mapping(source = "operation.totalValue", target = "totalValue")
    @Mapping(source = "operation.capitalGain", target = "capitalGain")
    @Mapping(source = "operation.purchaseTax", target = "purchaseTax")
    @Mapping(source = "operation.saleTax", target = "saleTax")
    @Mapping(source = "operation.totalTax", target = "totalTax")
    @Mapping(source = "operation.netEarnings", target = "netEarnings")
    @Mapping(source = "operation.purchaseDate", target = "purchaseDate")
    @Mapping(source = "operation.saleDate", target = "saleDate")
    @Mapping(source = "stockEntity", target = "stock")
    @Mapping(source = "taxEntity", target = "tax")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OperationEntity toEntity(Operation operation, StockEntity stockEntity, TaxEntity taxEntity);

    default Operation toDomain(OperationEntity entity) {
        Tax tax = new Tax(
                entity.getTax().getId(),
                entity.getTax().getInstitutionName(),
                entity.getTax().getTaxRate());
        return new Operation(
                entity.getId(),
                entity.getShareQuantity(),
                entity.getPurchasePrice(),
                entity.getStock().getCurrentPrice(),
                entity.getTotalValue(),
                entity.getCapitalGain(),
                entity.getNetEarnings(),
                entity.getPurchaseDate(),
                entity.getSaleDate(),
                entity.getStock().getId(),
                tax);
    }
}
