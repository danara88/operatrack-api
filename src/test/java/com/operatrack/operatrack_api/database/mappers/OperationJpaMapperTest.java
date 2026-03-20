package com.operatrack.operatrack_api.database.mappers;

import com.operatrack.operatrack_api.database.entities.OperationEntity;
import com.operatrack.operatrack_api.database.entities.StockEntity;
import com.operatrack.operatrack_api.database.entities.BrokerageFirmEntity;
import com.operatrack.operatrack_api.model.Operation;
import com.operatrack.operatrack_api.model.BrokerageFirm;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class OperationJpaMapperTest {

    private static final String OPERATION_ID = "op-uuid-1";
    private static final String STOCK_ID = "stock-uuid-1";
    private static final String BROKERAGE_FIRM_ID = "tax-uuid-1";

    private StockEntity buildStockEntity() {
        return StockEntity.builder()
                .id(STOCK_ID)
                .name("Apple Inc.")
                .tickerSymbol("AAPL")
                .currentPrice(180.0)
                .build();
    }

    private BrokerageFirmEntity buildBrokerageFirmEntity() {
        return BrokerageFirmEntity.builder()
                .id(BROKERAGE_FIRM_ID)
                .institutionName("Scotiabank")
                .taxRate(0.0035)
                .build();
    }

    private OperationEntity buildOperationEntity(StockEntity stockEntity, BrokerageFirmEntity brokerageFirmEntity) {
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
                .brokerageFirm(brokerageFirmEntity)
                .build();
    }

    // --- toDomain ---

    @Test
    void toDomain_mapsAllFieldsCorrectly() {
        StockEntity stockEntity = buildStockEntity();
        BrokerageFirmEntity brokerageFirmEntity = buildBrokerageFirmEntity();
        OperationEntity entity = buildOperationEntity(stockEntity, brokerageFirmEntity);

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
        BrokerageFirmEntity brokerageFirmEntity = buildBrokerageFirmEntity();
        Instant saleDate = Instant.parse("2024-06-01T12:00:00Z");
        OperationEntity entity = buildOperationEntity(stockEntity, brokerageFirmEntity);
        entity.setSaleDate(saleDate);

        Operation operation = OperationJpaMapper.toDomain(entity);

        assertEquals(saleDate, operation.getSaleDate());
    }

    @Test
    void toDomain_mapsZeroTaxValues() {
        StockEntity stockEntity = buildStockEntity();
        BrokerageFirmEntity brokerageFirmEntity = BrokerageFirmEntity.builder()
                .id(BROKERAGE_FIRM_ID)
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
                .brokerageFirm(brokerageFirmEntity)
                .build();

        Operation operation = OperationJpaMapper.toDomain(entity);

        assertEquals(0.0, operation.getPurchaseTax());
        assertEquals(0.0, operation.getSaleTax());
        assertEquals(0.0, operation.getTotalTax());
    }

    // --- toEntity ---

    @Test
    void toEntity_mapsAllFieldsCorrectly() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        Operation operation = new Operation(
                OPERATION_ID, 10, 150.0, 160.0,
                Instant.parse("2024-01-15T10:00:00Z"), null, STOCK_ID, brokerageFirm
        );
        StockEntity stockEntity = buildStockEntity();
        BrokerageFirmEntity brokerageFirmEntity = buildBrokerageFirmEntity();

        OperationEntity entity = OperationJpaMapper.toEntity(operation, stockEntity, brokerageFirmEntity);

        assertEquals(OPERATION_ID, entity.getId());
        assertEquals(10, entity.getShareQuantity());
        assertEquals(150.0, entity.getPurchasePrice());
        assertEquals(10 * 160.0, entity.getTotalValue(), 1e-9);
        assertEquals((10 * 160.0) - (10 * 150.0), entity.getCapitalGain(), 1e-9);
        assertEquals(operation.getPurchaseTax(), entity.getPurchaseTax());
        assertEquals(operation.getSaleTax(), entity.getSaleTax());
        assertEquals(operation.getTotalTax(), entity.getTotalTax());
        assertEquals(operation.getNetEarnings(), entity.getNetEarnings());
        assertEquals(Instant.parse("2024-01-15T10:00:00Z"), entity.getPurchaseDate());
        assertNull(entity.getSaleDate());
        assertEquals(stockEntity, entity.getStock());
        assertEquals(brokerageFirmEntity, entity.getBrokerageFirm());
    }

    @Test
    void toEntity_mapsSaleDateWhenPresent() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        Instant saleDate = Instant.parse("2024-06-01T12:00:00Z");
        Operation operation = new Operation(
                OPERATION_ID, 10, 150.0, 160.0,
                Instant.parse("2024-01-15T10:00:00Z"), saleDate, STOCK_ID, brokerageFirm
        );
        StockEntity stockEntity = buildStockEntity();
        BrokerageFirmEntity brokerageFirmEntity = buildBrokerageFirmEntity();

        OperationEntity entity = OperationJpaMapper.toEntity(operation, stockEntity, brokerageFirmEntity);

        assertEquals(saleDate, entity.getSaleDate());
    }

    @Test
    void toDomain_andToEntity_areSymmetric() {
        StockEntity stockEntity = buildStockEntity();
        BrokerageFirmEntity brokerageFirmEntity = buildBrokerageFirmEntity();
        OperationEntity original = buildOperationEntity(stockEntity, brokerageFirmEntity);

        Operation domain = OperationJpaMapper.toDomain(original);
        OperationEntity roundTripped = OperationJpaMapper.toEntity(domain, stockEntity, brokerageFirmEntity);

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
