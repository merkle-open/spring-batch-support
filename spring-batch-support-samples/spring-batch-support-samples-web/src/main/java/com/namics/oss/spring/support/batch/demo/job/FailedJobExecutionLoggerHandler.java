package com.namics.oss.spring.support.batch.demo.job;

import com.namics.oss.spring.support.batch.handler.FailedJobExecutionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * User: lboesch, Namics AG
 * Date: 21.01.14
 */
@Component
public class FailedJobExecutionLoggerHandler implements FailedJobExecutionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(FailedJobExecutionLoggerHandler.class);

	@Override
	public boolean isApplicable(String jobName) {
		return true;
	}

	@Override
	public void handleFailedJobExecution(String jobName) {
		LOG.error("Job [{}] has failed, this is default logger listener", jobName);
	}
}
