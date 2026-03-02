package com.operatrack.operatrack_api.model;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a stock market operation in the system.
 * An operation tracks the full lifecycle of a stock trade, including purchase details,
 * sale details, taxes applied, and the resulting financial outcomes.
 */
@Getter
@Setter
public class Operation {

    /**
     * The unique identifier of the operation.
     */
    private String id;

    /**
     * The number of shares involved in the operation.
     */
    private Integer shareQuantity;

    /**
     * The price per share at the time of purchase.
     */
    private Double purchasePrice;

    /**
     * The total monetary value of the operation (shares × price).
     */
    private Double totalValue;

    /**
     * The capital gain obtained from the operation (sale proceeds minus purchase cost).
     */
    private Double capitalGain;

    /**
     * The tax amount applied at the time of purchase.
     */
    private Double purchaseTax;

    /**
     * The tax amount applied at the time of sale.
     */
    private Double saleTax;

    /**
     * The combined total of purchase tax and sale tax.
     */
    private Double totalTax;

    /**
     * The net earnings after deducting all taxes from the capital gain.
     */
    private Double netEarnings;

    /**
     * The date and time when the shares were purchased.
     */
    private Instant purchaseDate;

    /**
     * The date and time when the shares were sold.
     * May be {@code null} for operations that have not been closed (sold) yet.
     */
    private Instant saleDate;
}
