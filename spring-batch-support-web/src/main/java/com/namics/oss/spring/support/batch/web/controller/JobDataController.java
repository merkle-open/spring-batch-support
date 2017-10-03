/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.web.controller;

import com.namics.oss.spring.support.batch.model.Job;
import com.namics.oss.spring.support.batch.model.Parameter;
import com.namics.oss.spring.support.batch.service.JobService;
import com.namics.oss.spring.support.batch.web.bean.JobParameterBean;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.batch.support.PropertiesConverter.stringToProperties;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * JobDataController.
 *
 * @author lboesch, Namics AG
 * @since 20.06.2014
 */
@RestController
@RequestMapping("/jobs")
public class JobDataController {
	public static final String EXECUTION_TIMESTAMP = "executionTimestamp";
	public static final String JOB_PARAMETERS = "jobParameters";

	protected JobService jobService;
	protected JobParametersConverter jobParametersConverter = new DefaultJobParametersConverter();

	public JobDataController(JobService jobService) {
		this.jobService = jobService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<Job> getJobs() {
		return jobService.getJobs();
	}

	@RequestMapping(value = "/{jobName}", method = RequestMethod.GET)
	public Job getJobs(@PathVariable("jobName") String jobName) {
		return jobService.getJob(jobName);
	}

	@RequestMapping(value = "/{jobName}/parameters", method = RequestMethod.GET)
	public List<JobParameterBean> getJobParameters(@PathVariable("jobName") String jobName) {
		Job job = jobService.getJob(jobName);
		List<JobParameterBean> jobParameterBeans = new ArrayList<>();
		if (job.getJobParameters() != null) {
			for (Parameter parameter : job.getJobParameters()) {
				if (!EXECUTION_TIMESTAMP.equalsIgnoreCase(parameter.getKey())) {
					jobParameterBeans.add(new JobParameterBean()
							                      .name(parameter.getKey())
							                      .value(parameter.getValue())
							                      .parameterType(parameter.getParameterType()));
				}
			}
		}
		return jobParameterBeans;
	}

	@RequestMapping(value = "/{jobName}/start", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void startJob(@PathVariable("jobName") String jobName, @RequestParam(required = false) MultiValueMap<String, String> payload) {
		if (isEmpty(payload) || !payload.containsKey(JOB_PARAMETERS)) {
			jobService.startJob(jobName);
		}
		JobParameters jobParameters = jobParametersConverter.getJobParameters(stringToProperties(payload.getFirst(JOB_PARAMETERS)));
		jobService.startJob(jobName, jobParameters);
	}

	@RequestMapping(value = "/{jobName}/stop", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void stopJob(@PathVariable("jobName") String jobName) {
		jobService.stopJob(jobName);
	}
}
