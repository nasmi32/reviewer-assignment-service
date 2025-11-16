package com.example.reviewer_assignment_service.service;

import com.example.reviewer_assignment_service.exceptions.ConflictException;
import com.example.reviewer_assignment_service.exceptions.NotFoundException;
import com.example.reviewer_assignment_service.model.dto.pullRequest.FullPullRequestDto;
import com.example.reviewer_assignment_service.model.dto.user.*;
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

        Team team = null;
        if (user.getTeam() != null) {
            team = teamRepository.findById(user.getTeam().getId()).orElseThrow(() -> new NotFoundException("Team not found"));
        }

        userResponseDto.setTeamName(team != null ? team.getName() : "");

        return userResponseDto;
    }

    public UserReviewsResponseDto getReviewsByUserId(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }

        UserReviewsResponseDto userReviewsResponseDto = new UserReviewsResponseDto();
        userReviewsResponseDto.setUserId(userId);

        List<PullRequestReview> pullRequestReviews = pullRequestReviewRepository.findByUserId(userId);

        List<PullRequest> pullRequests = pullRequestReviews.stream().map(PullRequestReview::getPullRequest).toList();

        List<FullPullRequestDto> fullPullRequestDtos = pullRequests.stream().map(pullRequest -> {
            FullPullRequestDto fullPullRequestDto = new FullPullRequestDto();
            fullPullRequestDto.setPullRequestId(pullRequest.getId());
            fullPullRequestDto.setPullRequestName(pullRequest.getTitle());
            fullPullRequestDto.setAuthorId(pullRequest.getAuthor().getId());
            fullPullRequestDto.setStatus(pullRequest.getStatus());
            return fullPullRequestDto;
        }).toList();

        userReviewsResponseDto.setPullRequests(fullPullRequestDtos);
        return userReviewsResponseDto;
    }

    public UsersResponseDto getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> usersDto = users.stream().map(user -> {
            UserResponseDto userDto = new UserResponseDto();
            userDto.setUserId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setActive(user.isActive());

            if (user.getTeam() != null) {
                userDto.setTeamName(user.getTeam().getName());
            }
            else {
                userDto.setTeamName("");
            }

            return userDto;
        }).toList();

        UsersResponseDto usersResponseDto = new UsersResponseDto();
        usersResponseDto.setUsers(usersDto);
        return usersResponseDto;
    }

    @Transactional
    public UserResponseDto addUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new ConflictException("User with username " + userDto.getUsername() + " already exists");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setActive(userDto.getIsActive());

        Team team = null;
        if (userDto.getTeamId() != null) {
            team = teamRepository.findById(userDto.getTeamId()).orElseThrow(() -> new NotFoundException("Team with id " + userDto.getTeamId() + " not found"));
            user.setTeam(team);
        }

        userRepository.save(user);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserId(user.getId());
        userResponseDto.setUsername(userDto.getUsername());
        userResponseDto.setActive(userDto.getIsActive());

        userResponseDto.setTeamName(team != null ? user.getTeam().getName() : null);
        return userResponseDto;
    }

    @Transactional
    public UserResponseDto connectUserToTeam(UserTeamConnectionDto dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException("User with id " + dto.getUserId() + " not found"));
        Team team = teamRepository.findById(dto.getTeamId()).orElseThrow(() -> new NotFoundException("Team with id " + dto.getTeamId() + " not found"));

        if (user.getTeam() != null && user.getTeam().getId().equals(team.getId())) {
            throw new ConflictException("User with id " + dto.getUserId() + " is already in team with id " + dto.getTeamId());
        }

        user.setTeam(team);
        userRepository.save(user);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserId(user.getId());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setActive(user.isActive());
        userResponseDto.setTeamName(user.getTeam().getName());
        return userResponseDto;
    }
}
