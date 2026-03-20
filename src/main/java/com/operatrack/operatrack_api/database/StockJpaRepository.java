package com.operatrack.operatrack_api.database;

import com.operatrack.operatrack_api.database.repositories.StockRepository;
import org.springframework.stereotype.Component;

@Component
public class StockJpaRepository {

    private final StockRepository stockRepository;

    public StockJpaRepository(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
}
