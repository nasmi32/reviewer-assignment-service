package com.example.reviewer_assignment_service.repository;

import com.example.reviewer_assignment_service.model.links.PullRequestReview;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PullRequestReviewRepository extends JpaRepository<PullRequestReview, UUID> {
    List<PullRequestReview> findByUserId(UUID userId);

    @Query("SELECT pr.user.id FROM PullRequestReview pr WHERE pr.pullRequest.id = :prId")
    List<UUID> findAllByPullRequestId(@Param("prId") UUID id);

    Optional<PullRequestReview> findByPullRequestIdAndUserId(UUID pullRequestId, UUID userId);

    @Query("""
        SELECT COUNT(prr)
        FROM PullRequestReview prr
        WHERE prr.user.id = :id
    """)
    long getReviewByReviewerId(UUID id);
}
