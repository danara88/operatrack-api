package com.operatrack.operatrack_api.controllers.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateStockPriceRequestDTO(
        @NotNull @PositiveOrZero Double currentPrice
) {
}
