package com.operatrack.operatrack_api.controllers;

import com.operatrack.operatrack_api.controllers.dtos.CreateStockRequestDTO;
import com.operatrack.operatrack_api.controllers.dtos.CreateStockResponseDTO;
import com.operatrack.operatrack_api.controllers.responses.ApiResponse;
import com.operatrack.operatrack_api.model.Stock;
import com.operatrack.operatrack_api.services.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CreateStockResponseDTO> createStock(@RequestBody @Valid CreateStockRequestDTO request) {
        Stock stock = stockService.create(request.name(), request.tickerSymbol(), request.currentPrice());
        CreateStockResponseDTO data = new CreateStockResponseDTO(stock.getId(), stock.getName(), stock.getTickerSymbol(), stock.getCurrentPrice());
        return ApiResponse.<CreateStockResponseDTO>builder()
                .data(data)
                .status(HttpStatus.CREATED.value())
                .build();
    }
}
