package com.operatrack.operatrack_api.model;

import com.operatrack.operatrack_api.model.exceptions.InvalidPurchasePriceException;
import com.operatrack.operatrack_api.model.exceptions.InvalidShareQuantityException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OperationTest {

    // --- Constructor / invariants ---

    @Test
    void constructor_createsValidOperation() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        Operation operation = new Operation(10, 150.0, 160.0, null, null, brokerageFirm);
        assertNull(operation.getId());
        assertEquals(10, operation.getShareQuantity());
        assertEquals(150.0, operation.getPurchasePrice());
        assertEquals(10 * 160.0, operation.getTotalValue(), 1e-9);
        assertEquals((10 * 160.0) - (10 * 150.0), operation.getCapitalGain(), 1e-9);
        assertEquals(brokerageFirm.calculatePurchaseTax(10, 150.0), operation.getPurchaseTax(), 1e-9);
        assertEquals(brokerageFirm.calculateSaleTax(10, 160.0), operation.getSaleTax(), 1e-9);
        assertEquals(brokerageFirm.calculateTotalTax(10, 150.0, 160.0), operation.getTotalTax(), 1e-9);
        double expectedCapitalGain = (10 * 160.0) - (10 * 150.0);
        double expectedNetEarnings = expectedCapitalGain - brokerageFirm.calculateTotalTax(10, 150.0, 160.0);
        assertEquals(expectedNetEarnings, operation.getNetEarnings(), 1e-9);
        assertNotNull(operation.getPurchaseDate());
        assertNull(operation.getSaleDate());
    }

    @Test
    void constructor_acceptsZeroShareQuantity() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0);
        Operation operation = new Operation(0, 150.0, 150.0, null, null, brokerageFirm);
        assertEquals(0, operation.getShareQuantity());
        assertEquals(0.0, operation.getTotalValue());
        assertEquals(0.0, operation.getCapitalGain());
        assertEquals(0.0, operation.getNetEarnings());
    }

    @Test
    void constructor_acceptsZeroPurchasePrice() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0);
        Operation operation = new Operation(10, 0.0, 0.0, null, null, brokerageFirm);
        assertEquals(0.0, operation.getPurchasePrice());
        assertEquals(0.0, operation.getTotalValue());
    }

    @Test
    void constructor_computesZeroTotalValueWhenShareQuantityIsZero() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0);
        Operation operation = new Operation(0, 0.0, 0.0, null, null, brokerageFirm);
        assertEquals(0.0, operation.getTotalValue());
    }

    @Test
    void constructor_computesZeroPurchaseTaxWhenTaxRateIsZero() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0);
        Operation operation = new Operation(10, 150.0, 150.0, null, null, brokerageFirm);
        assertEquals(0.0, operation.getPurchaseTax());
    }

    @Test
    void constructor_computesZeroSaleTaxWhenTaxRateIsZero() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0);
        Operation operation = new Operation(10, 150.0, 150.0, null, null, brokerageFirm);
        assertEquals(0.0, operation.getSaleTax());
    }

    @Test
    void constructor_computesZeroTotalTaxWhenTaxRateIsZero() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0);
        Operation operation = new Operation(10, 150.0, 150.0, null, null, brokerageFirm);
        assertEquals(0.0, operation.getTotalTax());
    }

    @Test
    void constructor_setsPurchaseDateToNow() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        Instant before = Instant.now();
        Operation operation = new Operation(10, 150.0, 150.0, null, null, brokerageFirm);
        Instant after = Instant.now();
        assertNotNull(operation.getPurchaseDate());
        assertFalse(operation.getPurchaseDate().isBefore(before));
        assertFalse(operation.getPurchaseDate().isAfter(after));
    }

    @Test
    void constructor_throwsWhenShareQuantityIsNull() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        InvalidShareQuantityException ex = assertThrows(InvalidShareQuantityException.class,
                () -> new Operation(null, 150.0, 150.0, null, null, brokerageFirm));
        assertEquals("Share quantity must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenShareQuantityIsNegative() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        InvalidShareQuantityException ex = assertThrows(InvalidShareQuantityException.class,
                () -> new Operation(-1, 150.0, 150.0, null, null, brokerageFirm));
        assertEquals("Share quantity must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenPurchasePriceIsNull() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        InvalidPurchasePriceException ex = assertThrows(InvalidPurchasePriceException.class,
                () -> new Operation(10, null, 150.0, null, null, brokerageFirm));
        assertEquals("Purchase price must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenPurchasePriceIsNegative() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        InvalidPurchasePriceException ex = assertThrows(InvalidPurchasePriceException.class,
                () -> new Operation(10, -1.0, 150.0, null, null, brokerageFirm));
        assertEquals("Purchase price must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_computesTotalValueFromShareQuantityAndCurrentPrice() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        Operation operation = new Operation(10, 150.0, 200.0, null, null, brokerageFirm);
        assertEquals(10 * 200.0, operation.getTotalValue(), 1e-9);
    }

    @Test
    void constructor_computesCapitalGainFromPrices() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        Operation operation = new Operation(10, 150.0, 200.0, null, null, brokerageFirm);
        assertEquals((10 * 200.0) - (10 * 150.0), operation.getCapitalGain(), 1e-9);
    }

    @Test
    void constructor_computesNetEarningsFromCapitalGainMinusTotalTax() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        Operation operation = new Operation(10, 150.0, 200.0, null, null, brokerageFirm);
        double expectedCapitalGain = (10 * 200.0) - (10 * 150.0);
        double expectedTotalTax = brokerageFirm.calculateTotalTax(10, 150.0, 200.0);
        assertEquals(expectedCapitalGain - expectedTotalTax, operation.getNetEarnings(), 1e-9);
    }

    // --- Constructor with id / invariants ---

    @Test
    void constructorWithId_createsValidOperationWithProvidedId() {
        String providedId = "custom-operation-id-123";
        Instant purchaseDate = Instant.parse("2024-01-15T10:00:00Z");
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        Operation operation = new Operation(providedId, 10, 150.0, 160.0,
                purchaseDate, null, null, brokerageFirm);
        assertEquals(providedId, operation.getId());
        assertEquals(10, operation.getShareQuantity());
        assertEquals(150.0, operation.getPurchasePrice());
        assertEquals(10 * 160.0, operation.getTotalValue(), 1e-9);
        assertEquals((10 * 160.0) - (10 * 150.0), operation.getCapitalGain(), 1e-9);
        assertEquals(purchaseDate, operation.getPurchaseDate());
        assertNull(operation.getSaleDate());
    }

    @Test
    void constructorWithId_acceptsUUIDAsId() {
        String providedId = UUID.randomUUID().toString();
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        Operation operation = new Operation(providedId, 10, 150.0, 160.0,
                Instant.now(), null, null, brokerageFirm);
        assertEquals(providedId, operation.getId());
    }

    @Test
    void constructorWithId_assignsProvidedPurchaseDate() {
        Instant purchaseDate = Instant.parse("2023-06-01T08:30:00Z");
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        Operation operation = new Operation("op-id", 10, 150.0, 160.0,
                purchaseDate, null, null, brokerageFirm);
        assertEquals(purchaseDate, operation.getPurchaseDate());
    }

    @Test
    void constructorWithId_computesTaxValuesFromTaxInstance() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        Operation operation = new Operation("op-id", 10, 150.0, 160.0,
                Instant.now(), null, null, brokerageFirm);
        assertEquals(brokerageFirm.calculatePurchaseTax(10, 150.0), operation.getPurchaseTax(), 1e-9);
        assertEquals(brokerageFirm.calculateSaleTax(10, 160.0), operation.getSaleTax(), 1e-9);
        assertEquals(brokerageFirm.calculateTotalTax(10, 150.0, 160.0), operation.getTotalTax(), 1e-9);
    }

    @Test
    void constructorWithId_computesNetEarningsFromCapitalGainMinusTotalTax() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        Operation operation = new Operation("op-id", 10, 150.0, 160.0,
                Instant.now(), null, null, brokerageFirm);
        double expectedCapitalGain = (10 * 160.0) - (10 * 150.0);
        double expectedTotalTax = brokerageFirm.calculateTotalTax(10, 150.0, 160.0);
        assertEquals(expectedCapitalGain - expectedTotalTax, operation.getNetEarnings(), 1e-9);
    }

    @Test
    void constructorWithId_throwsWhenShareQuantityIsNegative() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        assertThrows(InvalidShareQuantityException.class,
                () -> new Operation("op-id", -1, 150.0, 160.0,
                        Instant.now(), null, null, brokerageFirm));
    }

    @Test
    void constructorWithId_throwsWhenPurchasePriceIsNegative() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0035);
        assertThrows(InvalidPurchasePriceException.class,
                () -> new Operation("op-id", 10, -1.0, 160.0,
                        Instant.now(), null, null, brokerageFirm));
    }
}

