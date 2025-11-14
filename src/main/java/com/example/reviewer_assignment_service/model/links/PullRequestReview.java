package com.example.reviewer_assignment_service.model.links;

import com.example.reviewer_assignment_service.model.entity.PullRequest;
import com.example.reviewer_assignment_service.model.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "pull_request_review")
public class PullRequestReview {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pull_request_id", nullable = false)
    private PullRequest pullRequest;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User user;
}
