package com.operatrack.operatrack_api.controllers.dtos;

public record GetBrokerageFirmResponseDTO(
        String id,
        String institutionName,
        Double taxRate
) {
}
