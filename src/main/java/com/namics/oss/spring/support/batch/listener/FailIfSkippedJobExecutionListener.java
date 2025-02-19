package com.namics.oss.spring.support.batch.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

/**
 * Fails whole job if a step skipped an element.
 */
public class FailIfSkippedJobExecutionListener implements JobExecutionListener {

	@Override
	public void afterJob(JobExecution jobExecution) {
		for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
			if (stepExecution.getSkipCount() > 0) {
				jobExecution.setExitStatus(ExitStatus.FAILED);
				jobExecution.setStatus(BatchStatus.FAILED);
				return;
			}
		}
	}

}
