/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.starter.sample.config;

import com.namics.oss.spring.support.batch.starter.sample.tasklet.LongRunningTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

/**
 * LongRunningJobConfig.
 *
 * @author lboesch, Namics AG
 * @since 20.06.2017
 */
@Configuration
public class LongRunningJobConfig {

	@Inject
	protected JobBuilderFactory jobBuilders;
	@Inject
	protected StepBuilderFactory stepBuilders;


	@Bean
	public Job veryLongRunningJob() {
		return jobBuilders.get("veryLongRunningJob")
		                  .start(stepBuilders.get("veryLongRunningJobStep1")
		                                     .tasklet(longRunningTasklet())
		                                     .build())
		                  .build();
	}

	@Bean
	public LongRunningTasklet longRunningTasklet() {
		return new LongRunningTasklet(5);
	}

}
