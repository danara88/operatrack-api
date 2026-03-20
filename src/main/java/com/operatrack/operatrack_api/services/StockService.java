package com.operatrack.operatrack_api.services;

import com.operatrack.operatrack_api.controllers.exceptions.DuplicatedResourceException;
import com.operatrack.operatrack_api.controllers.exceptions.ResourceNotFoundException;
import com.operatrack.operatrack_api.database.StockJpaRepository;
import com.operatrack.operatrack_api.model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private final StockJpaRepository stockJpaRepository;

    public StockService(StockJpaRepository stockJpaRepository) {
        this.stockJpaRepository = stockJpaRepository;
    }

    public Page<Stock> findAll(int page, int size) {
        return stockJpaRepository.findAll(page, size);
    }

    public Stock updatePrice(String id, Double newPrice) {
        Stock stock = stockJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock with id '" + id + "' not found"));
        stock.updateCurrentPrice(newPrice);
        return stockJpaRepository.save(stock);
    }

    public Stock create(String name, String tickerSymbol, Double currentPrice) {
        if (stockJpaRepository.existsByTickerSymbol(tickerSymbol)) {
            throw new DuplicatedResourceException("Stock with ticker symbol '" + tickerSymbol + "' already exists");
        }
        Stock stock = new Stock(name, tickerSymbol, currentPrice);
        return stockJpaRepository.save(stock);
    }

    public void delete(String id) {
        if (!stockJpaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Stock with id '" + id + "' not found");
        }
        stockJpaRepository.deleteById(id);
    }
}
