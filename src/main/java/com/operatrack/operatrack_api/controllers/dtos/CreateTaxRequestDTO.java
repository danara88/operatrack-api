package com.operatrack.operatrack_api.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateTaxRequestDTO(
        @NotBlank String institutionName,
        @NotNull @PositiveOrZero Double taxRate
) {
}
