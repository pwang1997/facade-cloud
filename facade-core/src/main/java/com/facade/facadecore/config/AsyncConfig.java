package com.facade.facadecore.config;

import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/25/2024
 */

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("asyncSQLExecutor")
    public Executor doAsyncSQLExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutorBuilder()
                .corePoolSize(10)
                .maxPoolSize(20)
                .queueCapacity(500)
                .keepAlive(Duration.ofSeconds(30))
                .threadNamePrefix("async-sql-")
                .build();

        executor.setRejectedExecutionHandler(
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        executor.initialize();

        return executor;
    }


    @Bean("asyncAwsServiceExecutor")
    public Executor doAsyncAwsServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutorBuilder()
            .corePoolSize(10)
            .maxPoolSize(20)
            .queueCapacity(500)
            .keepAlive(Duration.ofSeconds(30))
            .threadNamePrefix("async-aws-service-")
            .build();

        executor.setRejectedExecutionHandler(
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        executor.initialize();

        return executor;
    }

    @Bean("asyncJobExecutor")
    public Executor doAsyncJobExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutorBuilder()
            .corePoolSize(10)
            .maxPoolSize(20)
            .queueCapacity(500)
            .keepAlive(Duration.ofSeconds(30))
            .threadNamePrefix("async-job-")
            .build();

        executor.setRejectedExecutionHandler(
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        executor.initialize();

        return executor;
    }
}
