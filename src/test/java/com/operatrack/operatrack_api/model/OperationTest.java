package com.operatrack.operatrack_api.model;

import com.operatrack.operatrack_api.model.exceptions.InvalidPurchasePriceException;
import com.operatrack.operatrack_api.model.exceptions.InvalidPurchaseTaxException;
import com.operatrack.operatrack_api.model.exceptions.InvalidSaleTaxException;
import com.operatrack.operatrack_api.model.exceptions.InvalidShareQuantityException;
import com.operatrack.operatrack_api.model.exceptions.InvalidTotalTaxException;
import com.operatrack.operatrack_api.model.exceptions.InvalidTotalValueException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OperationTest {

    // --- Constructor / invariants ---

    @Test
    void constructor_createsValidOperation() {
        Operation operation = new Operation(10, 150.0, 1500.0, 50.0, 5.0, 5.0, 10.0, 35.0, null, null, null);
        assertNotNull(operation.getId());
        assertDoesNotThrow(() -> UUID.fromString(operation.getId()));
        assertEquals(10, operation.getShareQuantity());
        assertEquals(150.0, operation.getPurchasePrice());
        assertEquals(1500.0, operation.getTotalValue());
        assertEquals(50.0, operation.getCapitalGain());
        assertEquals(5.0, operation.getPurchaseTax());
        assertEquals(5.0, operation.getSaleTax());
        assertEquals(10.0, operation.getTotalTax());
        assertEquals(35.0, operation.getNetEarnings());
        assertNotNull(operation.getPurchaseDate());
        assertNull(operation.getSaleDate());
    }

    @Test
    void constructor_acceptsZeroShareQuantity() {
        Operation operation = new Operation(0, 150.0, 0.0, 0.0, 0.0, 0.0, 0.0, null, null, null, null);
        assertEquals(0, operation.getShareQuantity());
    }

    @Test
    void constructor_acceptsZeroPurchasePrice() {
        Operation operation = new Operation(10, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, null, null, null, null);
        assertEquals(0.0, operation.getPurchasePrice());
    }

    @Test
    void constructor_acceptsZeroTotalValue() {
        Operation operation = new Operation(0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, null, null, null, null);
        assertEquals(0.0, operation.getTotalValue());
    }

    @Test
    void constructor_acceptsZeroPurchaseTax() {
        Operation operation = new Operation(10, 150.0, 1500.0, 0.0, 0.0, 0.0, 0.0, null, null, null, null);
        assertEquals(0.0, operation.getPurchaseTax());
    }

    @Test
    void constructor_acceptsZeroSaleTax() {
        Operation operation = new Operation(10, 150.0, 1500.0, 0.0, 0.0, 0.0, 0.0, null, null, null, null);
        assertEquals(0.0, operation.getSaleTax());
    }

    @Test
    void constructor_acceptsZeroTotalTax() {
        Operation operation = new Operation(10, 150.0, 1500.0, 0.0, 0.0, 0.0, 0.0, null, null, null, null);
        assertEquals(0.0, operation.getTotalTax());
    }

    @Test
    void constructor_setsPurchaseDateToNow() {
        Instant before = Instant.now();
        Operation operation = new Operation(10, 150.0, 1500.0, 50.0, 5.0, 5.0, 10.0, null, null, null, null);
        Instant after = Instant.now();
        assertNotNull(operation.getPurchaseDate());
        assertFalse(operation.getPurchaseDate().isBefore(before));
        assertFalse(operation.getPurchaseDate().isAfter(after));
    }

    @Test
    void constructor_throwsWhenShareQuantityIsNull() {
        InvalidShareQuantityException ex = assertThrows(InvalidShareQuantityException.class,
                () -> new Operation(null, 150.0, 1500.0, 50.0, 5.0, 5.0, 10.0, null, null, null, null));
        assertEquals("Share quantity must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenShareQuantityIsNegative() {
        InvalidShareQuantityException ex = assertThrows(InvalidShareQuantityException.class,
                () -> new Operation(-1, 150.0, 1500.0, 50.0, 5.0, 5.0, 10.0, null, null, null, null));
        assertEquals("Share quantity must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenPurchasePriceIsNull() {
        InvalidPurchasePriceException ex = assertThrows(InvalidPurchasePriceException.class,
                () -> new Operation(10, null, 1500.0, 50.0, 5.0, 5.0, 10.0, null, null, null, null));
        assertEquals("Purchase price must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenPurchasePriceIsNegative() {
        InvalidPurchasePriceException ex = assertThrows(InvalidPurchasePriceException.class,
                () -> new Operation(10, -1.0, 1500.0, 50.0, 5.0, 5.0, 10.0, null, null, null, null));
        assertEquals("Purchase price must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenTotalValueIsNull() {
        InvalidTotalValueException ex = assertThrows(InvalidTotalValueException.class,
                () -> new Operation(10, 150.0, null, 50.0, 5.0, 5.0, 10.0, null, null, null, null));
        assertEquals("Total value must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenTotalValueIsNegative() {
        InvalidTotalValueException ex = assertThrows(InvalidTotalValueException.class,
                () -> new Operation(10, 150.0, -1.0, 50.0, 5.0, 5.0, 10.0, null, null, null, null));
        assertEquals("Total value must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenPurchaseTaxIsNull() {
        InvalidPurchaseTaxException ex = assertThrows(InvalidPurchaseTaxException.class,
                () -> new Operation(10, 150.0, 1500.0, 50.0, null, 5.0, 10.0, null, null, null, null));
        assertEquals("Purchase tax must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenPurchaseTaxIsNegative() {
        InvalidPurchaseTaxException ex = assertThrows(InvalidPurchaseTaxException.class,
                () -> new Operation(10, 150.0, 1500.0, 50.0, -1.0, 5.0, 10.0, null, null, null, null));
        assertEquals("Purchase tax must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenSaleTaxIsNull() {
        InvalidSaleTaxException ex = assertThrows(InvalidSaleTaxException.class,
                () -> new Operation(10, 150.0, 1500.0, 50.0, 5.0, null, 10.0, null, null, null, null));
        assertEquals("Sale tax must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenSaleTaxIsNegative() {
        InvalidSaleTaxException ex = assertThrows(InvalidSaleTaxException.class,
                () -> new Operation(10, 150.0, 1500.0, 50.0, 5.0, -1.0, 10.0, null, null, null, null));
        assertEquals("Sale tax must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenTotalTaxIsNull() {
        InvalidTotalTaxException ex = assertThrows(InvalidTotalTaxException.class,
                () -> new Operation(10, 150.0, 1500.0, 50.0, 5.0, 5.0, null, null, null, null, null));
        assertEquals("Total tax must be zero or a positive number.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenTotalTaxIsNegative() {
        InvalidTotalTaxException ex = assertThrows(InvalidTotalTaxException.class,
                () -> new Operation(10, 150.0, 1500.0, 50.0, 5.0, 5.0, -1.0, null, null, null, null));
        assertEquals("Total tax must be zero or a positive number.", ex.getMessage());
    }

    // --- Constructor with id / invariants ---

    @Test
    void constructorWithId_createsValidOperationWithProvidedId() {
        String providedId = "custom-operation-id-123";
        Instant purchaseDate = Instant.parse("2024-01-15T10:00:00Z");
        Operation operation = new Operation(providedId, 10, 150.0, 1500.0, 50.0, 5.0, 5.0, 10.0,
                35.0, purchaseDate, null, null, null);
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
        Operation operation = new Operation(providedId, 10, 150.0, 1500.0, 50.0, 5.0, 5.0, 10.0,
                null, Instant.now(), null, null, null);
        assertEquals(providedId, operation.getId());
    }

    @Test
    void constructorWithId_assignsProvidedPurchaseDate() {
        Instant purchaseDate = Instant.parse("2023-06-01T08:30:00Z");
        Operation operation = new Operation("op-id", 10, 150.0, 1500.0, 50.0, 5.0, 5.0, 10.0,
                null, purchaseDate, null, null, null);
        assertEquals(purchaseDate, operation.getPurchaseDate());
    }

    @Test
    void constructorWithId_throwsWhenShareQuantityIsNegative() {
        assertThrows(InvalidShareQuantityException.class,
                () -> new Operation("op-id", -1, 150.0, 1500.0, 50.0, 5.0, 5.0, 10.0,
                        null, Instant.now(), null, null, null));
    }

    @Test
    void constructorWithId_throwsWhenPurchasePriceIsNegative() {
        assertThrows(InvalidPurchasePriceException.class,
                () -> new Operation("op-id", 10, -1.0, 1500.0, 50.0, 5.0, 5.0, 10.0,
                        null, Instant.now(), null, null, null));
    }

    @Test
    void constructorWithId_throwsWhenTotalValueIsNegative() {
        assertThrows(InvalidTotalValueException.class,
                () -> new Operation("op-id", 10, 150.0, -1.0, 50.0, 5.0, 5.0, 10.0,
                        null, Instant.now(), null, null, null));
    }

    @Test
    void constructorWithId_throwsWhenPurchaseTaxIsNegative() {
        assertThrows(InvalidPurchaseTaxException.class,
                () -> new Operation("op-id", 10, 150.0, 1500.0, 50.0, -1.0, 5.0, 10.0,
                        null, Instant.now(), null, null, null));
    }

    @Test
    void constructorWithId_throwsWhenSaleTaxIsNegative() {
        assertThrows(InvalidSaleTaxException.class,
                () -> new Operation("op-id", 10, 150.0, 1500.0, 50.0, 5.0, -1.0, 10.0,
                        null, Instant.now(), null, null, null));
    }

    @Test
    void constructorWithId_throwsWhenTotalTaxIsNegative() {
        assertThrows(InvalidTotalTaxException.class,
                () -> new Operation("op-id", 10, 150.0, 1500.0, 50.0, 5.0, 5.0, -1.0,
                        null, Instant.now(), null, null, null));
    }
}
