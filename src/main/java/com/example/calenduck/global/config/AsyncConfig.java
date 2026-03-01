package com.example.calenduck.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean("batchTaskExecutor")
    public ThreadPoolTaskExecutor batchTaskExecutor(
            @Value("${app.thread-pool.batch.core-size}") int coreSize,
            @Value("${app.thread-pool.batch.max-size}") int maxSize,
            @Value("${app.thread-pool.batch.queue-capacity}") int queueCapacity) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreSize);
        executor.setMaxPoolSize(maxSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("batch-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }

    @Bean("bookmarkTaskExecutor")
    public ThreadPoolTaskExecutor bookmarkTaskExecutor(
            @Value("${app.thread-pool.bookmark.core-size}") int coreSize,
            @Value("${app.thread-pool.bookmark.max-size}") int maxSize,
            @Value("${app.thread-pool.bookmark.queue-capacity}") int queueCapacity) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreSize);
        executor.setMaxPoolSize(maxSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("bookmark-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }
}
