package com.operatrack.operatrack_api.model;

import java.time.Instant;
import java.util.UUID;

import com.operatrack.operatrack_api.model.exceptions.InvalidPurchasePriceException;
import com.operatrack.operatrack_api.model.exceptions.InvalidPurchaseTaxException;
import com.operatrack.operatrack_api.model.exceptions.InvalidSaleTaxException;
import com.operatrack.operatrack_api.model.exceptions.InvalidShareQuantityException;
import com.operatrack.operatrack_api.model.exceptions.InvalidTotalTaxException;
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
    @Getter private final String id;

    /**
     * The number of shares involved in the operation.
     */
    @Getter private final Integer shareQuantity;

    /**
     * The price per share at the time of purchase.
     */
    @Getter private final Double purchasePrice;

    /**
     * The total monetary value of the operation (shares × price).
     */
    @Getter private final Double totalValue;

    /**
     * The capital gain obtained from the operation (sale proceeds minus purchase cost).
     */
    @Getter private final Double capitalGain;

    /**
     * The tax amount applied at the time of purchase.
     */
    @Getter private final Double purchaseTax;

    /**
     * The tax amount applied at the time of sale.
     */
    @Getter private final Double saleTax;

    /**
     * The combined total of purchase tax and sale tax.
     */
    @Getter private final Double totalTax;

    /**
     * The net earnings after deducting all taxes from the capital gain.
     */
    @Getter private final Double netEarnings;

    /**
     * The date and time when the shares were purchased.
     */
    @Getter private final Instant purchaseDate;

    /**
     * The date and time when the shares were sold.
     * May be {@code null} for operations that have not been closed (sold) yet.
     */
    @Getter private final Instant saleDate;

    /**
     * The stock associated with this operation.
     */
    @Getter private final Stock stock;

    /**
     * The tax applied to this operation.
     */
    @Getter private final Tax tax;

    /**
     * Constructor with auto-generated UUID id and purchase date set to now.
     *
     * @param shareQuantity number of shares (≥ 0)
     * @param purchasePrice price per share at purchase (≥ 0)
     * @param totalValue    total monetary value of the operation (≥ 0)
     * @param capitalGain   capital gain from the operation
     * @param purchaseTax   tax applied at purchase (≥ 0)
     * @param saleTax       tax applied at sale (≥ 0)
     * @param totalTax      combined purchase and sale tax (≥ 0)
     * @param netEarnings   net earnings after taxes
     * @param saleDate      date of sale, may be {@code null} if not yet sold
     * @param stock         associated stock
     * @param tax           associated tax entity
     */
    public Operation(Integer shareQuantity, Double purchasePrice, Double totalValue,
                     Double capitalGain, Double purchaseTax, Double saleTax,
                     Double totalTax, Double netEarnings, Instant saleDate,
                     Stock stock, Tax tax) {
        validateFields(shareQuantity, purchasePrice, totalValue, purchaseTax, saleTax, totalTax);
        this.id = UUID.randomUUID().toString();
        this.shareQuantity = shareQuantity;
        this.purchasePrice = purchasePrice;
        this.totalValue = totalValue;
        this.capitalGain = capitalGain;
        this.purchaseTax = purchaseTax;
        this.saleTax = saleTax;
        this.totalTax = totalTax;
        this.netEarnings = netEarnings;
        this.purchaseDate = Instant.now();
        this.saleDate = saleDate;
        this.stock = stock;
        this.tax = tax;
    }

    /**
     * Constructor with provided id and purchase date.
     *
     * @param id            operation identifier
     * @param shareQuantity number of shares (≥ 0)
     * @param purchasePrice price per share at purchase (≥ 0)
     * @param totalValue    total monetary value of the operation (≥ 0)
     * @param capitalGain   capital gain from the operation
     * @param purchaseTax   tax applied at purchase (≥ 0)
     * @param saleTax       tax applied at sale (≥ 0)
     * @param totalTax      combined purchase and sale tax (≥ 0)
     * @param netEarnings   net earnings after taxes
     * @param purchaseDate  date of purchase
     * @param saleDate      date of sale, may be {@code null} if not yet sold
     * @param stock         associated stock
     * @param tax           associated tax entity
     */
    public Operation(String id, Integer shareQuantity, Double purchasePrice, Double totalValue,
                     Double capitalGain, Double purchaseTax, Double saleTax,
                     Double totalTax, Double netEarnings, Instant purchaseDate,
                     Instant saleDate, Stock stock, Tax tax) {
        validateFields(shareQuantity, purchasePrice, totalValue, purchaseTax, saleTax, totalTax);
        this.id = id;
        this.shareQuantity = shareQuantity;
        this.purchasePrice = purchasePrice;
        this.totalValue = totalValue;
        this.capitalGain = capitalGain;
        this.purchaseTax = purchaseTax;
        this.saleTax = saleTax;
        this.totalTax = totalTax;
        this.netEarnings = netEarnings;
        this.purchaseDate = purchaseDate;
        this.saleDate = saleDate;
        this.stock = stock;
        this.tax = tax;
    }

    private void validateFields(Integer shareQuantity, Double purchasePrice, Double totalValue,
                                Double purchaseTax, Double saleTax, Double totalTax) {
        if (shareQuantity == null || shareQuantity < 0) {
            throw new InvalidShareQuantityException("Share quantity must be zero or a positive number.");
        }
        if (purchasePrice == null || purchasePrice < 0) {
            throw new InvalidPurchasePriceException("Purchase price must be zero or a positive number.");
        }
        if (totalValue == null || totalValue < 0) {
            throw new InvalidTotalValueException("Total value must be zero or a positive number.");
        }
        if (purchaseTax == null || purchaseTax < 0) {
            throw new InvalidPurchaseTaxException("Purchase tax must be zero or a positive number.");
        }
        if (saleTax == null || saleTax < 0) {
            throw new InvalidSaleTaxException("Sale tax must be zero or a positive number.");
        }
        if (totalTax == null || totalTax < 0) {
            throw new InvalidTotalTaxException("Total tax must be zero or a positive number.");
        }
    }
}
