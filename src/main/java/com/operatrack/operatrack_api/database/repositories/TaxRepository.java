package com.operatrack.operatrack_api.database.repositories;

import com.operatrack.operatrack_api.database.entities.TaxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRepository extends JpaRepository<TaxEntity, String> {
}
