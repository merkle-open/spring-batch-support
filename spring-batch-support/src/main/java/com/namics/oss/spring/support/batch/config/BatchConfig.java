/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.config;

import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * BatchConfig.
 *
 * @author lboesch, Namics AG
 * @since 25.02.14
 */
@Configuration
@ComponentScan(
        basePackages = {
                "com.namics.oss.spring.support.batch",
        },
        includeFilters = {
                @ComponentScan.Filter(Component.class),
                @ComponentScan.Filter(Service.class),
                @ComponentScan.Filter(Repository.class),
        }
)
public class BatchConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.afterPropertiesSet();
        return threadPoolTaskScheduler;
    }

    @Bean
    public ZoneId fallbackZoneId() {
        return ZoneId.systemDefault();
    }
}
