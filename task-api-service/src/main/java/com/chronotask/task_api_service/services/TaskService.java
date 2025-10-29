package com.chronotask.task_api_service.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chronotask.task_api_service.dto.TaskDtos.CreateTaskRequest;
import com.chronotask.task_api_service.dto.TaskDtos.TaskResponse;
import com.chronotask.task_api_service.enums.TaskStatus;
import com.chronotask.task_api_service.exception.ResourceNotFoundException;
import com.chronotask.task_api_service.models.Task;
import com.chronotask.task_api_service.models.User;
import com.chronotask.task_api_service.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    @Transactional
    public TaskResponse createTask(CreateTaskRequest request) {
        User creator = userService.getOrThrow(request.creatorId());
        User assignee = userService.getOrThrow(request.assigneeId());

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .createdBy(creator)
                .assignee(assignee)
                .dueDate(request.dueDate())
                .priority(request.priority())
                .status(TaskStatus.PENDING)
                .build();
        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TaskResponse getTask(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
        return toResponse(task);
    }

    @Transactional
    public TaskResponse markCompleted(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
        task.setStatus(TaskStatus.COMPLETED);
        return toResponse(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getPendingOrOverdue() {
        List<Task> tasks = taskRepository.findByDueDateBeforeAndStatusNot(LocalDateTime.now(), TaskStatus.COMPLETED);
        return tasks.stream().map(this::toResponse).toList();
    }

    private TaskResponse toResponse(Task t) {
        UUID creatorId = t.getCreatedBy() != null ? t.getCreatedBy().getId() : null;
        UUID assigneeId = t.getAssignee() != null ? t.getAssignee().getId() : null;
        return new TaskResponse(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                creatorId,
                assigneeId,
                t.getDueDate(),
                t.getStatus(),
                t.getPriority());
    }
}
