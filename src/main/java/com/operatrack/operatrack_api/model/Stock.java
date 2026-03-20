package com.operatrack.operatrack_api.model;

import com.operatrack.operatrack_api.model.exceptions.InvalidCurrentPriceException;
import com.operatrack.operatrack_api.model.exceptions.InvalidStockNameException;
import com.operatrack.operatrack_api.model.exceptions.InvalidTickerSymbolException;
import lombok.Getter;

/**
 * Represents a stock (company) that the user wants to monitor.
 * Encapsulates the ticker symbol and current price, which are the
 * foundation for calculating gains, losses, and the total value of a position.
 */
public class Stock {

    /**
     * Unique identifier for the stock.
     */
    @Getter
    private String id;

    /**
     * Full name of the company associated with the stock.
     */
    @Getter
    private final String name;

    /**
     * Ticker symbol used to identify the stock on the exchange (e.g., "AAPL", "MSFT").
     */
    @Getter
    private final String tickerSymbol;

    /**
     * Current market price of one share of the stock.
     */
    @Getter
    private final Double currentPrice;

    /**
     * Creates a new Stock entity with validation.
     * The unique identifier is automatically generated as a random UUID string.
     *
     * @param name         the full name of the company (must be between 4 and 20 characters)
     * @param tickerSymbol the stock exchange ticker symbol (must be between 1 and 5 characters)
     * @param currentPrice the current market price per share (must be zero or positive)
     * @throws InvalidTickerSymbolException if tickerSymbol is null or not between 1 and 5 characters
     * @throws InvalidStockNameException    if name is null or not between 4 and 20 characters
     * @throws InvalidCurrentPriceException if currentPrice is null or negative
     */
    public Stock(String name, String tickerSymbol, Double currentPrice) {
        validateFields(name, tickerSymbol, currentPrice);
        this.name = name;
        this.tickerSymbol = tickerSymbol;
        this.currentPrice = currentPrice;
    }

    /**
     * Creates a new Stock entity with a provided identifier and validation.
     * Use this constructor when the unique identifier is known in advance (e.g., when restoring from persistence).
     *
     * @param id           the unique identifier to assign to this stock
     * @param name         the full name of the company (must be between 4 and 20 characters)
     * @param tickerSymbol the stock exchange ticker symbol (must be between 1 and 5 characters)
     * @param currentPrice the current market price per share (must be zero or positive)
     * @throws InvalidTickerSymbolException if tickerSymbol is null or not between 1 and 5 characters
     * @throws InvalidStockNameException    if name is null or not between 4 and 20 characters
     * @throws InvalidCurrentPriceException if currentPrice is null or negative
     */
    public Stock(String id, String name, String tickerSymbol, Double currentPrice) {
        validateFields(name, tickerSymbol, currentPrice);
        this.id = id;
        this.name = name;
        this.tickerSymbol = tickerSymbol;
        this.currentPrice = currentPrice;
    }

    private void validateFields(String name, String tickerSymbol, Double currentPrice) {
        if (tickerSymbol == null || tickerSymbol.isEmpty() || tickerSymbol.length() > 5) {
            throw new InvalidTickerSymbolException("Ticker symbol must be between 1 and 5 characters.");
        }
        if (name == null || name.length() < 4 || name.length() > 20) {
            throw new InvalidStockNameException("Stock name must be between 4 and 20 characters.");
        }
        if (currentPrice == null || currentPrice < 0) {
            throw new InvalidCurrentPriceException("Current price must be zero or a positive number.");
        }
    }
}
