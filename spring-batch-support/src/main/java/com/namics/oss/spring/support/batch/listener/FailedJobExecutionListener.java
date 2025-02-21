package com.namics.oss.spring.support.batch.listener;

import com.namics.oss.spring.support.batch.handler.FailedJobExecutionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Delegates to applicable handlers if a job has failed
 *
 * @author lboesch, Namics AG
 * @since Jan 09, 2014
 */
public class FailedJobExecutionListener implements JobExecutionListener {
	private static final Logger LOG = LoggerFactory.getLogger(FailedJobExecutionListener.class);
	private final List<FailedJobExecutionHandler> failedJobExecutionHandlers;

	public FailedJobExecutionListener(final List<FailedJobExecutionHandler> failedJobExecutionHandlers) {
		this.failedJobExecutionHandlers = failedJobExecutionHandlers;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		String jobName = jobExecution.getJobInstance().getJobName();
		if (jobExecution.getStatus() == BatchStatus.FAILED) {

			if (!CollectionUtils.isEmpty(jobExecution.getAllFailureExceptions())) {
				for (Throwable t : jobExecution.getAllFailureExceptions()) {
					LOG.info("job [{}] had exception: [{}]", jobName, t, t);
					LOG.warn("job [{}] had exception: [{}]", jobName, t, "");
				}
			}

			if (failedJobExecutionHandlers != null) {
				for (FailedJobExecutionHandler handler : failedJobExecutionHandlers) {
					if (handler.isApplicable(jobName)) {
						try {
							handler.handleFailedJobExecution(jobName);
						} catch (Exception e) {
							LOG.warn("exception on handling of failed job [{}] with handler [{}], [{}]", jobName, handler.getClass(), e, "");
						}
					}
				}
			}
		}
		LOG.info("job [{}] finished, start [{}], end [{}]", jobName, jobExecution.getStartTime(), jobExecution.getEndTime());
	}
}
