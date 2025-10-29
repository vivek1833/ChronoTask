package com.chronotask.scheduler_service.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.chronotask.scheduler_service.events.TaskOverdueEvent;
import com.chronotask.scheduler_service.models.Task;
import com.chronotask.scheduler_service.models.enums.TaskStatus;
import com.chronotask.scheduler_service.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OverduePublisherService {
    private final TaskRepository taskRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.topics.task-overdue:task-overdue-topic}")
    private String topic;

    public int publishOverdueTasks() {
        List<Task> overdue = taskRepository.findByDueDateBeforeAndStatusNot(LocalDateTime.now(), TaskStatus.COMPLETED);
        overdue.forEach(t -> {
            TaskOverdueEvent event = new TaskOverdueEvent(
                    t.getId(),
                    t.getCreatedBy() != null ? t.getCreatedBy().getId() : null,
                    t.getAssignee() != null ? t.getAssignee().getId() : null,
                    t.getDueDate());
            try {
                var result = kafkaTemplate.send(topic, t.getId().toString(), event).get();
                org.slf4j.LoggerFactory.getLogger(OverduePublisherService.class)
                        .info("Sent to {}-{}@{}", result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            } catch (Exception e) {
                org.slf4j.LoggerFactory.getLogger(OverduePublisherService.class)
                        .error("Failed to send overdue event for task {}: {}", t.getId(), e.getMessage());
            }
        });
        return overdue.size();
    }
}
