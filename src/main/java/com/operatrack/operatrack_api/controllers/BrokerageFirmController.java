package com.operatrack.operatrack_api.controllers;

import com.operatrack.operatrack_api.controllers.dtos.CreateBrokerageFirmRequestDTO;
import com.operatrack.operatrack_api.controllers.dtos.CreateBrokerageFirmResponseDTO;
import com.operatrack.operatrack_api.controllers.dtos.GetBrokerageFirmResponseDTO;
import com.operatrack.operatrack_api.controllers.dtos.UpdateBrokerageFirmRequestDTO;
import com.operatrack.operatrack_api.controllers.responses.ApiResponse;
import com.operatrack.operatrack_api.controllers.responses.PageResponse;
import com.operatrack.operatrack_api.model.BrokerageFirm;
import com.operatrack.operatrack_api.services.BrokerageFirmService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brokerage-firms")
public class BrokerageFirmController {

    private final BrokerageFirmService brokerageFirmService;

    public BrokerageFirmController(BrokerageFirmService brokerageFirmService) {
        this.brokerageFirmService = brokerageFirmService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PageResponse<GetBrokerageFirmResponseDTO>> getBrokerageFirms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BrokerageFirm> brokerageFirmPage = brokerageFirmService.findAll(page, size);
        List<GetBrokerageFirmResponseDTO> content = brokerageFirmPage.getContent().stream()
                .map(b -> new GetBrokerageFirmResponseDTO(b.getId(), b.getInstitutionName(), b.getTaxRate()))
                .toList();
        PageResponse<GetBrokerageFirmResponseDTO> pageResponse = PageResponse.<GetBrokerageFirmResponseDTO>builder()
                .content(content)
                .page(brokerageFirmPage.getNumber())
                .size(brokerageFirmPage.getSize())
                .totalElements(brokerageFirmPage.getTotalElements())
                .totalPages(brokerageFirmPage.getTotalPages())
                .hasNext(brokerageFirmPage.hasNext())
                .hasPrevious(brokerageFirmPage.hasPrevious())
                .build();
        return ApiResponse.<PageResponse<GetBrokerageFirmResponseDTO>>builder()
                .data(pageResponse)
                .status(HttpStatus.OK.value())
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CreateBrokerageFirmResponseDTO> createBrokerageFirm(@RequestBody @Valid CreateBrokerageFirmRequestDTO request) {
        BrokerageFirm brokerageFirm = brokerageFirmService.create(request.institutionName(), request.taxRate());
        CreateBrokerageFirmResponseDTO data = new CreateBrokerageFirmResponseDTO(brokerageFirm.getId(), brokerageFirm.getInstitutionName(), brokerageFirm.getTaxRate());
        return ApiResponse.<CreateBrokerageFirmResponseDTO>builder()
                .data(data)
                .status(HttpStatus.CREATED.value())
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<GetBrokerageFirmResponseDTO> updateBrokerageFirm(
            @PathVariable String id,
            @RequestBody @Valid UpdateBrokerageFirmRequestDTO request) {
        BrokerageFirm brokerageFirm = brokerageFirmService.update(id, request.institutionName(), request.taxRate());
        GetBrokerageFirmResponseDTO data = new GetBrokerageFirmResponseDTO(brokerageFirm.getId(), brokerageFirm.getInstitutionName(), brokerageFirm.getTaxRate());
        return ApiResponse.<GetBrokerageFirmResponseDTO>builder()
                .data(data)
                .status(HttpStatus.OK.value())
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBrokerageFirm(@PathVariable String id) {
        brokerageFirmService.delete(id);
    }
}
