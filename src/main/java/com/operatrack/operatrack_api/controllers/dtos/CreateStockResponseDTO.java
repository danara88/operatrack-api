package com.operatrack.operatrack_api.controllers.dtos;

public record CreateStockResponseDTO(
        String id,
        String name,
        String tickerSymbol,
        Double currentPrice
) {
}
