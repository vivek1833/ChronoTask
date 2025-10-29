package com.chronotask.worker_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chronotask.worker_service.models.User;

public interface UserRepository extends JpaRepository<User, UUID> {
}
