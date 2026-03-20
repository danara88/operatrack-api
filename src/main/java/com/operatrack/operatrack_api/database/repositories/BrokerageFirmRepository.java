package com.operatrack.operatrack_api.database.repositories;

import com.operatrack.operatrack_api.database.entities.BrokerageFirmEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrokerageFirmRepository extends JpaRepository<BrokerageFirmEntity, String> {
    boolean existsByInstitutionName(String institutionName);
}
