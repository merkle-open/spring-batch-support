/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.converter;

import com.namics.oss.spring.convert.CollectionConverter;
import com.namics.oss.spring.support.batch.model.Job;
import com.namics.oss.spring.support.batch.model.Parameter;
import com.namics.oss.spring.support.batch.model.ParameterType;
import com.namics.oss.spring.support.batch.util.DateUtil;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * JobExecutionToJobConverter.
 *
 * @author lboesch, Namics AG
 * @since 27.08.2014
 */
public class JobExecutionToJobConverter extends CollectionConverter<JobExecution, Job> {
	@Override
	public Job convert(JobExecution jobExecution) {
		return convert(jobExecution, null);
	}

	@Override
	public Job convert(JobExecution source, Job job) {
		Job target = job != null ? job : new Job(source.getJobConfigurationName());
		target.startTime(source.getStartTime())
		      .endTime(source.getEndTime())
		      .status(source.getStatus().toString())
		      .running(source.getStatus().isRunning())
		      .exitStatus(source.getExitStatus().getExitDescription())
		      .jobParameters(getJobParameters(source.getJobParameters()))
		      .steps(new StepExecutionToSimpleStepConverter().convertAll(new ArrayList<>(source.getStepExecutions())));
		return target;
	}

	private List<Parameter> getJobParameters(JobParameters jobParameters) {
		if (jobParameters == null || isEmpty(jobParameters.getParameters())) {
			return emptyList();
		}
		return jobParameters.getParameters().entrySet()
		                    .stream()
		                    .map(this::convertParameterEntryToParameter)
		                    .collect(toList());
	}

	protected Parameter convertParameterEntryToParameter(Map.Entry<String, JobParameter> entry) {
		Parameter paramToAdd = new Parameter().setKey(entry.getKey());
		JobParameter parameter = entry.getValue();
		if (parameter.getType() == JobParameter.ParameterType.DATE) {
			paramToAdd.setParameterType(ParameterType.DATE);
			Date param = (Date) parameter.getValue();
			if (param != null) {
				paramToAdd.value(DateUtil.getDate(param));
			}
			return paramToAdd;
		}
		return paramToAdd
				.value(parameter.getValue().toString())
				.parameterType(parameter.getType() == JobParameter.ParameterType.STRING ?
						               ParameterType.STRING : ParameterType.NUMBER);
	}
}
