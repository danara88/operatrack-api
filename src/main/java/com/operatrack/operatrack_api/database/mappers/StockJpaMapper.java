package com.operatrack.operatrack_api.database.mappers;

import com.operatrack.operatrack_api.database.entities.StockEntity;
import com.operatrack.operatrack_api.model.Stock;

public class StockJpaMapper {

    private StockJpaMapper() {}

    public static Stock toDomain(StockEntity entity) {
        return new Stock(
                entity.getId(),
                entity.getName(),
                entity.getTickerSymbol(),
                entity.getCurrentPrice()
        );
    }

    public static StockEntity toEntity(Stock stock) {
        return StockEntity.builder()
                .id(stock.getId())
                .name(stock.getName())
                .tickerSymbol(stock.getTickerSymbol())
                .currentPrice(stock.getCurrentPrice())
                .build();
    }
}
