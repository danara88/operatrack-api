package com.operatrack.operatrack_api.services;

import com.operatrack.operatrack_api.controllers.exceptions.DuplicatedResourceException;
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
}
