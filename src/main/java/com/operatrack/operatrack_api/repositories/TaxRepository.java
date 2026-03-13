package com.operatrack.operatrack_api.repositories;

import com.operatrack.operatrack_api.repositories.entities.TaxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRepository extends JpaRepository<TaxEntity, String> {
}
