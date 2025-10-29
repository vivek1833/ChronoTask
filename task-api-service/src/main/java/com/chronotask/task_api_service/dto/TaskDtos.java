package com.chronotask.task_api_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.chronotask.task_api_service.enums.Priority;
import com.chronotask.task_api_service.enums.TaskStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskDtos {
        public record CreateTaskRequest(
                        @NotBlank String title,
                        String description,
                        @NotNull UUID creatorId,
                        @NotNull UUID assigneeId,
                        @NotNull @Future LocalDateTime dueDate,
                        @NotNull Priority priority) {
        }

        public record TaskResponse(
                        UUID id,
                        String title,
                        String description,
                        UUID creatorId,
                        UUID assigneeId,
                        LocalDateTime dueDate,
                        TaskStatus status,
                        Priority priority) {
        }
}
