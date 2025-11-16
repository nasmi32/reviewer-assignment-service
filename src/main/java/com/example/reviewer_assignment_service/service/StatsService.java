package com.example.reviewer_assignment_service.service;

import com.example.reviewer_assignment_service.model.dto.stats.StatsResponseDto;
import com.example.reviewer_assignment_service.model.dto.stats.UserStats;
import com.example.reviewer_assignment_service.model.entity.User;
import com.example.reviewer_assignment_service.repository.PullRequestRepository;
import com.example.reviewer_assignment_service.repository.PullRequestReviewRepository;
import com.example.reviewer_assignment_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final UserRepository userRepository;
    private final PullRequestRepository pullRequestRepository;
    private final PullRequestReviewRepository pullRequestReviewRepository;

    public StatsResponseDto getStats() {
        List<User> users = userRepository.findAll();

        List<UserStats> userStatsList = users.stream().map(user -> {
            UserStats userStats = new UserStats();
            userStats.setUserId(user.getId());
            userStats.setUsername(user.getUsername());

            long pullRequestsCount = pullRequestRepository.countPullRequestsByAuthorId(user.getId());
            userStats.setPullRequestsCount(pullRequestsCount);

            long reviewsCount = pullRequestReviewRepository.getReviewByReviewerId(user.getId());
            userStats.setReviewsCount(reviewsCount);

            return userStats;
        }).toList();

        StatsResponseDto statsResponseDto = new StatsResponseDto();
        statsResponseDto.setUserStats(userStatsList);

        return statsResponseDto;
    }

}
