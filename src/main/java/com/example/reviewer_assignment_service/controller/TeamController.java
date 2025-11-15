package com.example.reviewer_assignment_service.controller;

import com.example.reviewer_assignment_service.model.dto.team.TeamDto;
import com.example.reviewer_assignment_service.model.dto.team.TeamResponseDto;
import com.example.reviewer_assignment_service.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping("/add")
    public ResponseEntity<TeamResponseDto> addTeam(@Valid @RequestBody TeamDto teamRequestDto) {
        TeamResponseDto teamResponseDto = teamService.addTeam(teamRequestDto);
        return ResponseEntity.created(URI.create("team" + teamResponseDto.getTeam().getTeamName()))
                .body(teamResponseDto);
    }

    @GetMapping("/get")
    public ResponseEntity<TeamDto> getTeam(@RequestParam(name = "team_name") String teamName) {
        TeamDto teamDto = teamService.getTeamByName(teamName);
        return ResponseEntity.ok(teamDto);
    }
}
