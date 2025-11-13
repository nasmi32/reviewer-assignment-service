package com.example.reviewer_assignment_service.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "pull_request")
public class PullRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pull_request_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    @Size(min = 1, max = 255)
    @NotBlank
    private String title;

    @Column(name = "author_id", nullable = false)
    @NotBlank
    private long authorId;

    @Column(name = "status", nullable = false)
    @NotBlank
    private Status status;

    @Column(name = "reviewer_ids")
    @Size(max=2)
    private ArrayList<Long> reviewerIds;
}
