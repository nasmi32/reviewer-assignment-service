package com.example.reviewer_assignment_service.service;

import com.example.reviewer_assignment_service.exceptions.ConflictException;
import com.example.reviewer_assignment_service.exceptions.NotFoundException;
import com.example.reviewer_assignment_service.model.dto.team.*;
import com.example.reviewer_assignment_service.model.entity.Team;
import com.example.reviewer_assignment_service.model.entity.User;
import com.example.reviewer_assignment_service.repository.TeamRepository;
import com.example.reviewer_assignment_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public TeamResponseDto addTeam(@Valid TeamDto teamDto) {
        if (teamRepository.existsByName(teamDto.getTeamName())) {
            throw new ConflictException("Team with name " + teamDto.getTeamName() + " already exists");
        }

        Team team = new Team();
        team.setName(teamDto.getTeamName());
        teamRepository.saveAndFlush(team);

//        TODO: удалять команды, которые остаются пустыми после перехода пользователей
        Set<User> users = teamDto.getMembers().stream().map(memberDto -> {
            User user = userRepository.findById(memberDto.getUserId()).orElseThrow(() -> new NotFoundException("User with id " + memberDto.getUserId() + " not found"));
            user.setUsername(memberDto.getUsername());
            user.setActive(memberDto.isActive());
            user.setTeam(team);
            return user;
        }).collect(Collectors.toSet());

        userRepository.saveAll(users);

        team.setUsers(users);

        TeamResponseDto teamResponseDto = new TeamResponseDto();
        teamResponseDto.setTeam(teamDto);
        return teamResponseDto;
    }

    public TeamDto getTeamByName(String teamName) {
        Team team = teamRepository.findByName(teamName).orElseThrow(() -> new NotFoundException("Team with name " + teamName + " not found"));

        TeamDto dto = new TeamDto();
        dto.setTeamName(teamName);
        List<MemberDto> members = getMembers(team);

        dto.setMembers(members);

        return dto;
    }

    private List<MemberDto> getMembers(Team team) {
        return team.getUsers().stream().map(user -> {
            MemberDto memberDto = new MemberDto();
            memberDto.setUserId(user.getId());
            memberDto.setUsername(user.getUsername());
            memberDto.setActive(user.isActive());

            return memberDto;
        }).toList();
    }

    public TeamsDto getAllTeams() {
        List<Team> teams = teamRepository.findAll();

        List<FullTeamDto> teamsDtos = teams.stream().map(team -> {
            FullTeamDto fullTeamDto = new FullTeamDto();
            fullTeamDto.setTeamId(team.getId());
            fullTeamDto.setTeamName(team.getName());

            List<MemberDto> members = getMembers(team);
            fullTeamDto.setMembers(members);

            return fullTeamDto;
        }).toList();

        TeamsDto teamsDto = new TeamsDto();
        teamsDto.setTeams(teamsDtos);
        return teamsDto;
    }
}
