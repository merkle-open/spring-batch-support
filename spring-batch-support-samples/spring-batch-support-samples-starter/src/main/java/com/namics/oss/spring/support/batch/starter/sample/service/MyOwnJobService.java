package com.namics.oss.spring.support.batch.starter.sample.service;

import com.namics.oss.spring.support.batch.service.impl.JobServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;

/**
 * MyOwnJobService.
 *
 * @author lboesch, Namics AG
 * @since 10.10.17 14:45
 */
public class MyOwnJobService extends JobServiceImpl {

	private static final Logger LOG = LoggerFactory.getLogger(MyOwnJobService.class);

	public MyOwnJobService(JobExplorer jobExplorer, JobOperator jobOperator, JobLauncher jobLauncher, JobRegistry jobRegistry, JobRepository jobRepository) {
		super(jobExplorer, jobOperator, jobLauncher, jobRegistry, jobRepository);
	}

	@Override
	public void startJob(String jobName) {
		LOG.error("do some crazy stuff before starting the batch job.");
		super.startJob(jobName);
	}
}
