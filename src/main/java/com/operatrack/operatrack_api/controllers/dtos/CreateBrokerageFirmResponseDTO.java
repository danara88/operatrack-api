package com.operatrack.operatrack_api.controllers.dtos;

public record CreateBrokerageFirmResponseDTO(
        String id,
        String institutionName,
        Double taxRate
) {
}
