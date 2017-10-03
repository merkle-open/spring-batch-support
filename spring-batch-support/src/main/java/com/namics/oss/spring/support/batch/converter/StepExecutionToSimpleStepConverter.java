/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.converter;

import com.namics.oss.spring.convert.CollectionConverter;
import com.namics.oss.spring.support.batch.model.SimpleStep;
import org.springframework.batch.core.StepExecution;

/**
 * StepExecutionToSimpleStepConverter.
 *
 * @author lboesch, Namics AG
 * @since 27.08.2014
 */
public class StepExecutionToSimpleStepConverter extends CollectionConverter<StepExecution, SimpleStep> {
	@Override
	public SimpleStep convert(StepExecution stepExecution) {
		return convert(stepExecution, null);
	}

	@Override
	public SimpleStep convert(StepExecution source, SimpleStep simpleStep) {
		SimpleStep target = simpleStep != null ? simpleStep : new SimpleStep();
		target.stepName(source.getStepName())
		      .status(source.getStatus().toString())
		      .readCount(source.getReadCount())
		      .writeCount(source.getWriteCount())
		      .commitCount(source.getCommitCount())
		      .rollbackCount(source.getRollbackCount())
		      .readSkipCount(source.getReadSkipCount())
		      .processSkipCount(source.getProcessSkipCount())
		      .writeSkipCount(source.getWriteSkipCount())
		      .startTime(source.getStartTime())
		      .endTime(source.getEndTime())
		      .exitStatus(source.getExitStatus().toString())
		      .lastUpdated(source.getLastUpdated())
		      .exitStatus(source.getExitStatus().toString());
		return target;
	}
}
