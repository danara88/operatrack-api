package com.operatrack.operatrack_api.controllers.responses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponse<T> {

    private final List<T> content;

    private final int page;

    private final int size;

    private final long totalElements;

    private final int totalPages;

    private final boolean hasNext;

    private final boolean hasPrevious;
}
