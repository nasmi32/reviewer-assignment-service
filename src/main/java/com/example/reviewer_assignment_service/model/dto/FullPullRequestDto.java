package com.example.reviewer_assignment_service.model.dto;


import com.example.reviewer_assignment_service.model.entity.Status;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FullPullRequestDto {
    private UUID pullRequestId;
    private String pullRequestName;
    private UUID authorId;
    private Status status;
}
