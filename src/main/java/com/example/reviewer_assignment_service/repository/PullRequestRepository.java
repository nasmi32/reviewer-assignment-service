package com.example.reviewer_assignment_service.repository;

import com.example.reviewer_assignment_service.model.entity.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PullRequestRepository extends JpaRepository<PullRequest, UUID> {
    boolean existsByAuthorIdAndTitle(UUID authorId, String title);

    @Query("""
        SELECT COUNT(pr)
        FROM PullRequest pr
        WHERE pr.author.id = :id
        """)
    long countPullRequestsByAuthorId(UUID id);
}
