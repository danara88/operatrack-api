package com.operatrack.operatrack_api.database.mappers;

import com.operatrack.operatrack_api.database.entities.OperationEntity;
import com.operatrack.operatrack_api.database.entities.StockEntity;
import com.operatrack.operatrack_api.database.entities.TaxEntity;
import com.operatrack.operatrack_api.model.Operation;
import com.operatrack.operatrack_api.model.Tax;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class OperationJpaMapperTest {

    private static final String OPERATION_ID = "op-uuid-1";
    private static final String STOCK_ID = "stock-uuid-1";
    private static final String TAX_ID = "tax-uuid-1";

    private StockEntity buildStockEntity() {
        return StockEntity.builder()
                .id(STOCK_ID)
                .name("Apple Inc.")
                .tickerSymbol("AAPL")
                .currentPrice(180.0)
                .build();
    }

    private TaxEntity buildTaxEntity() {
        return TaxEntity.builder()
                .id(TAX_ID)
                .institutionName("Scotiabank")
                .taxRate(0.0035)
                .build();
    }

    private OperationEntity buildOperationEntity(StockEntity stockEntity, TaxEntity taxEntity) {
        return OperationEntity.builder()
                .id(OPERATION_ID)
                .shareQuantity(10)
                .purchasePrice(150.0)
                .totalValue(1500.0)
                .capitalGain(50.0)
                .purchaseTax(6.09)
                .saleTax(7.308)
                .totalTax(13.398)
                .netEarnings(36.602)
                .purchaseDate(Instant.parse("2024-01-15T10:00:00Z"))
                .saleDate(null)
                .stock(stockEntity)
                .tax(taxEntity)
                .build();
    }

    // --- toDomain ---

    @Test
    void toDomain_mapsAllFieldsCorrectly() {
        StockEntity stockEntity = buildStockEntity();
        TaxEntity taxEntity = buildTaxEntity();
        OperationEntity entity = buildOperationEntity(stockEntity, taxEntity);

        Operation operation = OperationJpaMapper.toDomain(entity);

        assertEquals(OPERATION_ID, operation.getId());
        assertEquals(10, operation.getShareQuantity());
        assertEquals(150.0, operation.getPurchasePrice());
        assertEquals(1500.0, operation.getTotalValue());
        assertEquals(50.0, operation.getCapitalGain());
        assertEquals(6.09, operation.getPurchaseTax());
        assertEquals(7.308, operation.getSaleTax());
        assertEquals(13.398, operation.getTotalTax());
        assertEquals(36.602, operation.getNetEarnings());
        assertEquals(Instant.parse("2024-01-15T10:00:00Z"), operation.getPurchaseDate());
        assertNull(operation.getSaleDate());
        assertEquals(STOCK_ID, operation.getStockId());
    }

    @Test
    void toDomain_mapsSaleDateWhenPresent() {
        StockEntity stockEntity = buildStockEntity();
        TaxEntity taxEntity = buildTaxEntity();
        Instant saleDate = Instant.parse("2024-06-01T12:00:00Z");
        OperationEntity entity = buildOperationEntity(stockEntity, taxEntity);
        entity.setSaleDate(saleDate);

        Operation operation = OperationJpaMapper.toDomain(entity);

        assertEquals(saleDate, operation.getSaleDate());
    }

    @Test
    void toDomain_mapsZeroTaxValues() {
        StockEntity stockEntity = buildStockEntity();
        TaxEntity taxEntity = TaxEntity.builder()
                .id(TAX_ID)
                .institutionName("Zero Bank")
                .taxRate(0.0)
                .build();
        OperationEntity entity = OperationEntity.builder()
                .id(OPERATION_ID)
                .shareQuantity(10)
                .purchasePrice(150.0)
                .totalValue(1500.0)
                .capitalGain(0.0)
                .purchaseTax(0.0)
                .saleTax(0.0)
                .totalTax(0.0)
                .netEarnings(0.0)
                .purchaseDate(Instant.now())
                .stock(stockEntity)
                .tax(taxEntity)
                .build();

        Operation operation = OperationJpaMapper.toDomain(entity);

        assertEquals(0.0, operation.getPurchaseTax());
        assertEquals(0.0, operation.getSaleTax());
        assertEquals(0.0, operation.getTotalTax());
    }

    // --- toEntity ---

    @Test
    void toEntity_mapsAllFieldsCorrectly() {
        Tax tax = new Tax("BBVA", 0.0035);
        Operation operation = new Operation(
                OPERATION_ID, 10, 150.0, 160.0, 1500.0, 50.0,
                35.0, Instant.parse("2024-01-15T10:00:00Z"), null, STOCK_ID, tax
        );
        StockEntity stockEntity = buildStockEntity();
        TaxEntity taxEntity = buildTaxEntity();

        OperationEntity entity = OperationJpaMapper.toEntity(operation, stockEntity, taxEntity);

        assertEquals(OPERATION_ID, entity.getId());
        assertEquals(10, entity.getShareQuantity());
        assertEquals(150.0, entity.getPurchasePrice());
        assertEquals(1500.0, entity.getTotalValue());
        assertEquals(50.0, entity.getCapitalGain());
        assertEquals(operation.getPurchaseTax(), entity.getPurchaseTax());
        assertEquals(operation.getSaleTax(), entity.getSaleTax());
        assertEquals(operation.getTotalTax(), entity.getTotalTax());
        assertEquals(35.0, entity.getNetEarnings());
        assertEquals(Instant.parse("2024-01-15T10:00:00Z"), entity.getPurchaseDate());
        assertNull(entity.getSaleDate());
        assertEquals(stockEntity, entity.getStock());
        assertEquals(taxEntity, entity.getTax());
    }

    @Test
    void toEntity_mapsSaleDateWhenPresent() {
        Tax tax = new Tax("BBVA", 0.0035);
        Instant saleDate = Instant.parse("2024-06-01T12:00:00Z");
        Operation operation = new Operation(
                OPERATION_ID, 10, 150.0, 160.0, 1500.0, 50.0,
                35.0, Instant.parse("2024-01-15T10:00:00Z"), saleDate, STOCK_ID, tax
        );
        StockEntity stockEntity = buildStockEntity();
        TaxEntity taxEntity = buildTaxEntity();

        OperationEntity entity = OperationJpaMapper.toEntity(operation, stockEntity, taxEntity);

        assertEquals(saleDate, entity.getSaleDate());
    }

    @Test
    void toDomain_andToEntity_areSymmetric() {
        StockEntity stockEntity = buildStockEntity();
        TaxEntity taxEntity = buildTaxEntity();
        OperationEntity original = buildOperationEntity(stockEntity, taxEntity);

        Operation domain = OperationJpaMapper.toDomain(original);
        OperationEntity roundTripped = OperationJpaMapper.toEntity(domain, stockEntity, taxEntity);

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getShareQuantity(), roundTripped.getShareQuantity());
        assertEquals(original.getPurchasePrice(), roundTripped.getPurchasePrice());
        assertEquals(original.getTotalValue(), roundTripped.getTotalValue());
        assertEquals(original.getCapitalGain(), roundTripped.getCapitalGain());
        assertEquals(original.getPurchaseTax(), roundTripped.getPurchaseTax());
        assertEquals(original.getSaleTax(), roundTripped.getSaleTax());
        assertEquals(original.getTotalTax(), roundTripped.getTotalTax());
        assertEquals(original.getNetEarnings(), roundTripped.getNetEarnings());
        assertEquals(original.getPurchaseDate(), roundTripped.getPurchaseDate());
        assertEquals(original.getSaleDate(), roundTripped.getSaleDate());
        assertEquals(original.getStock().getId(), roundTripped.getStock().getId());
    }
}
