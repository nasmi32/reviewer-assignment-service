package com.example.reviewer_assignment_service.controller;

import com.example.reviewer_assignment_service.model.dto.ActiveDto;
import com.example.reviewer_assignment_service.model.dto.UserResponseDto;
import com.example.reviewer_assignment_service.model.dto.UserReviewsResponseDto;
import com.example.reviewer_assignment_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/setIsActive")
    public ResponseEntity<UserResponseDto> setIsActive(@Valid @RequestBody ActiveDto activeDto) {
        UserResponseDto userDto = userService.setIsActive(activeDto);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/getReview")
    public ResponseEntity<UserReviewsResponseDto> getReview(@RequestParam(name = "user_id") UUID userId) {
        UserReviewsResponseDto userReviewsResponseDto = userService.getReviewsByUserId(userId);
        return ResponseEntity.ok(userReviewsResponseDto);
    }
}
