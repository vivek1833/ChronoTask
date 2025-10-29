package com.chronotask.task_api_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chronotask.task_api_service.enums.TaskStatus;
import com.chronotask.task_api_service.models.Task;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByDueDateBeforeAndStatusNot(LocalDateTime cutoff, TaskStatus excluded);
}
