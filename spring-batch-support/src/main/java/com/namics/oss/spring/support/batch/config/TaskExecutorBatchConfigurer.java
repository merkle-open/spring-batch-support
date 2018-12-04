package com.namics.oss.spring.support.batch.config;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Inject;

/**
 * TaskExecutorBatchConfigurer.
 * Configuration is same as {@link org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer} with possibility to add TaskExecutor.
 * This is necessary to start jobs async, springs default would be a SyncThreadPool.
 *
 * @author lboesch, Namics AG
 * @since 29.08.17 08:01
 */
@Configuration
public class TaskExecutorBatchConfigurer extends DefaultBatchConfigurer {

    private PlatformTransactionManager transactionManager;

    @Inject
    public TaskExecutorBatchConfigurer(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    @Bean
    public ThreadPoolTaskScheduler batchTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.afterPropertiesSet();
        return threadPoolTaskScheduler;
    }

    @Override
    protected JobLauncher createJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(super.getJobRepository());
        jobLauncher.setTaskExecutor(batchTaskScheduler());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
