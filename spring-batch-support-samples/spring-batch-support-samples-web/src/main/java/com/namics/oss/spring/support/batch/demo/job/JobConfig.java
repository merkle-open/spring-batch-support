/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.demo.job;

import com.namics.oss.spring.support.batch.service.JobService;
import com.namics.oss.spring.support.batch.tasklet.SpringBatchDatabaseCleanUpTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.inject.Inject;

/**
 * JobConfig.
 *
 * @author lboesch, Namics AG
 * @since 20.06.2014
 */
@Configuration
public class JobConfig {

	@Inject
	protected JobBuilderFactory jobBuilders;
	@Inject
	protected StepBuilderFactory stepBuilders;
	@Inject
	protected JdbcTemplate jdbcTemplate;
	@Inject
	protected JobService jobService;

	@Scheduled(cron = "10 0 * * * ?")
	public void launchDatabaseCleanUpJob() {
		jobService.startJob(batchSpringBatchDatabaseCleanUp().getName());
	}

	@Bean
	public Job batchSpringBatchDatabaseCleanUp() {
		return jobBuilders.get("batchSpringBatchDatabaseCleanUp")
		                  .start(stepBuilders.get("batchSpringBatchDatabaseCleanUpStep")
		                                     .tasklet(batchSpringBatchDatabaseCleanUpTasklet())
		                                     .build())
		                  .build();
	}

	@Bean
	public SpringBatchDatabaseCleanUpTasklet batchSpringBatchDatabaseCleanUpTasklet() {
		return new SpringBatchDatabaseCleanUpTasklet(10, jdbcTemplate);
	}
}
