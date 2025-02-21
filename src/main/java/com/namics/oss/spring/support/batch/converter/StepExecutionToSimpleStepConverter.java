package com.namics.oss.spring.support.batch.converter;

import com.namics.oss.spring.support.batch.model.SimpleStep;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

@Component
public class StepExecutionToSimpleStepConverter {

	public SimpleStep convert(final StepExecution source) {
		return new SimpleStep(
				source.getStepName(),
				source.getStatus().toString(),
				source.getReadCount(),
				source.getWriteCount(),
				source.getCommitCount(),
				source.getRollbackCount(),
				source.getReadSkipCount(),
				source.getProcessSkipCount(),
				source.getWriteSkipCount(),
				source.getStartTime(),
				source.getEndTime(),
				source.getLastUpdated(),
				source.getExitStatus().toString()
		);
	}
}
