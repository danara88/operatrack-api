package com.operatrack.operatrack_api.database;

import com.operatrack.operatrack_api.database.entities.BrokerageFirmEntity;
import com.operatrack.operatrack_api.database.mappers.BrokerageFirmJpaMapper;
import com.operatrack.operatrack_api.database.repositories.BrokerageFirmRepository;
import com.operatrack.operatrack_api.model.BrokerageFirm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BrokerageFirmJpaRepository {

    private final BrokerageFirmRepository brokerageFirmRepository;

    public BrokerageFirmJpaRepository(BrokerageFirmRepository brokerageFirmRepository) {
        this.brokerageFirmRepository = brokerageFirmRepository;
    }

    public boolean existsByInstitutionName(String institutionName) {
        return brokerageFirmRepository.existsByInstitutionName(institutionName);
    }

    public Optional<BrokerageFirm> findById(String id) {
        return brokerageFirmRepository.findById(id).map(BrokerageFirmJpaMapper::toDomain);
    }

    public Page<BrokerageFirm> findAll(int page, int size) {
        return brokerageFirmRepository.findAll(PageRequest.of(page, size))
                .map(BrokerageFirmJpaMapper::toDomain);
    }

    public BrokerageFirm save(BrokerageFirm brokerageFirm) {
        BrokerageFirmEntity entity = BrokerageFirmJpaMapper.toEntity(brokerageFirm);
        BrokerageFirmEntity savedEntity = brokerageFirmRepository.save(entity);
        return BrokerageFirmJpaMapper.toDomain(savedEntity);
    }

    public boolean existsById(String id) {
        return brokerageFirmRepository.existsById(id);
    }

    public void deleteById(String id) {
        brokerageFirmRepository.deleteById(id);
    }
}
