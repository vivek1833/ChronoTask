package com.chronotask.worker_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chronotask.worker_service.models.Task;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
