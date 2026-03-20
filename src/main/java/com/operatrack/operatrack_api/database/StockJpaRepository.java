package com.operatrack.operatrack_api.database;

import com.operatrack.operatrack_api.database.entities.StockEntity;
import com.operatrack.operatrack_api.database.mappers.StockJpaMapper;
import com.operatrack.operatrack_api.database.repositories.StockRepository;
import com.operatrack.operatrack_api.model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StockJpaRepository {

    private final StockRepository stockRepository;

    public StockJpaRepository(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public boolean existsByTickerSymbol(String tickerSymbol) {
        return stockRepository.existsByTickerSymbol(tickerSymbol);
    }

    public Optional<Stock> findById(String id) {
        return stockRepository.findById(id).map(StockJpaMapper::toDomain);
    }

    public Page<Stock> findAll(int page, int size) {
        return stockRepository.findAll(PageRequest.of(page, size))
                .map(StockJpaMapper::toDomain);
    }

    public Stock save(Stock stock) {
        StockEntity entity = StockJpaMapper.toEntity(stock);
        StockEntity savedEntity = stockRepository.save(entity);
        return StockJpaMapper.toDomain(savedEntity);
    }

    public boolean existsById(String id) {
        return stockRepository.existsById(id);
    }

    public void deleteById(String id) {
        stockRepository.deleteById(id);
    }
}
