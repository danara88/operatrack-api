package com.operatrack.operatrack_api.services;

import com.operatrack.operatrack_api.database.StockJpaRepository;
import com.operatrack.operatrack_api.model.Stock;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private final StockJpaRepository stockJpaRepository;

    public StockService(StockJpaRepository stockJpaRepository) {
        this.stockJpaRepository = stockJpaRepository;
    }

    public Stock create(String name, String tickerSymbol, Double currentPrice) {
        Stock stock = new Stock(name, tickerSymbol, currentPrice);
        return stockJpaRepository.save(stock);
    }
}
