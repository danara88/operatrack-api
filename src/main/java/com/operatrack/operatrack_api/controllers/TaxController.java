package com.operatrack.operatrack_api.controllers;

import com.operatrack.operatrack_api.controllers.dtos.CreateTaxRequestDTO;
import com.operatrack.operatrack_api.controllers.dtos.CreateTaxResponseDTO;
import com.operatrack.operatrack_api.controllers.dtos.GetTaxResponseDTO;
import com.operatrack.operatrack_api.controllers.dtos.UpdateTaxRequestDTO;
import com.operatrack.operatrack_api.controllers.responses.ApiResponse;
import com.operatrack.operatrack_api.controllers.responses.PageResponse;
import com.operatrack.operatrack_api.model.Tax;
import com.operatrack.operatrack_api.services.TaxService;
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
@RequestMapping("/api/v1/taxes")
public class TaxController {

    private final TaxService taxService;

    public TaxController(TaxService taxService) {
        this.taxService = taxService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PageResponse<GetTaxResponseDTO>> getTaxes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Tax> taxPage = taxService.findAll(page, size);
        List<GetTaxResponseDTO> content = taxPage.getContent().stream()
                .map(t -> new GetTaxResponseDTO(t.getId(), t.getInstitutionName(), t.getTaxRate()))
                .toList();
        PageResponse<GetTaxResponseDTO> pageResponse = PageResponse.<GetTaxResponseDTO>builder()
                .content(content)
                .page(taxPage.getNumber())
                .size(taxPage.getSize())
                .totalElements(taxPage.getTotalElements())
                .totalPages(taxPage.getTotalPages())
                .hasNext(taxPage.hasNext())
                .hasPrevious(taxPage.hasPrevious())
                .build();
        return ApiResponse.<PageResponse<GetTaxResponseDTO>>builder()
                .data(pageResponse)
                .status(HttpStatus.OK.value())
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CreateTaxResponseDTO> createTax(@RequestBody @Valid CreateTaxRequestDTO request) {
        Tax tax = taxService.create(request.institutionName(), request.taxRate());
        CreateTaxResponseDTO data = new CreateTaxResponseDTO(tax.getId(), tax.getInstitutionName(), tax.getTaxRate());
        return ApiResponse.<CreateTaxResponseDTO>builder()
                .data(data)
                .status(HttpStatus.CREATED.value())
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<GetTaxResponseDTO> updateTax(
            @PathVariable String id,
            @RequestBody @Valid UpdateTaxRequestDTO request) {
        Tax tax = taxService.update(id, request.institutionName(), request.taxRate());
        GetTaxResponseDTO data = new GetTaxResponseDTO(tax.getId(), tax.getInstitutionName(), tax.getTaxRate());
        return ApiResponse.<GetTaxResponseDTO>builder()
                .data(data)
                .status(HttpStatus.OK.value())
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTax(@PathVariable String id) {
        taxService.delete(id);
    }
}
