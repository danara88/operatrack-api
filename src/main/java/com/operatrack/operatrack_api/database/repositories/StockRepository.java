package com.operatrack.operatrack_api.database.repositories;

import com.operatrack.operatrack_api.database.entities.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockEntity, String> {
}
