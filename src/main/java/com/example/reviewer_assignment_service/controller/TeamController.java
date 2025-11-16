package com.example.reviewer_assignment_service.controller;

import com.example.reviewer_assignment_service.model.dto.ErrorDto;
import com.example.reviewer_assignment_service.model.dto.team.TeamDto;
import com.example.reviewer_assignment_service.model.dto.team.TeamResponseDto;
import com.example.reviewer_assignment_service.model.dto.team.TeamsDto;
import com.example.reviewer_assignment_service.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
@Tag(name = "TeamController", description = "Управление командами")
public class TeamController {
    private final TeamService teamService;

    @Operation(
            summary = "Создание команды",
            description = "Создать команду из указанных пользователей.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Команда создана",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TeamResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Команда с таким названием уже существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            )
    })
    @PostMapping("/add")
    public ResponseEntity<TeamResponseDto> addTeam(@Valid @RequestBody TeamDto teamRequestDto) {
        TeamResponseDto teamResponseDto = teamService.addTeam(teamRequestDto);
        return ResponseEntity.created(URI.create("team" + teamResponseDto.getTeam().getTeamName()))
                .body(teamResponseDto);
    }

    @Operation(
            summary = "Список участников команды",
            description = "Просмотреть состав команды.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список участников получен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TeamDto.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Команда с таким названием не существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            )
    })
    @GetMapping("/get")
    public ResponseEntity<TeamDto> getTeam(@RequestParam(name = "team_name") @Parameter(description = "Название команды", example = "Backend Avengers") String teamName) {
        TeamDto teamDto = teamService.getTeamByName(teamName);
        return ResponseEntity.ok(teamDto);
    }

    @Operation(
            summary = "Список всех команд",
            description = "Просмотреть все команды.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список команд получен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TeamsDto.class)
                    )
            )
    })
    @GetMapping("/getAll")
    public ResponseEntity<TeamsDto> getAllTeams() {
        TeamsDto teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

}
