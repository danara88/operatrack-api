package com.operatrack.operatrack_api.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a tax entity associated with a brokerage firm or financial institution.
 * Each institution may apply a different tax rate, which can be reused across multiple operations.
 */
@Data
@Builder
public class Tax {

    /**
     * The unique identifier for the tax entity.
     */
    private String id;

    /**
     * The name of the financial institution or brokerage firm that applies this tax.
     */
    private String institutionName;

    /**
     * The tax rate percentage applied by the institution (e.g., 0.35 for 0.35%).
     */
    private Double taxRatePercentage;

    /**
     * List of operations to which this tax has been applied.
     */
    private List<Operation> operations;
}
