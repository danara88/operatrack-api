package com.operatrack.operatrack_api.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a tax entity associated with a brokerage firm or financial institution.
 * Each institution may apply a different tax rate, which can be reused across multiple operations.
 */
@Getter
@Setter
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
}
