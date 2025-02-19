package com.namics.oss.spring.support.batch.handler.impl;

import com.namics.oss.spring.support.batch.handler.FailedJobExecutionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FailedJobExecutionLoggerHandler implements FailedJobExecutionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(FailedJobExecutionLoggerHandler.class);

	@Override
	public boolean isApplicable(String jobName) {
		return true; //use for every job
	}

	@Override
	public void handleFailedJobExecution(String jobName) {
		LOG.error("batch job with name={} failed. please take required action to fix it.", jobName);
	}
}
