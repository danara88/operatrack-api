package com.operatrack.operatrack_api.services;

import com.operatrack.operatrack_api.controllers.exceptions.DuplicatedResourceException;
import com.operatrack.operatrack_api.controllers.exceptions.ResourceNotFoundException;
import com.operatrack.operatrack_api.database.StockJpaRepository;
import com.operatrack.operatrack_api.model.Stock;
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
class StockServiceTest {

    @Mock
    private StockJpaRepository stockJpaRepository;

    @InjectMocks
    private StockService stockService;

    // --- findAll ---

    @Test
    void findAll_returnsPageFromRepository() {
        Stock stock = new Stock("uuid-1", "Apple Inc.", "AAPL", 150.0);
        Page<Stock> expectedPage = new PageImpl<>(List.of(stock));
        when(stockJpaRepository.findAll(0, 10)).thenReturn(expectedPage);

        Page<Stock> result = stockService.findAll(0, 10);

        assertEquals(expectedPage, result);
        verify(stockJpaRepository).findAll(0, 10);
    }

    @Test
    void findAll_returnsEmptyPageWhenNoStocksExist() {
        Page<Stock> emptyPage = new PageImpl<>(List.of());
        when(stockJpaRepository.findAll(0, 10)).thenReturn(emptyPage);

        Page<Stock> result = stockService.findAll(0, 10);

        assertTrue(result.isEmpty());
        verify(stockJpaRepository).findAll(0, 10);
    }

    // --- create ---

    @Test
    void create_savesAndReturnsStock_whenTickerSymbolIsUnique() {
        Stock savedStock = new Stock("uuid-1", "Apple Inc.", "AAPL", 150.0);
        when(stockJpaRepository.existsByTickerSymbol("AAPL")).thenReturn(false);
        when(stockJpaRepository.save(any(Stock.class))).thenReturn(savedStock);

        Stock result = stockService.create("Apple Inc.", "AAPL", 150.0);

        assertEquals(savedStock.getId(), result.getId());
        assertEquals(savedStock.getName(), result.getName());
        assertEquals(savedStock.getTickerSymbol(), result.getTickerSymbol());
        assertEquals(savedStock.getCurrentPrice(), result.getCurrentPrice());
        verify(stockJpaRepository).existsByTickerSymbol("AAPL");
        verify(stockJpaRepository).save(any(Stock.class));
    }

    @Test
    void create_throwsDuplicatedResourceException_whenTickerSymbolAlreadyExists() {
        when(stockJpaRepository.existsByTickerSymbol("AAPL")).thenReturn(true);

        DuplicatedResourceException exception = assertThrows(
                DuplicatedResourceException.class,
                () -> stockService.create("Apple Inc.", "AAPL", 150.0)
        );

        assertEquals("Stock with ticker symbol 'AAPL' already exists", exception.getMessage());
        verify(stockJpaRepository).existsByTickerSymbol("AAPL");
        verify(stockJpaRepository, never()).save(any(Stock.class));
    }

    @Test
    void create_doesNotSave_whenDuplicateIsDetected() {
        when(stockJpaRepository.existsByTickerSymbol("MSFT")).thenReturn(true);

        assertThrows(DuplicatedResourceException.class,
                () -> stockService.create("Microsoft", "MSFT", 300.0));

        verify(stockJpaRepository, never()).save(any());
    }

    // --- updatePrice ---

    @Test
    void updatePrice_updatesAndReturnsStock_whenStockExists() {
        Stock existing = new Stock("uuid-1", "Apple Inc.", "AAPL", 150.0);
        Stock updated = new Stock("uuid-1", "Apple Inc.", "AAPL", 200.0);
        when(stockJpaRepository.findById("uuid-1")).thenReturn(Optional.of(existing));
        when(stockJpaRepository.save(existing)).thenReturn(updated);

        Stock result = stockService.updatePrice("uuid-1", 200.0);

        assertEquals(200.0, result.getCurrentPrice());
        verify(stockJpaRepository).findById("uuid-1");
        verify(stockJpaRepository).save(existing);
    }

    @Test
    void updatePrice_throwsResourceNotFoundException_whenStockDoesNotExist() {
        when(stockJpaRepository.findById("unknown-id")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> stockService.updatePrice("unknown-id", 200.0));

        assertEquals("Stock with id 'unknown-id' not found", ex.getMessage());
        verify(stockJpaRepository).findById("unknown-id");
        verify(stockJpaRepository, never()).save(any());
    }

    @Test
    void updatePrice_doesNotSave_whenStockIsNotFound() {
        when(stockJpaRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> stockService.updatePrice("missing", 99.0));

        verify(stockJpaRepository, never()).save(any());
    }
}
