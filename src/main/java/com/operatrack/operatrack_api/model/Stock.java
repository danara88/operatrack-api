package com.operatrack.operatrack_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a stock (company) that the user wants to monitor.
 * Encapsulates the ticker symbol and current price, which are the
 * foundation for calculating gains, losses, and the total value of a position.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    /**
     * Unique identifier for the stock.
     */
    private String id;

    /**
     * Full name of the company associated with the stock.
     */
    private String name;

    /**
     * Ticker symbol used to identify the stock on the exchange (e.g., "AAPL", "MSFT").
     */
    private String tickerSymbol;

    /**
     * Current market price of one share of the stock.
     */
    private Double currentPrice;
}
