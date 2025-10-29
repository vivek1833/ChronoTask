package com.chronotask.scheduler_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chronotask.scheduler_service.models.Task;
import com.chronotask.scheduler_service.models.enums.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByDueDateBeforeAndStatusNot(LocalDateTime cutoff, TaskStatus excluded);
}
