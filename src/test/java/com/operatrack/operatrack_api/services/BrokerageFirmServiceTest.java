package com.operatrack.operatrack_api.services;

import com.operatrack.operatrack_api.controllers.exceptions.DuplicatedResourceException;
import com.operatrack.operatrack_api.controllers.exceptions.ResourceNotFoundException;
import com.operatrack.operatrack_api.database.BrokerageFirmJpaRepository;
import com.operatrack.operatrack_api.model.BrokerageFirm;
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
class BrokerageFirmServiceTest {

    @Mock
    private BrokerageFirmJpaRepository brokerageFirmJpaRepository;

    @InjectMocks
    private BrokerageFirmService brokerageFirmService;

    // --- findAll ---

    @Test
    void findAll_returnsPageFromRepository() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("uuid-1", "Scotiabank", 0.0035);
        Page<BrokerageFirm> expectedPage = new PageImpl<>(List.of(brokerageFirm));
        when(brokerageFirmJpaRepository.findAll(0, 10)).thenReturn(expectedPage);

        Page<BrokerageFirm> result = brokerageFirmService.findAll(0, 10);

        assertEquals(expectedPage, result);
        verify(brokerageFirmJpaRepository).findAll(0, 10);
    }

    @Test
    void findAll_returnsEmptyPageWhenNoBrokerageFirmsExist() {
        Page<BrokerageFirm> emptyPage = new PageImpl<>(List.of());
        when(brokerageFirmJpaRepository.findAll(0, 10)).thenReturn(emptyPage);

        Page<BrokerageFirm> result = brokerageFirmService.findAll(0, 10);

        assertTrue(result.isEmpty());
        verify(brokerageFirmJpaRepository).findAll(0, 10);
    }

    // --- create ---

    @Test
    void create_savesAndReturnsBrokerageFirm_whenInstitutionNameIsUnique() {
        BrokerageFirm savedBrokerageFirm = new BrokerageFirm("uuid-1", "Scotiabank", 0.0035);
        when(brokerageFirmJpaRepository.existsByInstitutionName("Scotiabank")).thenReturn(false);
        when(brokerageFirmJpaRepository.save(any(BrokerageFirm.class))).thenReturn(savedBrokerageFirm);

        BrokerageFirm result = brokerageFirmService.create("Scotiabank", 0.0035);

        assertEquals(savedBrokerageFirm.getId(), result.getId());
        assertEquals(savedBrokerageFirm.getInstitutionName(), result.getInstitutionName());
        assertEquals(savedBrokerageFirm.getTaxRate(), result.getTaxRate());
        verify(brokerageFirmJpaRepository).existsByInstitutionName("Scotiabank");
        verify(brokerageFirmJpaRepository).save(any(BrokerageFirm.class));
    }

    @Test
    void create_throwsDuplicatedResourceException_whenInstitutionNameAlreadyExists() {
        when(brokerageFirmJpaRepository.existsByInstitutionName("Scotiabank")).thenReturn(true);

        DuplicatedResourceException exception = assertThrows(
                DuplicatedResourceException.class,
                () -> brokerageFirmService.create("Scotiabank", 0.0035)
        );

        assertEquals("BrokerageFirm with institution name 'Scotiabank' already exists", exception.getMessage());
        verify(brokerageFirmJpaRepository).existsByInstitutionName("Scotiabank");
        verify(brokerageFirmJpaRepository, never()).save(any(BrokerageFirm.class));
    }

    @Test
    void create_doesNotSave_whenDuplicateIsDetected() {
        when(brokerageFirmJpaRepository.existsByInstitutionName("Banorte")).thenReturn(true);

        assertThrows(DuplicatedResourceException.class,
                () -> brokerageFirmService.create("Banorte", 0.0025));

        verify(brokerageFirmJpaRepository, never()).save(any());
    }

    // --- update ---

    @Test
    void update_updatesAndReturnsBrokerageFirm_whenBrokerageFirmExists() {
        BrokerageFirm existing = new BrokerageFirm("uuid-1", "Scotiabank", 0.0035);
        BrokerageFirm updated = new BrokerageFirm("uuid-1", "Banorte", 0.0025);
        when(brokerageFirmJpaRepository.findById("uuid-1")).thenReturn(Optional.of(existing));
        when(brokerageFirmJpaRepository.save(existing)).thenReturn(updated);

        BrokerageFirm result = brokerageFirmService.update("uuid-1", "Banorte", 0.0025);

        assertEquals("Banorte", result.getInstitutionName());
        assertEquals(0.0025, result.getTaxRate());
        verify(brokerageFirmJpaRepository).findById("uuid-1");
        verify(brokerageFirmJpaRepository).save(existing);
    }

    @Test
    void update_throwsResourceNotFoundException_whenBrokerageFirmDoesNotExist() {
        when(brokerageFirmJpaRepository.findById("unknown-id")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> brokerageFirmService.update("unknown-id", "Banorte", 0.0025));

        assertEquals("BrokerageFirm with id 'unknown-id' not found", ex.getMessage());
        verify(brokerageFirmJpaRepository).findById("unknown-id");
        verify(brokerageFirmJpaRepository, never()).save(any());
    }

    @Test
    void update_doesNotSave_whenBrokerageFirmIsNotFound() {
        when(brokerageFirmJpaRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> brokerageFirmService.update("missing", "Banorte", 0.0025));

        verify(brokerageFirmJpaRepository, never()).save(any());
    }

    // --- delete ---

    @Test
    void delete_deletesBrokerageFirm_whenBrokerageFirmExists() {
        when(brokerageFirmJpaRepository.existsById("uuid-1")).thenReturn(true);

        brokerageFirmService.delete("uuid-1");

        verify(brokerageFirmJpaRepository).existsById("uuid-1");
        verify(brokerageFirmJpaRepository).deleteById("uuid-1");
    }

    @Test
    void delete_throwsResourceNotFoundException_whenBrokerageFirmDoesNotExist() {
        when(brokerageFirmJpaRepository.existsById("unknown-id")).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> brokerageFirmService.delete("unknown-id"));

        assertEquals("BrokerageFirm with id 'unknown-id' not found", ex.getMessage());
        verify(brokerageFirmJpaRepository).existsById("unknown-id");
        verify(brokerageFirmJpaRepository, never()).deleteById(any());
    }

    @Test
    void delete_doesNotDelete_whenBrokerageFirmIsNotFound() {
        when(brokerageFirmJpaRepository.existsById("missing")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> brokerageFirmService.delete("missing"));

        verify(brokerageFirmJpaRepository, never()).deleteById(any());
    }
}
