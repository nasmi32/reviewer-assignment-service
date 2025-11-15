package com.example.reviewer_assignment_service.repository;

import com.example.reviewer_assignment_service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findAllByTeamIdAndIsActiveTrue(UUID teamId);
}
