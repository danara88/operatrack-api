package com.operatrack.operatrack_api.controllers.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {

    private final T data;

    private final int status;

    private final boolean success = true;
}
