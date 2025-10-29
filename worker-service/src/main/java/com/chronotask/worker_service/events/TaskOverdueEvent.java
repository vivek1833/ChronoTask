package com.chronotask.worker_service.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskOverdueEvent(
        UUID taskId,
        UUID creatorId,
        UUID assigneeId,
        LocalDateTime dueDate) {
}
