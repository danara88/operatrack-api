package com.operatrack.operatrack_api.database;

import com.operatrack.operatrack_api.database.repositories.TaxRepository;
import org.springframework.stereotype.Component;

@Component
public class TaxJpaRepository {

    private final TaxRepository taxRepository;

    public TaxJpaRepository(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }
}
