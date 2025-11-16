package com.example.reviewer_assignment_service.controller;

import com.example.reviewer_assignment_service.model.dto.ErrorDto;
import com.example.reviewer_assignment_service.model.dto.pullRequest.PRReassignedResponseDto;
import com.example.reviewer_assignment_service.model.dto.stats.StatsResponseDto;
import com.example.reviewer_assignment_service.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@Tag(name = "StatsController", description = "Статистика")
public class StatsController {
    private final StatsService statsService;

    @Operation(
            summary = "Вывод статистики",
            description = "Просмотр статистики для каждого пользователя: число созданных Pull Request, число Pull Request на ревью.")
    @ApiResponse(responseCode = "200", description = "Список выведен",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StatsResponseDto.class)))
    @GetMapping
    public ResponseEntity<StatsResponseDto> getStats() {
        StatsResponseDto statsResponseDto = statsService.getStats();
        return ResponseEntity.ok(statsResponseDto);
    }
}
