package com.operatrack.operatrack_api.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateStockRequestDTO(
        @NotBlank String name,
        @NotBlank String tickerSymbol,
        @NotNull @PositiveOrZero Double currentPrice
) {
}
