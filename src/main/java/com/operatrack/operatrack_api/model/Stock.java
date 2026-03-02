package com.operatrack.operatrack_api.model;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a stock (company) that the user wants to monitor.
 * Encapsulates the ticker symbol and current price, which are the
 * foundation for calculating gains, losses, and the total value of a position.
 */
@Data
@Builder
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
