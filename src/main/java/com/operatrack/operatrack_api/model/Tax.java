package com.operatrack.operatrack_api.model;

import java.util.List;
import lombok.Getter;

/**
 * Represents a tax entity associated with a brokerage firm or financial institution.
 * Each institution may apply a different tax rate, which can be reused across multiple operations.
 */
public class Tax {

    private static final double VAT_MULTIPLIER = 1.16;

    /**
     * The unique identifier for the tax entity.
     */
    @Getter
    private String id;

    /**
     * The name of the financial institution or brokerage firm that applies this tax.
     */
    @Getter
    private String institutionName;

    /**
     * The tax rate applied by the institution (e.g., 0.0035 for 0.35%). Must be between 0 and 1.
     */
    @Getter
    private Double taxRate;

    /**
     * List of operations to which this tax has been applied.
     */
    @Getter
    private List<Operation> operations;

    /**
     * Creates a new Tax entity with the given identifier, institution name, and tax rate.
     *
     * @param id              the unique identifier
     * @param institutionName the name of the financial institution (must be at least 4 characters)
     * @param taxRate         the decimal tax rate (must be between 0 and 1)
     * @throws RuntimeException if any business rule is violated
     */
    public Tax(String id, String institutionName, Double taxRate) {
        if (institutionName == null || institutionName.length() < 4) {
            throw new RuntimeException("Institution Name must be 4 characters length.");
        }
        if (taxRate == null || taxRate < 0 || taxRate > 1) {
            throw new RuntimeException("Tax rate must be between 0 and 1.");
        }
        this.id = id;
        this.institutionName = institutionName;
        this.taxRate = taxRate;
    }

    /**
     * Calculates the purchase tax for an operation.
     * Formula: -[(shareQuantity x purchasePrice) x taxRate] x 1.16
     *
     * @param shareQuantity the number of shares purchased
     * @param purchasePrice the price per share at the time of purchase
     * @return the purchase tax amount (negative value)
     */
    public double calculatePurchaseTax(int shareQuantity, double purchasePrice) {
        return -((shareQuantity * purchasePrice) * this.taxRate) * VAT_MULTIPLIER;
    }

    /**
     * Calculates the sale tax for an operation.
     * Formula: -[(shareQuantity x currentPrice) x taxRate] x 1.16
     *
     * @param shareQuantity the number of shares sold
     * @param currentPrice  the current market price per share
     * @return the sale tax amount (negative value)
     */
    public double calculateSaleTax(int shareQuantity, double currentPrice) {
        return -((shareQuantity * currentPrice) * this.taxRate) * VAT_MULTIPLIER;
    }

    /**
     * Calculates the total tax as the sum of purchase tax and sale tax.
     * Formula: purchaseTax + saleTax
     *
     * @param purchaseTax the purchase tax amount
     * @param saleTax     the sale tax amount
     * @return the total tax amount
     */
    public double calculateTotalTax(double purchaseTax, double saleTax) {
        return purchaseTax + saleTax;
    }
}
