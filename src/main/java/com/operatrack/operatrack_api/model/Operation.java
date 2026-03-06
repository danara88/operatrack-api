package com.operatrack.operatrack_api.model;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * Represents a stock market operation in the system.
 * An operation tracks the full lifecycle of a stock trade, including purchase details,
 * sale details, taxes applied, and the resulting financial outcomes.
 */
public class Operation {

    /**
     * The unique identifier of the operation.
     */
		@Getter
    private String id;

    /**
     * The number of shares involved in the operation.
     */
		@Getter
    private Integer shareQuantity;

    /**
     * The price per share at the time of purchase.
     */
		@Getter
    private Double purchasePrice;

    /**
     * The total monetary value of the operation (shares × price).
     */
		@Getter
    private Double totalValue;

    /**
     * The capital gain obtained from the operation (sale proceeds minus purchase cost).
     */
		@Getter
    private Double capitalGain;

    /**
     * The tax amount applied at the time of purchase.
     */
		@Getter
    private Double purchaseTax;

    /**
     * The tax amount applied at the time of sale.
     */
		@Getter
    private Double saleTax;

    /**
     * The combined total of purchase tax and sale tax.
     */
		@Getter
    private Double totalTax;

    /**
     * The net earnings after deducting all taxes from the capital gain.
     */
		@Getter
    private Double netEarnings;

    /**
     * The date and time when the shares were purchased.
     */
		@Getter
    private Instant purchaseDate;

    /**
     * The date and time when the shares were sold.
     * May be {@code null} for operations that have not been closed (sold) yet.
     */
		@Getter
    private Instant saleDate;

    /**
     * The stock associated with this operation.
     */
		@Getter
    private Stock stock;

    /**
     * The tax applied to this operation.
     */
		@Getter
    private Tax tax;
}
