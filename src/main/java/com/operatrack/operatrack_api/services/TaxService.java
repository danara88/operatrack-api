package com.operatrack.operatrack_api.services;

import com.operatrack.operatrack_api.controllers.exceptions.DuplicatedResourceException;
import com.operatrack.operatrack_api.controllers.exceptions.ResourceNotFoundException;
import com.operatrack.operatrack_api.database.TaxJpaRepository;
import com.operatrack.operatrack_api.model.Tax;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class TaxService {

    private final TaxJpaRepository taxJpaRepository;

    public TaxService(TaxJpaRepository taxJpaRepository) {
        this.taxJpaRepository = taxJpaRepository;
    }

    public Page<Tax> findAll(int page, int size) {
        return taxJpaRepository.findAll(page, size);
    }

    public Tax create(String institutionName, Double taxRate) {
        if (taxJpaRepository.existsByInstitutionName(institutionName)) {
            throw new DuplicatedResourceException("Tax with institution name '" + institutionName + "' already exists");
        }
        Tax tax = new Tax(institutionName, taxRate);
        return taxJpaRepository.save(tax);
    }

    public Tax update(String id, String institutionName, Double taxRate) {
        Tax tax = taxJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax with id '" + id + "' not found"));
        tax.updateInstitutionName(institutionName);
        tax.updateTaxRate(taxRate);
        return taxJpaRepository.save(tax);
    }

    public void delete(String id) {
        if (!taxJpaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tax with id '" + id + "' not found");
        }
        taxJpaRepository.deleteById(id);
    }
}
