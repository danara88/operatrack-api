package com.operatrack.operatrack_api.repositories;

import com.operatrack.operatrack_api.repositories.entities.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockEntity, String> {
}
