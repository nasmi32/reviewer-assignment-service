package com.example.reviewer_assignment_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "team_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "users")
    private ArrayList<User> users;
}
