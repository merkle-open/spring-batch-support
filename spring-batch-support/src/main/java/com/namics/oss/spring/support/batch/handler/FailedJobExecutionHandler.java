package com.namics.oss.spring.support.batch.handler;

/**
 * FailedJobExecutionHandler.
 * handler for a failed job
 *
 * @author lboesch, Namics AG
 * @since Dez 20, 2013
 */
public interface FailedJobExecutionHandler {
	/**
	 * verifies if this handler
	 * is applicable for the given job name
	 */
	boolean isApplicable(String jobName);

	/**
	 * handles failed jobs with the given name
	 * (for example sends mail)
	 *
	 * @param jobName jobname of failed job
	 */
	void handleFailedJobExecution(String jobName);

}
