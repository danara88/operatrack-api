package com.operatrack.operatrack_api.database.mappers;

import com.operatrack.operatrack_api.database.entities.StockEntity;
import com.operatrack.operatrack_api.model.Stock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockJpaMapperTest {

    // --- toDomain ---

    @Test
    void toDomain_mapsAllFieldsCorrectly() {
        StockEntity entity = StockEntity.builder()
                .id("stock-uuid-1")
                .name("Apple Inc.")
                .tickerSymbol("AAPL")
                .currentPrice(180.0)
                .build();

        Stock stock = StockJpaMapper.toDomain(entity);

        assertEquals("stock-uuid-1", stock.getId());
        assertEquals("Apple Inc.", stock.getName());
        assertEquals("AAPL", stock.getTickerSymbol());
        assertEquals(180.0, stock.getCurrentPrice());
    }

    @Test
    void toDomain_mapsZeroCurrentPrice() {
        StockEntity entity = StockEntity.builder()
                .id("stock-uuid-2")
                .name("Test Corp")
                .tickerSymbol("TEST")
                .currentPrice(0.0)
                .build();

        Stock stock = StockJpaMapper.toDomain(entity);

        assertEquals(0.0, stock.getCurrentPrice());
    }

    // --- toEntity ---

    @Test
    void toEntity_mapsAllFieldsCorrectly() {
        Stock stock = new Stock("stock-uuid-3", "Microsoft", "MSFT", 350.0);

        StockEntity entity = StockJpaMapper.toEntity(stock);

        assertEquals("stock-uuid-3", entity.getId());
        assertEquals("Microsoft", entity.getName());
        assertEquals("MSFT", entity.getTickerSymbol());
        assertEquals(350.0, entity.getCurrentPrice());
    }

    @Test
    void toEntity_mapsZeroCurrentPrice() {
        Stock stock = new Stock("stock-uuid-4", "Zero Corp", "ZERO", 0.0);

        StockEntity entity = StockJpaMapper.toEntity(stock);

        assertEquals(0.0, entity.getCurrentPrice());
    }

    @Test
    void toDomain_andToEntity_areSymmetric() {
        StockEntity original = StockEntity.builder()
                .id("stock-uuid-5")
                .name("Google LLC")
                .tickerSymbol("GOOG")
                .currentPrice(140.0)
                .build();

        StockEntity roundTripped = StockJpaMapper.toEntity(StockJpaMapper.toDomain(original));

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getName(), roundTripped.getName());
        assertEquals(original.getTickerSymbol(), roundTripped.getTickerSymbol());
        assertEquals(original.getCurrentPrice(), roundTripped.getCurrentPrice());
    }
}
