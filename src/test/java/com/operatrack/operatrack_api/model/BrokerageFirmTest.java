package com.operatrack.operatrack_api.model;

import com.operatrack.operatrack_api.model.exceptions.InvalidInstitutionNameException;
import com.operatrack.operatrack_api.model.exceptions.InvalidBrokerageFirmRateException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BrokerageFirmTest {

    // --- Constructor / invariants ---

    @Test
    void constructor_createsValidBrokerageFirm() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 0.0035);
        assertNull(brokerageFirm.getId());
        assertEquals("Scotiabank", brokerageFirm.getInstitutionName());
        assertEquals(0.0035, brokerageFirm.getTaxRate());
    }

    @Test
    void constructor_throwsWhenInstitutionNameIsTooShort() {
        InvalidInstitutionNameException ex = assertThrows(InvalidInstitutionNameException.class,
                () -> new BrokerageFirm("ABC", 0.0035));
        assertEquals("Institution Name must be 4 characters length.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenInstitutionNameIsNull() {
        InvalidInstitutionNameException ex = assertThrows(InvalidInstitutionNameException.class,
                () -> new BrokerageFirm(null, 0.0035));
        assertEquals("Institution Name must be 4 characters length.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenTaxRateIsNegative() {
        InvalidBrokerageFirmRateException ex = assertThrows(InvalidBrokerageFirmRateException.class,
                () -> new BrokerageFirm("Scotiabank", -0.01));
        assertEquals("Tax rate must be between 0 and 1.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenTaxRateIsGreaterThanOne() {
        InvalidBrokerageFirmRateException ex = assertThrows(InvalidBrokerageFirmRateException.class,
                () -> new BrokerageFirm("Scotiabank", 1.01));
        assertEquals("Tax rate must be between 0 and 1.", ex.getMessage());
    }

    @Test
    void constructor_throwsWhenTaxRateIsNull() {
        InvalidBrokerageFirmRateException ex = assertThrows(InvalidBrokerageFirmRateException.class,
                () -> new BrokerageFirm("Scotiabank", null));
        assertEquals("Tax rate must be between 0 and 1.", ex.getMessage());
    }

    @Test
    void constructor_acceptsBoundaryTaxRateOfZero() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 0.0);
        assertEquals(0.0, brokerageFirm.getTaxRate());
    }

    @Test
    void constructor_acceptsBoundaryTaxRateOfOne() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 1.0);
        assertEquals(1.0, brokerageFirm.getTaxRate());
    }

    @Test
    void constructor_acceptsInstitutionNameWithExactlyFourCharacters() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("BBVA", 0.0025);
        assertEquals("BBVA", brokerageFirm.getInstitutionName());
    }

    // --- Constructor with id / invariants ---

    @Test
    void constructorWithId_createsValidBrokerageFirmWithProvidedId() {
        String providedId = "custom-id-456";
        BrokerageFirm brokerageFirm = new BrokerageFirm(providedId, "Scotiabank", 0.0035);
        assertEquals(providedId, brokerageFirm.getId());
        assertEquals("Scotiabank", brokerageFirm.getInstitutionName());
        assertEquals(0.0035, brokerageFirm.getTaxRate());
    }

    @Test
    void constructorWithId_acceptsUUIDAsId() {
        String providedId = UUID.randomUUID().toString();
        BrokerageFirm brokerageFirm = new BrokerageFirm(providedId, "Scotiabank", 0.0035);
        assertEquals(providedId, brokerageFirm.getId());
    }

    @Test
    void constructorWithId_throwsWhenInstitutionNameIsInvalid() {
        assertThrows(InvalidInstitutionNameException.class,
                () -> new BrokerageFirm("custom-id", "ABC", 0.0035));
    }

    @Test
    void constructorWithId_throwsWhenTaxRateIsInvalid() {
        assertThrows(InvalidBrokerageFirmRateException.class,
                () -> new BrokerageFirm("custom-id", "Scotiabank", 1.5));
    }

    // --- calculatePurchaseTax ---

    @Test
    void calculatePurchaseTax_returnsCorrectValue() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 0.0035);
        // [(100 * 50.0) * 0.0035] * 1.16 = [(5000) * 0.0035] * 1.16 = 17.5 * 1.16 = 20.3
        double result = brokerageFirm.calculatePurchaseTax(100, 50.0);
        assertEquals(20.3, result, 1e-9);
    }

    // --- calculateSaleTax ---

    @Test
    void calculateSaleTax_returnsCorrectValue() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 0.0035);
        // [(100 * 55.0) * 0.0035] * 1.16 = [(5500) * 0.0035] * 1.16 = 19.25 * 1.16 = 22.33
        double result = brokerageFirm.calculateSaleTax(100, 55.0);
        assertEquals(22.33, result, 1e-9);
    }

    // --- calculateTotalTax ---

    @Test
    void calculateTotalTax_returnsSumOfPurchaseAndSaleTax() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 0.0035);
        // calculatePurchaseTax(100, 50.0) = 20.3, calculateSaleTax(100, 55.0) = 22.33 => 42.63
        double result = brokerageFirm.calculateTotalTax(100, 50.0, 55.0);
        assertEquals(42.63, result, 1e-9);
    }

    // --- updateInstitutionName ---

    @Test
    void updateInstitutionName_updatesName_whenValid() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 0.0035);
        brokerageFirm.updateInstitutionName("Banorte");
        assertEquals("Banorte", brokerageFirm.getInstitutionName());
    }

    @Test
    void updateInstitutionName_throwsWhenNameIsNull() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 0.0035);
        InvalidInstitutionNameException ex = assertThrows(InvalidInstitutionNameException.class,
                () -> brokerageFirm.updateInstitutionName(null));
        assertEquals("Institution Name must be 4 characters length.", ex.getMessage());
    }

    @Test
    void updateInstitutionName_throwsWhenNameIsTooShort() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 0.0035);
        InvalidInstitutionNameException ex = assertThrows(InvalidInstitutionNameException.class,
                () -> brokerageFirm.updateInstitutionName("ABC"));
        assertEquals("Institution Name must be 4 characters length.", ex.getMessage());
    }

    @Test
    void updateInstitutionName_acceptsNameWithExactlyFourCharacters() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 0.0035);
        brokerageFirm.updateInstitutionName("BBVA");
        assertEquals("BBVA", brokerageFirm.getInstitutionName());
    }

    // --- updateTaxRate ---

    @Test
    void updateTaxRate_updatesRate_whenValid() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 0.0035);
        brokerageFirm.updateTaxRate(0.005);
        assertEquals(0.005, brokerageFirm.getTaxRate());
    }

    @Test
    void updateTaxRate_throwsWhenRateIsNull() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 0.0035);
        InvalidBrokerageFirmRateException ex = assertThrows(InvalidBrokerageFirmRateException.class,
                () -> brokerageFirm.updateTaxRate(null));
        assertEquals("Tax rate must be between 0 and 1.", ex.getMessage());
    }

    @Test
    void updateTaxRate_throwsWhenRateIsNegative() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 0.0035);
        InvalidBrokerageFirmRateException ex = assertThrows(InvalidBrokerageFirmRateException.class,
                () -> brokerageFirm.updateTaxRate(-0.01));
        assertEquals("Tax rate must be between 0 and 1.", ex.getMessage());
    }

    @Test
    void updateTaxRate_throwsWhenRateIsGreaterThanOne() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 0.0035);
        InvalidBrokerageFirmRateException ex = assertThrows(InvalidBrokerageFirmRateException.class,
                () -> brokerageFirm.updateTaxRate(1.01));
        assertEquals("Tax rate must be between 0 and 1.", ex.getMessage());
    }

    @Test
    void updateTaxRate_acceptsBoundaryRateOfZero() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 0.0035);
        brokerageFirm.updateTaxRate(0.0);
        assertEquals(0.0, brokerageFirm.getTaxRate());
    }

    @Test
    void updateTaxRate_acceptsBoundaryRateOfOne() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("Scotiabank", 0.0035);
        brokerageFirm.updateTaxRate(1.0);
        assertEquals(1.0, brokerageFirm.getTaxRate());
    }
}
