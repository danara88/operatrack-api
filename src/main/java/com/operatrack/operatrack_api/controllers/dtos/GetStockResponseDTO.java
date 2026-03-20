package com.operatrack.operatrack_api.controllers.dtos;

public record GetStockResponseDTO(
        String id,
        String name,
        String tickerSymbol,
        Double currentPrice
) {
}
