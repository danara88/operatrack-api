package com.operatrack.operatrack_api.controllers.dtos;

public record GetTaxResponseDTO(
        String id,
        String institutionName,
        Double taxRate
) {
}
