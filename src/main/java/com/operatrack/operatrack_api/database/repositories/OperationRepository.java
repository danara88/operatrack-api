package com.operatrack.operatrack_api.database.repositories;

import com.operatrack.operatrack_api.database.entities.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<OperationEntity, String> {
}
