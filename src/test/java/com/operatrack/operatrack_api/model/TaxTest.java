package com.operatrack.operatrack_api.model;

import com.operatrack.operatrack_api.model.exceptions.InvalidInstitutionNameException;
import com.operatrack.operatrack_api.model.exceptions.InvalidTaxRateException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaxTest {

    // --- Constructor / invariants ---

    @Test
    void constructor_createsValidTax() {
        Tax tax = new Tax("Scotiabank", 0.0035);
        assertNotNull(tax.getId());
        assertDoesNotThrow(() -> UUID.fromString(tax.getId()));
        assertEquals("Scotiabank", tax.getInstitutionName());
        assertEquals(0.0035, tax.getTaxRate());
    }

    @Test
    void constructor_throwsWhenInstitutionNameIsTooShort() {
        InvalidInstitutionNameException ex = assertThrows(InvalidInstitutionNameException.class,
                () -> new Tax("ABC", 0.0035));
        assertEquals("Institution Name must be 4 characters length.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenInstitutionNameIsNull() {
        InvalidInstitutionNameException ex = assertThrows(InvalidInstitutionNameException.class,
                () -> new Tax(null, 0.0035));
        assertEquals("Institution Name must be 4 characters length.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenTaxRateIsNegative() {
        InvalidTaxRateException ex = assertThrows(InvalidTaxRateException.class,
                () -> new Tax("Scotiabank", -0.01));
        assertEquals("Tax rate must be between 0 and 1.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenTaxRateIsGreaterThanOne() {
        InvalidTaxRateException ex = assertThrows(InvalidTaxRateException.class,
                () -> new Tax("Scotiabank", 1.01));
        assertEquals("Tax rate must be between 0 and 1.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenTaxRateIsNull() {
        InvalidTaxRateException ex = assertThrows(InvalidTaxRateException.class,
                () -> new Tax("Scotiabank", null));
        assertEquals("Tax rate must be between 0 and 1.", ex.getMessage());
    }

    @Test
    void constructor_acceptsBoundaryTaxRateOfZero() {
        Tax tax = new Tax("Scotiabank", 0.0);
        assertEquals(0.0, tax.getTaxRate());
    }

    @Test
    void constructor_acceptsBoundaryTaxRateOfOne() {
        Tax tax = new Tax("Scotiabank", 1.0);
        assertEquals(1.0, tax.getTaxRate());
    }

    @Test
    void constructor_acceptsInstitutionNameWithExactlyFourCharacters() {
        Tax tax = new Tax("BBVA", 0.0025);
        assertEquals("BBVA", tax.getInstitutionName());
    }

    // --- Constructor with id / invariants ---

    @Test
    void constructorWithId_createsValidTaxWithProvidedId() {
        String providedId = "custom-tax-id-456";
        Tax tax = new Tax(providedId, "Scotiabank", 0.0035);
        assertEquals(providedId, tax.getId());
        assertEquals("Scotiabank", tax.getInstitutionName());
        assertEquals(0.0035, tax.getTaxRate());
    }

    @Test
    void constructorWithId_acceptsUUIDAsId() {
        String providedId = UUID.randomUUID().toString();
        Tax tax = new Tax(providedId, "Scotiabank", 0.0035);
        assertEquals(providedId, tax.getId());
    }

    @Test
    void constructorWithId_throwsWhenInstitutionNameIsInvalid() {
        assertThrows(InvalidInstitutionNameException.class,
                () -> new Tax("custom-id", "ABC", 0.0035));
    }

    @Test
    void constructorWithId_throwsWhenTaxRateIsInvalid() {
        assertThrows(InvalidTaxRateException.class,
                () -> new Tax("custom-id", "Scotiabank", 1.5));
    }

    // --- calculatePurchaseTax ---

    @Test
    void calculatePurchaseTax_returnsCorrectValue() {
        Tax tax = new Tax("Scotiabank", 0.0035);
        // -[(100 * 50.0) * 0.0035] * 1.16 = -[(5000) * 0.0035] * 1.16 = -17.5 * 1.16 = -20.3
        double result = tax.calculatePurchaseTax(100, 50.0);
        assertEquals(-20.3, result, 1e-9);
    }

    // --- calculateSaleTax ---

    @Test
    void calculateSaleTax_returnsCorrectValue() {
        Tax tax = new Tax("Scotiabank", 0.0035);
        // -[(100 * 55.0) * 0.0035] * 1.16 = -[(5500) * 0.0035] * 1.16 = -19.25 * 1.16 = -22.33
        double result = tax.calculateSaleTax(100, 55.0);
        assertEquals(-22.33, result, 1e-9);
    }

    // --- calculateTotalTax ---

    @Test
    void calculateTotalTax_returnsSumOfPurchaseAndSaleTax() {
        Tax tax = new Tax("Scotiabank", 0.0035);
        double result = tax.calculateTotalTax(-20.3, -22.33);
        assertEquals(-42.63, result, 1e-9);
    }
}
