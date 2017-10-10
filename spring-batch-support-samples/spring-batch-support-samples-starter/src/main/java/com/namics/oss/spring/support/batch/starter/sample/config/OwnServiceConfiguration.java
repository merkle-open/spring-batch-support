package com.namics.oss.spring.support.batch.starter.sample.config;

import com.namics.oss.spring.support.batch.service.JobService;
import com.namics.oss.spring.support.batch.starter.sample.service.MyOwnJobService;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;

/**
 * OwnServiceConfiguration.
 * if you uncomment this @Configuration, you register your own JobService to use and can override for example the start of a batch job.
 * In this example, an log statement on error level is written on starting an app.
 *
 * @author lboesch, Namics AG
 * @since 10.10.17 14:48
 */

//@Configuration
public class OwnServiceConfiguration {

	@Bean
	public JobService jobService(JobOperator batchJobOperator, JobRegistry batchJobRegistry, JobExplorer jobExplorer, JobLauncher jobLauncher, JobRepository jobRepository) throws Exception {
		return new MyOwnJobService(jobExplorer, batchJobOperator, jobLauncher, batchJobRegistry, jobRepository);
	}

}
