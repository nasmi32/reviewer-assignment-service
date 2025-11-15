package com.example.reviewer_assignment_service.model.dto;

import com.example.reviewer_assignment_service.model.entity.Status;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PullRequestMergeResponseDto {
    private UUID pullRequestId;
    private String pullRequestName;
    private UUID authorId;
    private Status status;
    private List<UUID> assignedReviewers;
    private OffsetDateTime mergedAt;
}
