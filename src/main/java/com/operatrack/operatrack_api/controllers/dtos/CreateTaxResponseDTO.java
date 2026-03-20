package com.operatrack.operatrack_api.controllers.dtos;

public record CreateTaxResponseDTO(
        String id,
        String institutionName,
        Double taxRate
) {
}
