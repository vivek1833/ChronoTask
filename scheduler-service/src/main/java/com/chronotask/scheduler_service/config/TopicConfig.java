package com.chronotask.scheduler_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfig {

    @Value("${app.topics.task-overdue:task-overdue-topic}")
    private String taskOverdueTopic;

    @Bean
    public NewTopic taskOverdueTopic() {
        return TopicBuilder.name(taskOverdueTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
