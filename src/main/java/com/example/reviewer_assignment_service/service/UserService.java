package com.example.reviewer_assignment_service.service;

import com.example.reviewer_assignment_service.exceptions.NotFoundException;
import com.example.reviewer_assignment_service.model.dto.user.ActiveDto;
import com.example.reviewer_assignment_service.model.dto.pullRequest.FullPullRequestDto;
import com.example.reviewer_assignment_service.model.dto.user.UserResponseDto;
import com.example.reviewer_assignment_service.model.dto.user.UserReviewsResponseDto;
import com.example.reviewer_assignment_service.model.dto.user.UsersResponseDto;
import com.example.reviewer_assignment_service.model.entity.PullRequest;
import com.example.reviewer_assignment_service.model.entity.Team;
import com.example.reviewer_assignment_service.model.entity.User;
import com.example.reviewer_assignment_service.model.links.PullRequestReview;
import com.example.reviewer_assignment_service.repository.PullRequestReviewRepository;
import com.example.reviewer_assignment_service.repository.TeamRepository;
import com.example.reviewer_assignment_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PullRequestReviewRepository pullRequestReviewRepository;

    @Transactional
    public UserResponseDto setIsActive(@Valid ActiveDto activeDto) {
        User user = userRepository.findById(activeDto.getUserId()).orElseThrow(() -> new NotFoundException("User with id " + activeDto.getUserId() + " not found"));

        user.setActive(activeDto.getIsActive());

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserId(user.getId());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setActive(user.isActive());

        userRepository.saveAndFlush(user);

        Team team = teamRepository.findById(user.getTeam().getId()).orElseThrow(() -> new NotFoundException("Team not found"));
        userResponseDto.setTeamName(team.getName());

        return userResponseDto;
    }

    public UserReviewsResponseDto getReviewsByUserId(UUID userId) {
        UserReviewsResponseDto userReviewsResponseDto = new UserReviewsResponseDto();
        userReviewsResponseDto.setUserId(userId);

        List<PullRequestReview> pullRequestReviews = pullRequestReviewRepository.findByUserId(userId);

        List<PullRequest> pullRequests = pullRequestReviews.stream()
                .map(PullRequestReview::getPullRequest)
                .toList();

        List<FullPullRequestDto> fullPullRequestDtos = pullRequests.stream()
                .map(pullRequest -> {
                    FullPullRequestDto fullPullRequestDto = new FullPullRequestDto();
                    fullPullRequestDto.setPullRequestId(pullRequest.getId());
                    fullPullRequestDto.setPullRequestName(pullRequest.getTitle());
                    fullPullRequestDto.setAuthorId(pullRequest.getAuthor().getId());
                    fullPullRequestDto.setStatus(pullRequest.getStatus());
                    return fullPullRequestDto;
                })
                .toList();

        userReviewsResponseDto.setPullRequests(fullPullRequestDtos);
        return userReviewsResponseDto;
    }

    public UsersResponseDto getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> usersDto = users.stream()
                .map(user -> {
                    UserResponseDto userDto = new UserResponseDto();
                    userDto.setUserId(user.getId());
                    userDto.setUsername(user.getUsername());
                    userDto.setActive(user.isActive());
                    userDto.setTeamName(user.getTeam().getName());
                    return userDto;
                })
                .toList();

        UsersResponseDto usersResponseDto = new UsersResponseDto();
        usersResponseDto.setUsers(usersDto);
        return usersResponseDto;
    }
}
