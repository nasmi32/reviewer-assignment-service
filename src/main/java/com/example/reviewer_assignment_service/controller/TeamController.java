package com.example.reviewer_assignment_service.controller;

import com.example.reviewer_assignment_service.model.dto.TeamRequestDto;
import com.example.reviewer_assignment_service.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

//    @PostMapping("/add")
//    public ResponseEntity<TeamResponseDto> addTeam(@Valid @RequestBody(required = true)TeamRequestDto) {
//
//    }
}
