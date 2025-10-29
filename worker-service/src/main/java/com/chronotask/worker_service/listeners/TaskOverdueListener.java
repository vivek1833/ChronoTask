package com.chronotask.worker_service.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chronotask.worker_service.events.TaskOverdueEvent;
import com.chronotask.worker_service.models.Task;
import com.chronotask.worker_service.models.enums.TaskStatus;
import com.chronotask.worker_service.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskOverdueListener {
    private static final Logger log = LoggerFactory.getLogger(TaskOverdueListener.class);

    private final TaskRepository taskRepository;
    private final JavaMailSender mailSender;

    @Value("${app.topics.task-overdue}")
    private String topic;

    @KafkaListener(topics = "task-overdue-topic", containerFactory = "taskOverdueKafkaListenerContainerFactory")
    @Transactional
    public void onMessage(TaskOverdueEvent event) {
        Task task = taskRepository.findById(event.taskId()).orElse(null);
        if (task == null) {
            log.warn("Task {} not found when processing overdue event", event.taskId());
            return;
        }

        task.setStatus(TaskStatus.OVERDUE);

        String subject = "Task Overdue: " + task.getTitle();
        String body = "Task is overdue since " + event.dueDate() + "\nTask ID: " + event.taskId();

        if (task.getCreatedBy() != null && task.getCreatedBy().getEmail() != null) {
            sendEmail(task.getCreatedBy().getEmail(), subject, body);
        }
        if (task.getAssignee() != null && task.getAssignee().getEmail() != null) {
            sendEmail(task.getAssignee().getEmail(), subject, body);
        }

        log.info("Processed overdue event for task {}", event.taskId());
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
