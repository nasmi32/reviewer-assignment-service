package com.example.reviewer_assignment_service.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
    @Schema(description = "Время ошибки", example = "2025-11-16T14:39:57.043620839Z", accessMode = Schema.AccessMode.READ_ONLY)
    private String timestamp;
    @Schema(description = "HTTP статус ошибки", example = "ERROR_CODE", accessMode = Schema.AccessMode.READ_ONLY)
    private int status;
    @Schema(description = "Название HTTP статуса", example = "ERROR_STATUS", accessMode = Schema.AccessMode.READ_ONLY)
    private String error;
    @Schema(description = "Описание ошибки", example = "Error description", accessMode = Schema.AccessMode.READ_ONLY)
    private String message;
    @Schema(description = "Путь запроса", example = "http://localhost:8080/users", accessMode = Schema.AccessMode.READ_ONLY)
    private String path;
}