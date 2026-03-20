package com.operatrack.operatrack_api.controllers;

import com.operatrack.operatrack_api.controllers.dtos.CreateStockRequestDTO;
import com.operatrack.operatrack_api.controllers.dtos.CreateStockResponseDTO;
import com.operatrack.operatrack_api.controllers.dtos.GetStockResponseDTO;
import com.operatrack.operatrack_api.controllers.responses.ApiResponse;
import com.operatrack.operatrack_api.controllers.responses.PageResponse;
import com.operatrack.operatrack_api.model.Stock;
import com.operatrack.operatrack_api.services.StockService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PageResponse<GetStockResponseDTO>> getStocks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Stock> stockPage = stockService.findAll(page, size);
        List<GetStockResponseDTO> content = stockPage.getContent().stream()
                .map(s -> new GetStockResponseDTO(s.getId(), s.getName(), s.getTickerSymbol(), s.getCurrentPrice()))
                .toList();
        PageResponse<GetStockResponseDTO> pageResponse = PageResponse.<GetStockResponseDTO>builder()
                .content(content)
                .page(stockPage.getNumber())
                .size(stockPage.getSize())
                .totalElements(stockPage.getTotalElements())
                .totalPages(stockPage.getTotalPages())
                .hasNext(stockPage.hasNext())
                .hasPrevious(stockPage.hasPrevious())
                .build();
        return ApiResponse.<PageResponse<GetStockResponseDTO>>builder()
                .data(pageResponse)
                .status(HttpStatus.OK.value())
                .build();
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
