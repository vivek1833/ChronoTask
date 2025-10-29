package com.chronotask.scheduler_service.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskOverdueEvent(
        UUID taskId,
        UUID creatorId,
        UUID assigneeId,
        LocalDateTime dueDate) {
}
