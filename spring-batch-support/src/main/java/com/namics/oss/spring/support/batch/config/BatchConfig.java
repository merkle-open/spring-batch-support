/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.config;

import com.namics.oss.spring.support.batch.service.JobService;
import com.namics.oss.spring.support.batch.service.impl.JobServiceImpl;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * BatchConfig.
 *
 * @author lboesch, Namics AG
 * @since 25.02.14
 */
@Configuration
@Import(TaskExecutorBatchConfigurer.class)
@EnableBatchProcessing(modular = true)
public class BatchConfig {

	@Inject
	protected PlatformTransactionManager transactionManager;

	@Bean
	public JobRepository batchSimpleJobRepository(DataSource dataSource) throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(dataSource);
		factory.setTransactionManager(transactionManager);
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@Bean
	public JobOperator jobOperator(JobExplorer jobExplorer, JobLauncher jobLauncher, ListableJobLocator jobRegistry, JobRepository jobRepository) throws Exception {
		SimpleJobOperator jobOperator = new SimpleJobOperator();
		jobOperator.setJobExplorer(jobExplorer);
		jobOperator.setJobLauncher(jobLauncher);
		jobOperator.setJobRegistry(jobRegistry);
		jobOperator.setJobRepository(jobRepository);
		return jobOperator;
	}

	@Bean
	public JobService jobService(JobOperator batchJobOperator,
	                             JobRegistry batchJobRegistry,
	                             JobExplorer jobExplorer,
	                             JobLauncher jobLauncher,
	                             JobRepository jobRepository) throws Exception {
		return new JobServiceImpl(jobExplorer, batchJobOperator, jobLauncher, batchJobRegistry, jobRepository);
	}


}
