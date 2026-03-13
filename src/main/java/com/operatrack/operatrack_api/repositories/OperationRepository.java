package com.operatrack.operatrack_api.repositories;

import com.operatrack.operatrack_api.repositories.entities.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<OperationEntity, String> {
}
