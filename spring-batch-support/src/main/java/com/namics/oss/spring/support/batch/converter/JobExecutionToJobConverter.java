/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.converter;

import static java.util.stream.Collectors.toList;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.stereotype.Component;

import com.namics.oss.spring.support.batch.model.Job;
import com.namics.oss.spring.support.batch.model.Parameter;
import com.namics.oss.spring.support.batch.model.ParameterType;
import com.namics.oss.spring.support.batch.util.DateUtil;

import jakarta.inject.Inject;

@Component
public class JobExecutionToJobConverter {
	private final StepExecutionToSimpleStepConverter stepExecutionToSimpleStepConverter;
    private final DateUtil dateUtil;

	@Inject
    public JobExecutionToJobConverter(
			final StepExecutionToSimpleStepConverter stepExecutionToSimpleStepConverter,
			final DateUtil dateUtil
	) {
        this.stepExecutionToSimpleStepConverter = stepExecutionToSimpleStepConverter;
        this.dateUtil = dateUtil;
    }

	public Job convert(final JobExecution source) {
		return new Job(
				source.getJobInstance().getJobName(),
				source.getStartTime(),
				dateUtil.formatDate(source.getStartTime()),
				source.getEndTime(),
				dateUtil.formatDate(source.getEndTime()),
				dateUtil.formatDuration(source.getStartTime(), source.getEndTime()).orElse(null),
				source.getStatus().toString(),
				source.getStatus().isRunning(),
				source.getExitStatus().getExitDescription(),
				getJobParameters(source.getJobParameters()),
				source.getStepExecutions().stream().map(stepExecutionToSimpleStepConverter::convert).collect(toList())
		);
	}

	private List<Parameter> getJobParameters(final JobParameters jobParameters) {
		return jobParameters.getParameters().entrySet()
				.stream()
				.map(this::convertParameterEntryToParameter)
				.collect(toList());
	}

	protected Parameter convertParameterEntryToParameter(final Map.Entry<String, JobParameter<?>> entry) {
		JobParameter<?> parameter = entry.getValue();
		return new Parameter(
				entry.getKey(),
				getValue(parameter),
				getType(parameter)
		);
	}

	private String getValue(final JobParameter<?> parameter) {
		if(parameter.getType() == Date.class) {
			return dateUtil.formatDate(dateUtil.asLocalDateTime((Date) parameter.getValue()));
		}
		return String.valueOf(parameter.getValue());
	}

	private ParameterType getType(final JobParameter<?> parameter) {
		if(parameter.getType() == String.class) {
			return ParameterType.STRING;
		}
		if(parameter.getType() == LocalDateTime.class) {
			return ParameterType.DATE;
		}
		return ParameterType.NUMBER;
	}
}
