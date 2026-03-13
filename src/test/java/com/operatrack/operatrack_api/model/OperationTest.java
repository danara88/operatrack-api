package com.operatrack.operatrack_api.model;

import com.operatrack.operatrack_api.model.exceptions.InvalidPurchasePriceException;
import com.operatrack.operatrack_api.model.exceptions.InvalidShareQuantityException;
import com.operatrack.operatrack_api.model.exceptions.InvalidTotalValueException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OperationTest {

    // --- Constructor / invariants ---

    @Test
    void constructor_createsValidOperation() {
        Tax tax = new Tax("BBVA", 0.0035);
        Operation operation = new Operation(10, 150.0, 160.0, 1500.0, 50.0, 35.0, null, null, tax);
        assertNotNull(operation.getId());
        assertDoesNotThrow(() -> UUID.fromString(operation.getId()));
        assertEquals(10, operation.getShareQuantity());
        assertEquals(150.0, operation.getPurchasePrice());
        assertEquals(1500.0, operation.getTotalValue());
        assertEquals(50.0, operation.getCapitalGain());
        assertEquals(tax.calculatePurchaseTax(10, 150.0), operation.getPurchaseTax(), 1e-9);
        assertEquals(tax.calculateSaleTax(10, 160.0), operation.getSaleTax(), 1e-9);
        assertEquals(tax.calculateTotalTax(10, 150.0, 160.0), operation.getTotalTax(), 1e-9);
        assertEquals(35.0, operation.getNetEarnings());
        assertNotNull(operation.getPurchaseDate());
        assertNull(operation.getSaleDate());
    }

    @Test
    void constructor_acceptsZeroShareQuantity() {
        Tax tax = new Tax("BBVA", 0.0);
        Operation operation = new Operation(0, 150.0, 150.0, 0.0, 0.0, null, null, null, tax);
        assertEquals(0, operation.getShareQuantity());
    }

    @Test
    void constructor_acceptsZeroPurchasePrice() {
        Tax tax = new Tax("BBVA", 0.0);
        Operation operation = new Operation(10, 0.0, 0.0, 0.0, 0.0, null, null, null, tax);
        assertEquals(0.0, operation.getPurchasePrice());
    }

    @Test
    void constructor_acceptsZeroTotalValue() {
        Tax tax = new Tax("BBVA", 0.0);
        Operation operation = new Operation(0, 0.0, 0.0, 0.0, 0.0, null, null, null, tax);
        assertEquals(0.0, operation.getTotalValue());
    }

    @Test
    void constructor_computesZeroPurchaseTaxWhenTaxRateIsZero() {
        Tax tax = new Tax("BBVA", 0.0);
        Operation operation = new Operation(10, 150.0, 150.0, 1500.0, 0.0, null, null, null, tax);
        assertEquals(0.0, operation.getPurchaseTax());
    }

    @Test
    void constructor_computesZeroSaleTaxWhenTaxRateIsZero() {
        Tax tax = new Tax("BBVA", 0.0);
        Operation operation = new Operation(10, 150.0, 150.0, 1500.0, 0.0, null, null, null, tax);
        assertEquals(0.0, operation.getSaleTax());
    }

    @Test
    void constructor_computesZeroTotalTaxWhenTaxRateIsZero() {
        Tax tax = new Tax("BBVA", 0.0);
        Operation operation = new Operation(10, 150.0, 150.0, 1500.0, 0.0, null, null, null, tax);
        assertEquals(0.0, operation.getTotalTax());
    }

    @Test
    void constructor_setsPurchaseDateToNow() {
        Tax tax = new Tax("BBVA", 0.0035);
        Instant before = Instant.now();
        Operation operation = new Operation(10, 150.0, 150.0, 1500.0, 50.0, null, null, null, tax);
        Instant after = Instant.now();
        assertNotNull(operation.getPurchaseDate());
        assertFalse(operation.getPurchaseDate().isBefore(before));
        assertFalse(operation.getPurchaseDate().isAfter(after));
    }

    @Test
    void constructor_throwsWhenShareQuantityIsNull() {
        Tax tax = new Tax("BBVA", 0.0035);
        InvalidShareQuantityException ex = assertThrows(InvalidShareQuantityException.class,
                () -> new Operation(null, 150.0, 150.0, 1500.0, 50.0, null, null, null, tax));
        assertEquals("Share quantity must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenShareQuantityIsNegative() {
        Tax tax = new Tax("BBVA", 0.0035);
        InvalidShareQuantityException ex = assertThrows(InvalidShareQuantityException.class,
                () -> new Operation(-1, 150.0, 150.0, 1500.0, 50.0, null, null, null, tax));
        assertEquals("Share quantity must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenPurchasePriceIsNull() {
        Tax tax = new Tax("BBVA", 0.0035);
        InvalidPurchasePriceException ex = assertThrows(InvalidPurchasePriceException.class,
                () -> new Operation(10, null, 150.0, 1500.0, 50.0, null, null, null, tax));
        assertEquals("Purchase price must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenPurchasePriceIsNegative() {
        Tax tax = new Tax("BBVA", 0.0035);
        InvalidPurchasePriceException ex = assertThrows(InvalidPurchasePriceException.class,
                () -> new Operation(10, -1.0, 150.0, 1500.0, 50.0, null, null, null, tax));
        assertEquals("Purchase price must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenTotalValueIsNull() {
        Tax tax = new Tax("BBVA", 0.0035);
        InvalidTotalValueException ex = assertThrows(InvalidTotalValueException.class,
                () -> new Operation(10, 150.0, 150.0, null, 50.0, null, null, null, tax));
        assertEquals("Total value must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenTotalValueIsNegative() {
        Tax tax = new Tax("BBVA", 0.0035);
        InvalidTotalValueException ex = assertThrows(InvalidTotalValueException.class,
                () -> new Operation(10, 150.0, 150.0, -1.0, 50.0, null, null, null, tax));
        assertEquals("Total value must be zero or a positive number.", ex.getMessage());
    }

    // --- Constructor with id / invariants ---

    @Test
    void constructorWithId_createsValidOperationWithProvidedId() {
        String providedId = "custom-operation-id-123";
        Instant purchaseDate = Instant.parse("2024-01-15T10:00:00Z");
        Tax tax = new Tax("BBVA", 0.0035);
        Operation operation = new Operation(providedId, 10, 150.0, 160.0, 1500.0, 50.0,
                35.0, purchaseDate, null, null, tax);
        assertEquals(providedId, operation.getId());
        assertEquals(10, operation.getShareQuantity());
        assertEquals(150.0, operation.getPurchasePrice());
        assertEquals(1500.0, operation.getTotalValue());
        assertEquals(purchaseDate, operation.getPurchaseDate());
        assertNull(operation.getSaleDate());
    }

    @Test
    void constructorWithId_acceptsUUIDAsId() {
        String providedId = UUID.randomUUID().toString();
        Tax tax = new Tax("BBVA", 0.0035);
        Operation operation = new Operation(providedId, 10, 150.0, 160.0, 1500.0, 50.0,
                null, Instant.now(), null, null, tax);
        assertEquals(providedId, operation.getId());
    }

    @Test
    void constructorWithId_assignsProvidedPurchaseDate() {
        Instant purchaseDate = Instant.parse("2023-06-01T08:30:00Z");
        Tax tax = new Tax("BBVA", 0.0035);
        Operation operation = new Operation("op-id", 10, 150.0, 160.0, 1500.0, 50.0,
                null, purchaseDate, null, null, tax);
        assertEquals(purchaseDate, operation.getPurchaseDate());
    }

    @Test
    void constructorWithId_computesTaxValuesFromTaxInstance() {
        Tax tax = new Tax("BBVA", 0.0035);
        Operation operation = new Operation("op-id", 10, 150.0, 160.0, 1500.0, 50.0,
                35.0, Instant.now(), null, null, tax);
        assertEquals(tax.calculatePurchaseTax(10, 150.0), operation.getPurchaseTax(), 1e-9);
        assertEquals(tax.calculateSaleTax(10, 160.0), operation.getSaleTax(), 1e-9);
        assertEquals(tax.calculateTotalTax(10, 150.0, 160.0), operation.getTotalTax(), 1e-9);
    }

    @Test
    void constructorWithId_throwsWhenShareQuantityIsNegative() {
        Tax tax = new Tax("BBVA", 0.0035);
        assertThrows(InvalidShareQuantityException.class,
                () -> new Operation("op-id", -1, 150.0, 160.0, 1500.0, 50.0,
                        null, Instant.now(), null, null, tax));
    }

    @Test
    void constructorWithId_throwsWhenPurchasePriceIsNegative() {
        Tax tax = new Tax("BBVA", 0.0035);
        assertThrows(InvalidPurchasePriceException.class,
                () -> new Operation("op-id", 10, -1.0, 160.0, 1500.0, 50.0,
                        null, Instant.now(), null, null, tax));
    }

    @Test
    void constructorWithId_throwsWhenTotalValueIsNegative() {
        Tax tax = new Tax("BBVA", 0.0035);
        assertThrows(InvalidTotalValueException.class,
                () -> new Operation("op-id", 10, 150.0, 160.0, -1.0, 50.0,
                        null, Instant.now(), null, null, tax));
    }
}

