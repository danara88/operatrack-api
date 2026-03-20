package com.operatrack.operatrack_api.database;

import com.operatrack.operatrack_api.database.entities.TaxEntity;
import com.operatrack.operatrack_api.database.mappers.TaxJpaMapper;
import com.operatrack.operatrack_api.database.repositories.TaxRepository;
import com.operatrack.operatrack_api.model.Tax;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaxJpaRepository {

    private final TaxRepository taxRepository;

    public TaxJpaRepository(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }

    public boolean existsByInstitutionName(String institutionName) {
        return taxRepository.existsByInstitutionName(institutionName);
    }

    public Optional<Tax> findById(String id) {
        return taxRepository.findById(id).map(TaxJpaMapper::toDomain);
    }

    public Page<Tax> findAll(int page, int size) {
        return taxRepository.findAll(PageRequest.of(page, size))
                .map(TaxJpaMapper::toDomain);
    }

    public Tax save(Tax tax) {
        TaxEntity entity = TaxJpaMapper.toEntity(tax);
        TaxEntity savedEntity = taxRepository.save(entity);
        return TaxJpaMapper.toDomain(savedEntity);
    }

    public boolean existsById(String id) {
        return taxRepository.existsById(id);
    }

    public void deleteById(String id) {
        taxRepository.deleteById(id);
    }
}
