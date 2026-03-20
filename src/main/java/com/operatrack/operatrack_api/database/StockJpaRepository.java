package com.operatrack.operatrack_api.database;

import com.operatrack.operatrack_api.database.entities.StockEntity;
import com.operatrack.operatrack_api.database.mappers.StockJpaMapper;
import com.operatrack.operatrack_api.database.repositories.StockRepository;
import com.operatrack.operatrack_api.model.Stock;
import org.springframework.stereotype.Component;

@Component
public class StockJpaRepository {

    private final StockRepository stockRepository;

    public StockJpaRepository(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public boolean existsByTickerSymbol(String tickerSymbol) {
        return stockRepository.existsByTickerSymbol(tickerSymbol);
    }

    public Stock save(Stock stock) {
        StockEntity entity = StockJpaMapper.toEntity(stock);
        StockEntity savedEntity = stockRepository.save(entity);
        return StockJpaMapper.toDomain(savedEntity);
    }
}
