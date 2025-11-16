package com.example.reviewer_assignment_service.controller;

import com.example.reviewer_assignment_service.exceptions.ConflictException;
import com.example.reviewer_assignment_service.exceptions.NotFoundException;
import com.example.reviewer_assignment_service.model.dto.ErrorDto;
import com.example.reviewer_assignment_service.model.dto.pullRequest.*;
import com.example.reviewer_assignment_service.service.PullRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pullRequest")
@RequiredArgsConstructor
@Tag(name = "PullRequestController", description = "Управление Pull Request")
public class PullRequestController {
    private final PullRequestService pullRequestService;
    private final ServletContextInitializer servletContextInitializer;

    @Operation(
            summary = "Создание Pull Request",
            description = "После создания автоматически назначаются 1/2 ревьювера из команды автора.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pull Request создан успешно",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PRResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден, команда пуста",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Pull Request с таким названием у этого автора уже существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            )
    })
    @PostMapping("/create")
    public ResponseEntity<PRResponseDto> createPullRequest(@Valid @RequestBody PullRequestDto PullRequestDto) {
        PRResponseDto prResponseDto = pullRequestService.createPullRequest(PullRequestDto);
        return new ResponseEntity<>(prResponseDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Изменения статуса Pull Request",
            description = "Пометить Pull Request как MERGED. Нельзя пометить pr, который уже имеет статус MERGED.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pull Request в состоянии MERGED",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PRMergeResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Pull Request не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            )
    })
    @PostMapping("/merge")
    public ResponseEntity<PRMergeResponseDto> mergePullRequest(@Valid @RequestBody PullRequestIdDto pullRequestIdDto) {
        PRMergeResponseDto prMergeResponseDto = pullRequestService.mergePullRequest(pullRequestIdDto);
        return ResponseEntity.ok(prMergeResponseDto);
    }

    @Operation(
            summary = "Переназначение ревьювера",
            description = "Переназначить конкретного ревьювера на другого из его же команды (кроме автора).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Переназначение выполнено",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PRReassignedResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Pull Request не найден/Пользователь не найден/Команда пустая",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Pull Request уже в статусе MERGED/Пользователь не назначен на ревью этого Pull Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            )
    })
    @PostMapping("/reassign")
    public ResponseEntity<PRReassignedResponseDto> reassignPullRequestReviewer(@Valid @RequestBody PullRequestReassignDto pullRequestReassignDto) {
        PRReassignedResponseDto prReassignedResponseDto = pullRequestService.reassignPullRequest(pullRequestReassignDto);
        return ResponseEntity.ok(prReassignedResponseDto);
    }
}
