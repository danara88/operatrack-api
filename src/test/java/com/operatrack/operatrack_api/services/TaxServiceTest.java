package com.operatrack.operatrack_api.services;

import com.operatrack.operatrack_api.controllers.exceptions.DuplicatedResourceException;
import com.operatrack.operatrack_api.controllers.exceptions.ResourceNotFoundException;
import com.operatrack.operatrack_api.database.TaxJpaRepository;
import com.operatrack.operatrack_api.model.Tax;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxServiceTest {

    @Mock
    private TaxJpaRepository taxJpaRepository;

    @InjectMocks
    private TaxService taxService;

    // --- findAll ---

    @Test
    void findAll_returnsPageFromRepository() {
        Tax tax = new Tax("uuid-1", "Scotiabank", 0.0035);
        Page<Tax> expectedPage = new PageImpl<>(List.of(tax));
        when(taxJpaRepository.findAll(0, 10)).thenReturn(expectedPage);

        Page<Tax> result = taxService.findAll(0, 10);

        assertEquals(expectedPage, result);
        verify(taxJpaRepository).findAll(0, 10);
    }

    @Test
    void findAll_returnsEmptyPageWhenNoTaxesExist() {
        Page<Tax> emptyPage = new PageImpl<>(List.of());
        when(taxJpaRepository.findAll(0, 10)).thenReturn(emptyPage);

        Page<Tax> result = taxService.findAll(0, 10);

        assertTrue(result.isEmpty());
        verify(taxJpaRepository).findAll(0, 10);
    }

    // --- create ---

    @Test
    void create_savesAndReturnsTax_whenInstitutionNameIsUnique() {
        Tax savedTax = new Tax("uuid-1", "Scotiabank", 0.0035);
        when(taxJpaRepository.existsByInstitutionName("Scotiabank")).thenReturn(false);
        when(taxJpaRepository.save(any(Tax.class))).thenReturn(savedTax);

        Tax result = taxService.create("Scotiabank", 0.0035);

        assertEquals(savedTax.getId(), result.getId());
        assertEquals(savedTax.getInstitutionName(), result.getInstitutionName());
        assertEquals(savedTax.getTaxRate(), result.getTaxRate());
        verify(taxJpaRepository).existsByInstitutionName("Scotiabank");
        verify(taxJpaRepository).save(any(Tax.class));
    }

    @Test
    void create_throwsDuplicatedResourceException_whenInstitutionNameAlreadyExists() {
        when(taxJpaRepository.existsByInstitutionName("Scotiabank")).thenReturn(true);

        DuplicatedResourceException exception = assertThrows(
                DuplicatedResourceException.class,
                () -> taxService.create("Scotiabank", 0.0035)
        );

        assertEquals("Tax with institution name 'Scotiabank' already exists", exception.getMessage());
        verify(taxJpaRepository).existsByInstitutionName("Scotiabank");
        verify(taxJpaRepository, never()).save(any(Tax.class));
    }

    @Test
    void create_doesNotSave_whenDuplicateIsDetected() {
        when(taxJpaRepository.existsByInstitutionName("Banorte")).thenReturn(true);

        assertThrows(DuplicatedResourceException.class,
                () -> taxService.create("Banorte", 0.0025));

        verify(taxJpaRepository, never()).save(any());
    }

    // --- update ---

    @Test
    void update_updatesAndReturnsTax_whenTaxExists() {
        Tax existing = new Tax("uuid-1", "Scotiabank", 0.0035);
        Tax updated = new Tax("uuid-1", "Banorte", 0.0025);
        when(taxJpaRepository.findById("uuid-1")).thenReturn(Optional.of(existing));
        when(taxJpaRepository.save(existing)).thenReturn(updated);

        Tax result = taxService.update("uuid-1", "Banorte", 0.0025);

        assertEquals("Banorte", result.getInstitutionName());
        assertEquals(0.0025, result.getTaxRate());
        verify(taxJpaRepository).findById("uuid-1");
        verify(taxJpaRepository).save(existing);
    }

    @Test
    void update_throwsResourceNotFoundException_whenTaxDoesNotExist() {
        when(taxJpaRepository.findById("unknown-id")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> taxService.update("unknown-id", "Banorte", 0.0025));

        assertEquals("Tax with id 'unknown-id' not found", ex.getMessage());
        verify(taxJpaRepository).findById("unknown-id");
        verify(taxJpaRepository, never()).save(any());
    }

    @Test
    void update_doesNotSave_whenTaxIsNotFound() {
        when(taxJpaRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> taxService.update("missing", "Banorte", 0.0025));

        verify(taxJpaRepository, never()).save(any());
    }

    // --- delete ---

    @Test
    void delete_deletesTax_whenTaxExists() {
        when(taxJpaRepository.existsById("uuid-1")).thenReturn(true);

        taxService.delete("uuid-1");

        verify(taxJpaRepository).existsById("uuid-1");
        verify(taxJpaRepository).deleteById("uuid-1");
    }

    @Test
    void delete_throwsResourceNotFoundException_whenTaxDoesNotExist() {
        when(taxJpaRepository.existsById("unknown-id")).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> taxService.delete("unknown-id"));

        assertEquals("Tax with id 'unknown-id' not found", ex.getMessage());
        verify(taxJpaRepository).existsById("unknown-id");
        verify(taxJpaRepository, never()).deleteById(any());
    }

    @Test
    void delete_doesNotDelete_whenTaxIsNotFound() {
        when(taxJpaRepository.existsById("missing")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> taxService.delete("missing"));

        verify(taxJpaRepository, never()).deleteById(any());
    }
}
