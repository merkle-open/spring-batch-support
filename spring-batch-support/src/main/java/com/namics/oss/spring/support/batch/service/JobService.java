
package com.namics.oss.spring.support.batch.service;


import com.namics.oss.spring.support.batch.model.Job;
import org.springframework.batch.core.JobParameters;

import java.util.List;

/**
 * Manages Spring batch jobs.
 *
 * @author draymann, Namics AG
 * lboesch, Namics AG
 * @since Jun 3, 2013
 */
public interface JobService {

	/**
	 * Lists all registered jobs.
	 *
	 * @return a list containing all registered jobs
	 */
	List<Job> getJobs();

	/**
	 * Get job for jobname
	 *
	 * @param jobName the name of the job
	 * @return registered job
	 */
	Job getJob(String jobName);

	/**
	 * Starts a specific job.
	 *
	 * @param jobName the name of the job to start
	 */
	void startJob(String jobName);

	/**
	 * Starts a specific job with the given params.
	 *
	 * @param jobName       the name of the job to start
	 * @param jobParameters the parameters for the job
	 */
	void startJob(String jobName, JobParameters jobParameters);

	/**
	 * Stops a specific job.
	 *
	 * @param jobName .
	 */
	void stopJob(String jobName);

	/**
	 * Abandons a job.
	 *
	 * @param jobName .
	 */
	void abandonJob(String jobName);

	/**
	 * Starts a new job instance.
	 * use JobParametersIncrementer to change job parameters.
	 * it's possible to start two job instances on the same time, take care!
	 *
	 * @param name job name
	 */
	void startNextInstance(String name);
}
