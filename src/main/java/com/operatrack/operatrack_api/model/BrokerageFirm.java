package com.operatrack.operatrack_api.model;

import com.operatrack.operatrack_api.model.exceptions.InvalidInstitutionNameException;
import com.operatrack.operatrack_api.model.exceptions.InvalidBrokerageFirmRateException;
import lombok.Getter;

/**
 * Represents a brokerage firm or financial institution.
 * Each institution may apply a different tax rate, which can be reused across multiple operations.
 */
public class BrokerageFirm {

    private static final double VAT_MULTIPLIER = 1.16;

    /**
     * The unique identifier for the brokerage firm.
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
     * Creates a new BrokerageFirm with the given institution name and tax rate.
     * The unique identifier is automatically generated as a random UUID string.
     *
     * @param institutionName the name of the financial institution (must be at least 4 characters)
     * @param taxRate         the decimal tax rate (must be between 0 and 1)
     * @throws InvalidInstitutionNameException if the institution name is null or fewer than 4 characters
     * @throws InvalidBrokerageFirmRateException if the tax rate is null or not between 0 and 1
     */
    public BrokerageFirm(String institutionName, Double taxRate) {
        validateFields(institutionName, taxRate);
        this.institutionName = institutionName;
        this.taxRate = taxRate;
    }

    /**
     * Creates a new BrokerageFirm with a provided identifier, institution name, and tax rate.
     * Use this constructor when the unique identifier is known in advance (e.g., when restoring from persistence).
     *
     * @param id              the unique identifier to assign to this brokerage firm
     * @param institutionName the name of the financial institution (must be at least 4 characters)
     * @param taxRate         the decimal tax rate (must be between 0 and 1)
     * @throws InvalidInstitutionNameException if the institution name is null or fewer than 4 characters
     * @throws InvalidBrokerageFirmRateException if the tax rate is null or not between 0 and 1
     */
    public BrokerageFirm(String id, String institutionName, Double taxRate) {
        validateFields(institutionName, taxRate);
        this.id = id;
        this.institutionName = institutionName;
        this.taxRate = taxRate;
    }

    public void updateInstitutionName(String institutionName) {
        if (institutionName == null || institutionName.length() < 4) {
            throw new InvalidInstitutionNameException("Institution Name must be 4 characters length.");
        }
        this.institutionName = institutionName;
    }

    public void updateTaxRate(Double taxRate) {
        if (taxRate == null || taxRate < 0 || taxRate > 1) {
            throw new InvalidBrokerageFirmRateException("Tax rate must be between 0 and 1.");
        }
        this.taxRate = taxRate;
    }

    private void validateFields(String institutionName, Double taxRate) {
        if (institutionName == null || institutionName.length() < 4) {
            throw new InvalidInstitutionNameException("Institution Name must be 4 characters length.");
        }
        if (taxRate == null || taxRate < 0 || taxRate > 1) {
            throw new InvalidBrokerageFirmRateException("Tax rate must be between 0 and 1.");
        }
    }

    /**
     * Calculates the purchase tax for an operation.
     * Formula: [(shareQuantity x purchasePrice) x taxRate] x 1.16
     *
     * @param shareQuantity the number of shares purchased
     * @param purchasePrice the price per share at the time of purchase
     * @return the purchase tax amount (positive value)
     */
    public double calculatePurchaseTax(int shareQuantity, double purchasePrice) {
        return ((shareQuantity * purchasePrice) * this.taxRate) * VAT_MULTIPLIER;
    }

    /**
     * Calculates the sale tax for an operation.
     * Formula: [(shareQuantity x currentPrice) x taxRate] x 1.16
     *
     * @param shareQuantity the number of shares sold
     * @param currentPrice  the current market price per share
     * @return the sale tax amount (positive value)
     */
    public double calculateSaleTax(int shareQuantity, double currentPrice) {
        return ((shareQuantity * currentPrice) * this.taxRate) * VAT_MULTIPLIER;
    }

    /**
     * Calculates the total tax as the sum of purchase tax and sale tax.
     * Formula: calculatePurchaseTax(shareQuantity, purchasePrice) + calculateSaleTax(shareQuantity, currentPrice)
     *
     * @param shareQuantity the number of shares in the operation
     * @param purchasePrice the price per share at the time of purchase
     * @param currentPrice  the current market price per share
     * @return the total tax amount
     */
    public double calculateTotalTax(int shareQuantity, double purchasePrice, double currentPrice) {
        return calculatePurchaseTax(shareQuantity, purchasePrice) + calculateSaleTax(shareQuantity, currentPrice);
    }
}
