package com.operatrack.operatrack_api.model;

import java.time.Instant;
import java.util.UUID;

import com.operatrack.operatrack_api.model.exceptions.InvalidPurchasePriceException;
import com.operatrack.operatrack_api.model.exceptions.InvalidShareQuantityException;
import com.operatrack.operatrack_api.model.exceptions.InvalidTotalValueException;
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
		private final String id;

    /**
     * The number of shares involved in the operation.
     */
    @Getter
		private final Integer shareQuantity;

    /**
     * The price per share at the time of purchase.
     */
    @Getter
		private final Double purchasePrice;

    /**
     * The total monetary value of the operation (shares × price).
     */
    @Getter
		private final Double totalValue;

    /**
     * The capital gain obtained from the operation (sale proceeds minus purchase cost).
     */
    @Getter
		private final Double capitalGain;

    /**
     * The tax amount applied at the time of purchase.
     */
    @Getter
		private final Double purchaseTax;

    /**
     * The tax amount applied at the time of sale.
     */
    @Getter
		private final Double saleTax;

    /**
     * The combined total of purchase tax and sale tax.
     */
    @Getter
		private final Double totalTax;

    /**
     * The net earnings after deducting all taxes from the capital gain.
     */
    @Getter
		private final Double netEarnings;

    /**
     * The date and time when the shares were purchased.
     */
    @Getter
		private final Instant purchaseDate;

    /**
     * The date and time when the shares were sold.
     * May be {@code null} for operations that have not been closed (sold) yet.
     */
    @Getter
		private final Instant saleDate;

    /**
     * The identifier of the stock associated with this operation.
     */
    @Getter
		private final String stockId;

    /**
     * Constructor with auto-generated UUID id and purchase date set to now.
     *
     * @param shareQuantity number of shares (≥ 0)
     * @param purchasePrice price per share at purchase (≥ 0)
     * @param currentPrice  current market price per share
     * @param totalValue    total monetary value of the operation (≥ 0)
     * @param capitalGain   capital gain from the operation
     * @param netEarnings   net earnings after taxes
     * @param saleDate      date of sale, may be {@code null} if not yet sold
     * @param stockId       identifier of the associated stock
     * @param tax           the tax entity used to calculate purchase tax, sale tax, and total tax
     */
    public Operation(Integer shareQuantity, Double purchasePrice, Double currentPrice, Double totalValue,
                     Double capitalGain, Double netEarnings, Instant saleDate,
                     String stockId, Tax tax) {
        validateFields(shareQuantity, purchasePrice, totalValue);
        this.id = UUID.randomUUID().toString();
        this.shareQuantity = shareQuantity;
        this.purchasePrice = purchasePrice;
        this.totalValue = totalValue;
        this.capitalGain = capitalGain;
        this.purchaseTax = tax.calculatePurchaseTax(shareQuantity, purchasePrice);
        this.saleTax = tax.calculateSaleTax(shareQuantity, currentPrice);
        this.totalTax = tax.calculateTotalTax(this.purchaseTax, this.saleTax);
        this.netEarnings = netEarnings;
        this.purchaseDate = Instant.now();
        this.saleDate = saleDate;
        this.stockId = stockId;
    }

    /**
     * Constructor with provided id and purchase date.
     *
     * @param id            operation identifier
     * @param shareQuantity number of shares (≥ 0)
     * @param purchasePrice price per share at purchase (≥ 0)
     * @param currentPrice  current market price per share
     * @param totalValue    total monetary value of the operation (≥ 0)
     * @param capitalGain   capital gain from the operation
     * @param netEarnings   net earnings after taxes
     * @param purchaseDate  date of purchase
     * @param saleDate      date of sale, may be {@code null} if not yet sold
     * @param stockId       identifier of the associated stock
     * @param tax           the tax entity used to calculate purchase tax, sale tax, and total tax
     */
    public Operation(String id, Integer shareQuantity, Double purchasePrice, Double currentPrice, Double totalValue,
                     Double capitalGain, Double netEarnings, Instant purchaseDate,
                     Instant saleDate, String stockId, Tax tax) {
        validateFields(shareQuantity, purchasePrice, totalValue);
        this.id = id;
        this.shareQuantity = shareQuantity;
        this.purchasePrice = purchasePrice;
        this.totalValue = totalValue;
        this.capitalGain = capitalGain;
        this.purchaseTax = tax.calculatePurchaseTax(shareQuantity, purchasePrice);
        this.saleTax = tax.calculateSaleTax(shareQuantity, currentPrice);
        this.totalTax = tax.calculateTotalTax(this.purchaseTax, this.saleTax);
        this.netEarnings = netEarnings;
        this.purchaseDate = purchaseDate;
        this.saleDate = saleDate;
        this.stockId = stockId;
    }

    private void validateFields(Integer shareQuantity, Double purchasePrice, Double totalValue) {
        if (shareQuantity == null || shareQuantity < 0) {
            throw new InvalidShareQuantityException("Share quantity must be zero or a positive number.");
        }
        if (purchasePrice == null || purchasePrice < 0) {
            throw new InvalidPurchasePriceException("Purchase price must be zero or a positive number.");
        }
        if (totalValue == null || totalValue < 0) {
            throw new InvalidTotalValueException("Total value must be zero or a positive number.");
        }
    }
}
