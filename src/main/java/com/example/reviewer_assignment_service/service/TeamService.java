package com.example.reviewer_assignment_service.service;

import com.example.reviewer_assignment_service.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {
    private TeamRepository teamRepository;


}
