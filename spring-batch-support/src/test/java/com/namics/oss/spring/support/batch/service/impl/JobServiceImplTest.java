/*
 * Copyright 2000-2015 namics ag. All rights reserved.
 */

package com.namics.oss.spring.support.batch.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * JobServiceImplTest.
 *
 * @author lboesch, Namics AG
 * @since 29.04.2015
 */
public class JobServiceImplTest {

	JobExplorer jobExplorer = createMock(JobExplorer.class);
	JobOperator jobOperator = createMock(JobOperator.class);
	JobLauncher jobLauncher = createMock(JobLauncher.class);
	JobRegistry jobRegistry = createMock(JobRegistry.class);
	JobRepository jobRepository = createMock(JobRepository.class);

	JobServiceImpl service = new JobServiceImpl(jobExplorer, jobOperator, jobLauncher, jobRegistry, jobRepository);

	@BeforeEach
	public void setUp() {
		reset(jobExplorer, jobOperator, jobLauncher, jobRegistry, jobRepository);
	}

	public void replayAll() {
		replay(jobExplorer, jobOperator, jobLauncher, jobRegistry, jobRepository);
	}

	@AfterEach
	public void shutDown() {
		verify(jobExplorer, jobOperator, jobLauncher, jobRegistry, jobRepository);
	}

	@Test
	public void testGetJobsNullSafe() {
		expect(jobOperator.getJobNames()).andReturn(null).anyTimes();
		replayAll();
		assertThat(service.getJobs(), nullValue());
	}

	@Disabled
	@Test
	public void testGetJobs() throws Exception {
		Set<String> jobNames = new HashSet<>();
		jobNames.add("job1");
		jobNames.add("job2");
		jobNames.add("job3");

		Long job1Id = 1L;
		Long job2Id = 2L;
		List<Long> jobExecutions = new ArrayList<>();
		jobExecutions.add(job1Id);

		JobInstance jobInstance = new JobInstance(job1Id, "job1");

		expect(jobOperator.getJobNames()).andReturn(jobNames).anyTimes();
		expect(jobOperator.getJobInstances(eq("job1"), eq(0), eq(1))).andReturn(jobExecutions);
		expect(jobExplorer.getJobInstance(eq(job1Id))).andReturn(jobInstance);
//		expect(jobOperator.getJobInstances(eq("job2"), eq(0), eq(1))).andReturn(null);
		replayAll();
		assertThat(service.getJobs(), nullValue());
	}

}