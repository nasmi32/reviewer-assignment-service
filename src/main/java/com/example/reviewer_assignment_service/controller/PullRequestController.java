package com.example.reviewer_assignment_service.controller;

import com.example.reviewer_assignment_service.model.dto.*;
import com.example.reviewer_assignment_service.service.PullRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pullRequest")
@RequiredArgsConstructor
public class PullRequestController {
    private final PullRequestService pullRequestService;

    @PostMapping("/create")
    public ResponseEntity<PRResponseDto> createPullRequest(@Valid @RequestBody PullRequestDto PullRequestDto) {
        PRResponseDto prResponseDto = pullRequestService.createPullRequest(PullRequestDto);
        return new ResponseEntity<>(prResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/merge")
    public ResponseEntity<PRMergeResponseDto> mergePullRequest(@Valid @RequestBody PullRequestIdDto pullRequestIdDto) {
        PRMergeResponseDto prMergeResponseDto = pullRequestService.mergePullRequest(pullRequestIdDto);
        return ResponseEntity.ok(prMergeResponseDto);
    }

    @PostMapping("/reassign")
    public ResponseEntity<PRReassignedResponseDto> reassignPullRequestReviewer(@Valid @RequestBody PullRequestReassignDto pullRequestReassignDto) {
        PRReassignedResponseDto prReassignedResponseDto = pullRequestService.reassignPullRequest(pullRequestReassignDto);
        return ResponseEntity.ok(prReassignedResponseDto);
    }
}
