package com.example.reviewer_assignment_service.controller;

import com.example.reviewer_assignment_service.model.dto.ErrorDto;
import com.example.reviewer_assignment_service.model.dto.team.TeamDto;
import com.example.reviewer_assignment_service.model.dto.team.TeamResponseDto;
import com.example.reviewer_assignment_service.model.dto.user.*;
import com.example.reviewer_assignment_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "Управление пользователями")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Установить флаг активности пользователя",
            description = "Установить флаг isActive в значение true/false")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Флаг установлен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден/Команда не найдена",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            )
    })
    @PostMapping("/setIsActive")
    public ResponseEntity<UserResponseDto> setIsActive(@Valid @RequestBody ActiveDto activeDto) {
        UserResponseDto userDto = userService.setIsActive(activeDto);
        return ResponseEntity.ok(userDto);
    }

    @Operation(
            summary = "Pull Request, где пользователь назначен ревьювером",
            description = "Получить все Pull Request, где пользователь назначен ревьювером")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список получен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserReviewsResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            )
    })
    @GetMapping("/getReview")
    public ResponseEntity<UserReviewsResponseDto> getReview(@RequestParam(name = "user_id") UUID userId) {
        UserReviewsResponseDto userReviewsResponseDto = userService.getReviewsByUserId(userId);
        return ResponseEntity.ok(userReviewsResponseDto);
    }

    @Operation(
            summary = "Получить пользователей",
            description = "Получить список всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список получен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsersResponseDto.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<UsersResponseDto> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(
            summary = "Cоздать пользователя",
            description = "Создать пользователя (команду указывать не обязательно).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь создан",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Команда с таким названием не существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким именем уже существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            )
    })
    @PostMapping("/add")
    public ResponseEntity<UserResponseDto> addUser(@RequestBody UserDto userDto) {
        UserResponseDto userResponseDto = userService.addUser(userDto);
        return ResponseEntity.created(URI.create("user" + userResponseDto.getUserId()))
                .body(userResponseDto);
    }

    @Operation(
            summary = "Добавить пользователя в команду",
            description = "Добавить пользователя в команду.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь добавлен в команду",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Пользователь/Команда с таким id не существует",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Пользователь уже в команде",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            )
    })
    @PostMapping("/connect")
    public ResponseEntity<UserResponseDto> connectUserToTeam(@RequestBody UserTeamConnectionDto dto) {
        UserResponseDto userResponseDto = userService.connectUserToTeam(dto);
        return ResponseEntity.ok(userResponseDto);
    }
}
