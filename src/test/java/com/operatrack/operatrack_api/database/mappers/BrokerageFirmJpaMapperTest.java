package com.operatrack.operatrack_api.database.mappers;

import com.operatrack.operatrack_api.database.entities.BrokerageFirmEntity;
import com.operatrack.operatrack_api.model.BrokerageFirm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrokerageFirmJpaMapperTest {

    // --- toDomain ---

    @Test
    void toDomain_mapsAllFieldsCorrectly() {
        BrokerageFirmEntity entity = BrokerageFirmEntity.builder()
                .id("brokerage-firm-uuid-1")
                .institutionName("Scotiabank")
                .taxRate(0.0035)
                .build();

        BrokerageFirm brokerageFirm = BrokerageFirmJpaMapper.toDomain(entity);

        assertEquals("brokerage-firm-uuid-1", brokerageFirm.getId());
        assertEquals("Scotiabank", brokerageFirm.getInstitutionName());
        assertEquals(0.0035, brokerageFirm.getTaxRate());
    }

    @Test
    void toDomain_mapsZeroTaxRate() {
        BrokerageFirmEntity entity = BrokerageFirmEntity.builder()
                .id("brokerage-firm-uuid-2")
                .institutionName("BBVA Bank")
                .taxRate(0.0)
                .build();

        BrokerageFirm brokerageFirm = BrokerageFirmJpaMapper.toDomain(entity);

        assertEquals(0.0, brokerageFirm.getTaxRate());
    }

    // --- toEntity ---

    @Test
    void toEntity_mapsAllFieldsCorrectly() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("brokerage-firm-uuid-3", "Banorte", 0.0025);

        BrokerageFirmEntity entity = BrokerageFirmJpaMapper.toEntity(brokerageFirm);

        assertEquals("brokerage-firm-uuid-3", entity.getId());
        assertEquals("Banorte", entity.getInstitutionName());
        assertEquals(0.0025, entity.getTaxRate());
    }

    @Test
    void toEntity_mapsZeroTaxRate() {
        BrokerageFirm brokerageFirm = new BrokerageFirm("brokerage-firm-uuid-4", "Zero Bank", 0.0);

        BrokerageFirmEntity entity = BrokerageFirmJpaMapper.toEntity(brokerageFirm);

        assertEquals(0.0, entity.getTaxRate());
    }

    @Test
    void toDomain_andToEntity_areSymmetric() {
        BrokerageFirmEntity original = BrokerageFirmEntity.builder()
                .id("brokerage-firm-uuid-5")
                .institutionName("HSBC Bank")
                .taxRate(0.005)
                .build();

        BrokerageFirmEntity roundTripped = BrokerageFirmJpaMapper.toEntity(BrokerageFirmJpaMapper.toDomain(original));

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getInstitutionName(), roundTripped.getInstitutionName());
        assertEquals(original.getTaxRate(), roundTripped.getTaxRate());
    }
}
