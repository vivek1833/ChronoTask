package com.chronotask.scheduler_service.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chronotask.scheduler_service.services.OverduePublisherService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DailyOverdueJob {
    private static final Logger log = LoggerFactory.getLogger(DailyOverdueJob.class);

    private final OverduePublisherService overduePublisherService;

    // Run at 9:30 AM daily (server time)
    @Scheduled(cron = "0 * * * * *")
    public void run() {
        int count = overduePublisherService.publishOverdueTasks();
        log.info("Published {} overdue task events", count);
    }
}
