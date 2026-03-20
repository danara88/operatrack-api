package com.operatrack.operatrack_api.database.mappers;

import com.operatrack.operatrack_api.database.entities.TaxEntity;
import com.operatrack.operatrack_api.model.Tax;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaxJpaMapperTest {

    // --- toDomain ---

    @Test
    void toDomain_mapsAllFieldsCorrectly() {
        TaxEntity entity = TaxEntity.builder()
                .id("tax-uuid-1")
                .institutionName("Scotiabank")
                .taxRate(0.0035)
                .build();

        Tax tax = TaxJpaMapper.toDomain(entity);

        assertEquals("tax-uuid-1", tax.getId());
        assertEquals("Scotiabank", tax.getInstitutionName());
        assertEquals(0.0035, tax.getTaxRate());
    }

    @Test
    void toDomain_mapsZeroTaxRate() {
        TaxEntity entity = TaxEntity.builder()
                .id("tax-uuid-2")
                .institutionName("BBVA Bank")
                .taxRate(0.0)
                .build();

        Tax tax = TaxJpaMapper.toDomain(entity);

        assertEquals(0.0, tax.getTaxRate());
    }

    // --- toEntity ---

    @Test
    void toEntity_mapsAllFieldsCorrectly() {
        Tax tax = new Tax("tax-uuid-3", "Banorte", 0.0025);

        TaxEntity entity = TaxJpaMapper.toEntity(tax);

        assertEquals("tax-uuid-3", entity.getId());
        assertEquals("Banorte", entity.getInstitutionName());
        assertEquals(0.0025, entity.getTaxRate());
    }

    @Test
    void toEntity_mapsZeroTaxRate() {
        Tax tax = new Tax("tax-uuid-4", "Zero Bank", 0.0);

        TaxEntity entity = TaxJpaMapper.toEntity(tax);

        assertEquals(0.0, entity.getTaxRate());
    }

    @Test
    void toDomain_andToEntity_areSymmetric() {
        TaxEntity original = TaxEntity.builder()
                .id("tax-uuid-5")
                .institutionName("HSBC Bank")
                .taxRate(0.005)
                .build();

        TaxEntity roundTripped = TaxJpaMapper.toEntity(TaxJpaMapper.toDomain(original));

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getInstitutionName(), roundTripped.getInstitutionName());
        assertEquals(original.getTaxRate(), roundTripped.getTaxRate());
    }
}
