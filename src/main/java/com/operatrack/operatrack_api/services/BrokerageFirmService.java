package com.operatrack.operatrack_api.services;

import com.operatrack.operatrack_api.controllers.exceptions.DuplicatedResourceException;
import com.operatrack.operatrack_api.controllers.exceptions.ResourceNotFoundException;
import com.operatrack.operatrack_api.database.BrokerageFirmJpaRepository;
import com.operatrack.operatrack_api.model.BrokerageFirm;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class BrokerageFirmService {

    private final BrokerageFirmJpaRepository brokerageFirmJpaRepository;

    public BrokerageFirmService(BrokerageFirmJpaRepository brokerageFirmJpaRepository) {
        this.brokerageFirmJpaRepository = brokerageFirmJpaRepository;
    }

    public Page<BrokerageFirm> findAll(int page, int size) {
        return brokerageFirmJpaRepository.findAll(page, size);
    }

    public BrokerageFirm create(String institutionName, Double taxRate) {
        if (brokerageFirmJpaRepository.existsByInstitutionName(institutionName)) {
            throw new DuplicatedResourceException("BrokerageFirm with institution name '" + institutionName + "' already exists");
        }
        BrokerageFirm brokerageFirm = new BrokerageFirm(institutionName, taxRate);
        return brokerageFirmJpaRepository.save(brokerageFirm);
    }

    public BrokerageFirm update(String id, String institutionName, Double taxRate) {
        BrokerageFirm brokerageFirm = brokerageFirmJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BrokerageFirm with id '" + id + "' not found"));
        brokerageFirm.updateInstitutionName(institutionName);
        brokerageFirm.updateTaxRate(taxRate);
        return brokerageFirmJpaRepository.save(brokerageFirm);
    }

    public void delete(String id) {
        if (!brokerageFirmJpaRepository.existsById(id)) {
            throw new ResourceNotFoundException("BrokerageFirm with id '" + id + "' not found");
        }
        brokerageFirmJpaRepository.deleteById(id);
    }
}
