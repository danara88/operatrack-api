package com.operatrack.operatrack_api.services;

import com.operatrack.operatrack_api.controllers.exceptions.DuplicatedResourceException;
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
        if (stockJpaRepository.existsByTickerSymbol(tickerSymbol)) {
            throw new DuplicatedResourceException("Stock with ticker symbol '" + tickerSymbol + "' already exists");
        }
        Stock stock = new Stock(name, tickerSymbol, currentPrice);
        return stockJpaRepository.save(stock);
    }
}
