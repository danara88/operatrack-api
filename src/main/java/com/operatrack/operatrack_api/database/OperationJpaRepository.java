package com.operatrack.operatrack_api.database;

import com.operatrack.operatrack_api.database.repositories.OperationRepository;
import org.springframework.stereotype.Component;

@Component
public class OperationJpaRepository {

    private final OperationRepository operationRepository;

    public OperationJpaRepository(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }
}
