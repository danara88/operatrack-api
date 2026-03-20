package com.operatrack.operatrack_api.model;

import com.operatrack.operatrack_api.model.exceptions.InvalidCurrentPriceException;
import com.operatrack.operatrack_api.model.exceptions.InvalidStockNameException;
import com.operatrack.operatrack_api.model.exceptions.InvalidTickerSymbolException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    // --- Constructor / invariants ---

    @Test
    void constructor_createsValidStock() {
        Stock stock = new Stock("Apple Inc.", "AAPL", 150.0);
        assertNull(stock.getId());
        assertEquals("Apple Inc.", stock.getName());
        assertEquals("AAPL", stock.getTickerSymbol());
        assertEquals(150.0, stock.getCurrentPrice());
    }

    @Test
    void constructor_acceptsZeroCurrentPrice() {
        Stock stock = new Stock("Apple Inc.", "AAPL", 0.0);
        assertEquals(0.0, stock.getCurrentPrice());
    }

    @Test
    void constructor_acceptsTickerSymbolWithOneCharacter() {
        Stock stock = new Stock("Apple Inc.", "A", 150.0);
        assertEquals("A", stock.getTickerSymbol());
    }

    @Test
    void constructor_acceptsTickerSymbolWithFiveCharacters() {
        Stock stock = new Stock("Apple Inc.", "GOOGL", 150.0);
        assertEquals("GOOGL", stock.getTickerSymbol());
    }

    @Test
    void constructor_acceptsNameWithExactlyFourCharacters() {
        Stock stock = new Stock("Meta", "META", 300.0);
        assertEquals("Meta", stock.getName());
    }

    @Test
    void constructor_acceptsNameWithExactlyTwentyCharacters() {
        Stock stock = new Stock("Berkshire Hathaway I", "BRK", 400.0);
        assertEquals("Berkshire Hathaway I", stock.getName());
    }

    @Test
    void constructor_throwsWhenTickerSymbolIsNull() {
        InvalidTickerSymbolException ex = assertThrows(InvalidTickerSymbolException.class,
                () -> new Stock("Apple Inc.", null, 150.0));
        assertEquals("Ticker symbol must be between 1 and 5 characters.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenTickerSymbolIsEmpty() {
        InvalidTickerSymbolException ex = assertThrows(InvalidTickerSymbolException.class,
                () -> new Stock("Apple Inc.", "", 150.0));
        assertEquals("Ticker symbol must be between 1 and 5 characters.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenTickerSymbolExceedsFiveCharacters() {
        InvalidTickerSymbolException ex = assertThrows(InvalidTickerSymbolException.class,
                () -> new Stock("Apple Inc.", "TOOLNG", 150.0));
        assertEquals("Ticker symbol must be between 1 and 5 characters.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenNameIsNull() {
        InvalidStockNameException ex = assertThrows(InvalidStockNameException.class,
                () -> new Stock(null, "AAPL", 150.0));
        assertEquals("Stock name must be between 4 and 20 characters.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenNameIsTooShort() {
        InvalidStockNameException ex = assertThrows(InvalidStockNameException.class,
                () -> new Stock("App", "AAPL", 150.0));
        assertEquals("Stock name must be between 4 and 20 characters.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenNameExceedsTwentyCharacters() {
        InvalidStockNameException ex = assertThrows(InvalidStockNameException.class,
                () -> new Stock("A Very Long Stock Name", "AAPL", 150.0));
        assertEquals("Stock name must be between 4 and 20 characters.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenCurrentPriceIsNull() {
        InvalidCurrentPriceException ex = assertThrows(InvalidCurrentPriceException.class,
                () -> new Stock("Apple Inc.", "AAPL", null));
        assertEquals("Current price must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenCurrentPriceIsNegative() {
        InvalidCurrentPriceException ex = assertThrows(InvalidCurrentPriceException.class,
                () -> new Stock("Apple Inc.", "AAPL", -1.0));
        assertEquals("Current price must be zero or a positive number.", ex.getMessage());
    }

    // --- Constructor with id / invariants ---

    @Test
    void constructorWithId_createsValidStockWithProvidedId() {
        String providedId = "custom-stock-id-123";
        Stock stock = new Stock(providedId, "Apple Inc.", "AAPL", 150.0);
        assertEquals(providedId, stock.getId());
        assertEquals("Apple Inc.", stock.getName());
        assertEquals("AAPL", stock.getTickerSymbol());
        assertEquals(150.0, stock.getCurrentPrice());
    }

    @Test
    void constructorWithId_acceptsUUIDAsId() {
        String providedId = UUID.randomUUID().toString();
        Stock stock = new Stock(providedId, "Apple Inc.", "AAPL", 150.0);
        assertEquals(providedId, stock.getId());
    }

    @Test
    void constructorWithId_throwsWhenTickerSymbolIsInvalid() {
        assertThrows(InvalidTickerSymbolException.class,
                () -> new Stock("custom-id", "Apple Inc.", "TOOLNG", 150.0));
    }

    @Test
    void constructorWithId_throwsWhenNameIsInvalid() {
        assertThrows(InvalidStockNameException.class,
                () -> new Stock("custom-id", "App", "AAPL", 150.0));
    }

    @Test
    void constructorWithId_throwsWhenCurrentPriceIsNegative() {
        assertThrows(InvalidCurrentPriceException.class,
                () -> new Stock("custom-id", "Apple Inc.", "AAPL", -1.0));
    }

    // --- updateCurrentPrice ---

    @Test
    void updateCurrentPrice_updatesPrice_whenNewPriceIsPositive() {
        Stock stock = new Stock("Apple Inc.", "AAPL", 150.0);
        stock.updateCurrentPrice(200.0);
        assertEquals(200.0, stock.getCurrentPrice());
    }

    @Test
    void updateCurrentPrice_updatesPrice_whenNewPriceIsZero() {
        Stock stock = new Stock("Apple Inc.", "AAPL", 150.0);
        stock.updateCurrentPrice(0.0);
        assertEquals(0.0, stock.getCurrentPrice());
    }

    @Test
    void updateCurrentPrice_throwsWhenNewPriceIsNull() {
        Stock stock = new Stock("Apple Inc.", "AAPL", 150.0);
        InvalidCurrentPriceException ex = assertThrows(InvalidCurrentPriceException.class,
                () -> stock.updateCurrentPrice(null));
        assertEquals("Current price must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void updateCurrentPrice_throwsWhenNewPriceIsNegative() {
        Stock stock = new Stock("Apple Inc.", "AAPL", 150.0);
        InvalidCurrentPriceException ex = assertThrows(InvalidCurrentPriceException.class,
                () -> stock.updateCurrentPrice(-10.0));
        assertEquals("Current price must be zero or a positive number.", ex.getMessage());
    }
}
