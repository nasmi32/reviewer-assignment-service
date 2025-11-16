package com.example.reviewer_assignment_service.controller;

import com.example.reviewer_assignment_service.model.dto.StatsResponseDto;
import com.example.reviewer_assignment_service.model.dto.UserStats;
import com.example.reviewer_assignment_service.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<StatsResponseDto> getStats() {
        StatsResponseDto statsResponseDto = statsService.getStats();
        return ResponseEntity.ok(statsResponseDto);
    }
}
