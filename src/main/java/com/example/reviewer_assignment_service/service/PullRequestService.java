package com.example.reviewer_assignment_service.service;

import com.example.reviewer_assignment_service.exceptions.ConflictException;
import com.example.reviewer_assignment_service.exceptions.NotFoundException;
import com.example.reviewer_assignment_service.model.dto.pullRequest.*;
import com.example.reviewer_assignment_service.model.entity.PullRequest;
import com.example.reviewer_assignment_service.model.entity.Status;
import com.example.reviewer_assignment_service.model.entity.User;
import com.example.reviewer_assignment_service.model.links.PullRequestReview;
import com.example.reviewer_assignment_service.repository.PullRequestRepository;
import com.example.reviewer_assignment_service.repository.PullRequestReviewRepository;
import com.example.reviewer_assignment_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PullRequestService {
    private final UserRepository userRepository;
    private final PullRequestRepository pullRequestRepository;
    private final PullRequestReviewRepository pullRequestReviewRepository;

    @Transactional
    public PRResponseDto createPullRequest(@Valid PullRequestDto pullRequestDto) {
        PullRequest pullRequest = new PullRequest();
        pullRequest.setTitle(pullRequestDto.getPullRequestName());
        pullRequest.setStatus(Status.OPEN);

        User author = userRepository.findById(pullRequestDto.getAuthorId()).orElseThrow(
                () -> new NotFoundException("User with id " + pullRequestDto.getAuthorId() + " not found"));
        pullRequest.setAuthor(author);

        if (pullRequestRepository.existsByAuthorIdAndTitle(author.getId(), pullRequestDto.getPullRequestName())) {
            throw new ConflictException("Pull Request with name '" + pullRequestDto.getPullRequestName()
                    + "' by author with id " + author.getId() + " already exists");
        }

        List<User> teammates = userRepository.findAllByTeamIdAndIsActiveTrue(author.getTeam().getId());
        teammates.remove(author);

        if (teammates.isEmpty()) {
            throw new NotFoundException("There are no teammates or active teammates for user with id " + author.getId());
        }

        pullRequestRepository.save(pullRequest);

        int reviewersCount = Math.min(teammates.size(), 2);

        Collections.shuffle(teammates);
        List<UUID> assignedReviewers = new ArrayList<>();
        List<PullRequestReview> reviews = new ArrayList<>();
        for (int i = 0; i < reviewersCount; i++) {
            User reviewer = teammates.get(i);

            PullRequestReview pullRequestReview = new PullRequestReview();
            pullRequestReview.setPullRequest(pullRequest);
            pullRequestReview.setUser(reviewer);

            reviews.add(pullRequestReview);
            assignedReviewers.add(reviewer.getId());
        }
        pullRequestReviewRepository.saveAll(reviews);

        PullRequestResponseDto pullRequestResponseDto = new PullRequestResponseDto();
        pullRequestResponseDto.setPullRequestId(pullRequest.getId());
        pullRequestResponseDto.setPullRequestName(pullRequestDto.getPullRequestName());
        pullRequestResponseDto.setAuthorId(pullRequestDto.getAuthorId());
        pullRequestResponseDto.setStatus(pullRequest.getStatus());
        pullRequestResponseDto.setAssignedReviewers(assignedReviewers);

        PRResponseDto prResponseDto = new PRResponseDto();
        prResponseDto.setPr(pullRequestResponseDto);
        return prResponseDto;
    }

    @Transactional
    public PRMergeResponseDto mergePullRequest(@Valid PullRequestIdDto pullRequestIdDto) {
        PullRequest pullRequest = pullRequestRepository.findById(pullRequestIdDto.getPullRequestId()).orElseThrow(
                () -> new NotFoundException("Pull Request with id " + pullRequestIdDto.getPullRequestId() + " not found"));

        if (pullRequest.getStatus() != Status.MERGED) {
            pullRequest.setStatus(Status.MERGED);
            pullRequest.setMergedAt(OffsetDateTime.now());
            pullRequestRepository.save(pullRequest);
        }

        PRMergeResponseDto prResponseDto = new PRMergeResponseDto();
        PullRequestMergeResponseDto mergeResponseDto = new PullRequestMergeResponseDto();
        mergeResponseDto.setPullRequestId(pullRequest.getId());
        mergeResponseDto.setPullRequestName(pullRequest.getTitle());
        mergeResponseDto.setAuthorId(pullRequest.getAuthor().getId());
        mergeResponseDto.setStatus(pullRequest.getStatus());

        List<UUID> reviewers = pullRequestReviewRepository.findAllByPullRequestId(pullRequest.getId());
        mergeResponseDto.setAssignedReviewers(reviewers);

        mergeResponseDto.setMergedAt(pullRequest.getMergedAt());

        prResponseDto.setPr(mergeResponseDto);
        return prResponseDto;
    }

    @Transactional
    public PRReassignedResponseDto reassignPullRequest(@Valid PullRequestReassignDto pullRequestReassignDto) {
        PullRequest pullRequest = pullRequestRepository.findById(pullRequestReassignDto.getPullRequestId()).orElseThrow(
                () -> new NotFoundException("Pull Request with id " + pullRequestReassignDto.getPullRequestId() + " not found"));

        if (pullRequest.getStatus() == Status.MERGED) {
            throw new ConflictException("Pull Request with id " + pullRequest.getId() + " is already merged");
        }

        List<UUID> assignedReviewers = pullRequestReviewRepository.findAllByPullRequestId(pullRequest.getId());
        if (!assignedReviewers.contains(pullRequestReassignDto.getOldReviewerId())) {
            throw new ConflictException("User with id " + pullRequestReassignDto.getOldReviewerId() + " is not assigned");
        }

        User oldReviewer = userRepository.findById(pullRequestReassignDto.getOldReviewerId()).orElseThrow(
                () -> new NotFoundException("User with id " + pullRequestReassignDto.getOldReviewerId() + " not found"));
        List<User> teammates = userRepository.findAllByTeamIdAndIsActiveTrue(oldReviewer.getTeam().getId());
        teammates.remove(oldReviewer);

        User author = pullRequest.getAuthor();
        teammates.remove(author);

        if (!assignedReviewers.isEmpty()) {
            List<User> alreadyAssigned = userRepository.findAllById(assignedReviewers);
            teammates.removeAll(alreadyAssigned);
        }

        if (teammates.isEmpty()) {
            throw new NotFoundException("There are no active teammates for reviewer with id " + oldReviewer.getId());
        }

        Collections.shuffle(teammates);
        User newReviewer = teammates.getFirst();

        PullRequestReview pullRequestReview = pullRequestReviewRepository.findByPullRequestIdAndUserId(pullRequest.getId(),
                pullRequestReassignDto.getOldReviewerId()).orElseThrow(() -> new NotFoundException(
                "Review for Pull Request " + pullRequestReassignDto.getPullRequestId() +
                        " and reviewer " + pullRequestReassignDto.getOldReviewerId() + " not found"
        ));

        pullRequestReview.setUser(newReviewer);
        pullRequestReviewRepository.save(pullRequestReview);

        assignedReviewers.remove(pullRequestReassignDto.getOldReviewerId());
        assignedReviewers.add(newReviewer.getId());

        PullRequestReassignResponseDto pullRequestReassignResponseDto = getPullRequestReassignResponseDto(pullRequest,
                assignedReviewers);

        PRReassignedResponseDto prResponseDto = new PRReassignedResponseDto();
        prResponseDto.setPr(pullRequestReassignResponseDto);
        prResponseDto.setReplacedBy(teammates.getFirst().getId());
        return prResponseDto;
    }

    private static PullRequestReassignResponseDto getPullRequestReassignResponseDto(PullRequest pullRequest,
                                                                                    List<UUID> assignedReviewers) {
        PullRequestReassignResponseDto pullRequestReassignResponseDto = new PullRequestReassignResponseDto();
        pullRequestReassignResponseDto.setPullRequestId(pullRequest.getId());
        pullRequestReassignResponseDto.setPullRequestName(pullRequest.getTitle());
        pullRequestReassignResponseDto.setAuthorId(pullRequest.getAuthor().getId());
        pullRequestReassignResponseDto.setStatus(pullRequest.getStatus());
        pullRequestReassignResponseDto.setAssignedReviewers(assignedReviewers);
        return pullRequestReassignResponseDto;
    }
}
