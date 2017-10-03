package com.namics.oss.spring.support.batch.service.impl;

import com.namics.oss.spring.support.batch.converter.JobExecutionToJobConverter;
import com.namics.oss.spring.support.batch.model.Job;
import com.namics.oss.spring.support.batch.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;

import java.util.*;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Default implementation.
 * used to start/stop jobs without knowing of the specific spring batch job repo/launcher classes. adds timestamp to each job as param to ensure uniqueness.
 *
 * @author lboesch, Namics AG
 * @since Jan 10, 2013
 */
public class JobServiceImpl implements JobService {
	private static final Logger LOG = LoggerFactory.getLogger(JobServiceImpl.class);
	public static final String EXECUTION_TIMESTAMP = "executionTimestamp";

	protected JobExplorer jobExplorer;
	protected JobOperator jobOperator;
	protected JobLauncher jobLauncher;
	protected JobRegistry jobRegistry;
	protected JobRepository jobRepository;

	public JobServiceImpl(JobExplorer jobExplorer, JobOperator jobOperator, JobLauncher jobLauncher, JobRegistry jobRegistry,
	                      JobRepository jobRepository) {
		this.jobExplorer = jobExplorer;
		this.jobOperator = jobOperator;
		this.jobLauncher = jobLauncher;
		this.jobRegistry = jobRegistry;
		this.jobRepository = jobRepository;
	}

	@Override
	public List<com.namics.oss.spring.support.batch.model.Job> getJobs() {
		Set<String> jobNames = jobOperator.getJobNames();
		if (!isEmpty(jobNames)) {
			List<com.namics.oss.spring.support.batch.model.Job> jobs = new ArrayList<>(jobNames.size());
			for (String jobName : jobNames) {
				com.namics.oss.spring.support.batch.model.Job job = getJobOfJobName(jobName);
				jobs.add(job);
			}
			return jobs;
		}
		return null;
	}

	@Override
	public com.namics.oss.spring.support.batch.model.Job getJob(String jobName) {
		return getJobOfJobName(jobName);
	}

	/**
	 * returns the job with the last execution
	 *
	 * @param jobName name of the job
	 * @return job with latest execution
	 */
	protected com.namics.oss.spring.support.batch.model.Job getJobOfJobName(String jobName) {
		com.namics.oss.spring.support.batch.model.Job job = new Job(jobName);
		JobExecution execution = getLatestJobExecution(jobName);
		if (execution != null) {
			new JobExecutionToJobConverter().convert(execution, job);
		}
		return job;
	}

	/**
	 * get latest execution for a job with the given name.
	 *
	 * @param jobName name of the job
	 * @return latest execution
	 */
	protected JobExecution getLatestJobExecution(String jobName) {
		try {
			List<Long> jobInstances = jobOperator.getJobInstances(jobName, 0, 1); //todo expensive query!
			if (isEmpty(jobInstances)) {
				return null;
			}
			Long jobInstanceId = jobInstances.get(0);
			JobInstance jobInstance = jobExplorer.getJobInstance(jobInstanceId);
			if (jobInstance == null) {
				return null;
			}
			List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
			if (isEmpty(jobExecutions)) {
				return null;
			}
			return jobExecutions.get(0);
		} catch (NoSuchJobException e) {
			return null;
		}

	}

	@Override
	public void stopJob(String jobName) {
		JobExecution execution = getLatestJobExecution(jobName);
		if (execution != null) {
			try {
				jobOperator.stop(execution.getId());
			} catch (NoSuchJobExecutionException e) {
				LOG.error("there's no job execution for job {}: {}", jobName, e, "");
				LOG.debug("there's no job execution for job {} ", jobName, e);
			} catch (JobExecutionNotRunningException e) {
				LOG.warn("can't stop job, is not running: {}", e, "");
				LOG.debug("can't stop job, is not running", e);
			}
		}
	}

	@Override
	public void abandonJob(String jobName) {
		JobExecution execution = getLatestJobExecution(jobName);
		if (execution != null) {
			execution.upgradeStatus(BatchStatus.ABANDONED);
			execution.setEndTime(new Date());
			jobRepository.update(execution);
		}
	}

	@Override
	public void startJob(String jobName) {
		startJob(jobName, new JobParameters());
	}

	@Override
	public void startJob(String jobName, JobParameters jobParameters) {
		JobExecution jobExecution = getLatestJobExecution(jobName);

		if (jobExecution != null && jobExecution.getStatus().isRunning()) {
			LOG.info("the job {} is already running", jobName);
			return;
		}

		JobParametersBuilder builder = new JobParametersBuilder(jobParameters);
		Date executionTimestamp = Calendar.getInstance().getTime();
		builder.addDate(EXECUTION_TIMESTAMP, executionTimestamp);
		try {
			jobLauncher.run(jobRegistry.getJob(jobName), builder.toJobParameters());
		} catch (NoSuchJobException e) {
			LOG.error("the job {} does not exist: {}", jobName, e, "");
			LOG.debug("the job {} does not exist", jobName, e);
		} catch (JobParametersInvalidException e) {
			LOG.error("the job {} does have invalid parameters: {}", jobName, e, "");
			LOG.debug("the job {} does have invalid parameters", jobName, e);
		} catch (JobExecutionAlreadyRunningException e) {
			LOG.error("the job {} is already running: {}", jobName, e, "");
			LOG.debug("the job {} is already running", jobName, e);
		} catch (JobRestartException e) {
			LOG.error("the job {} could not be restarted: {}", jobName, e, "");
			LOG.debug("the job {} could not be restarted", jobName, e);
		} catch (JobInstanceAlreadyCompleteException e) {
			LOG.error("the job {} could not restart an already successful instance: {}", jobName, e, "");
			LOG.debug("the job {} could not restart an already successful instance", jobName, e);
		}
	}

	public void startNextInstance(String jobName) {
		try {
			jobOperator.startNextInstance(jobName);
		} catch (NoSuchJobException e) {
			LOG.error("the job {} does not exist: {}", jobName, e, "");
			LOG.debug("the job {} does not exist", jobName, e);
		} catch (JobParametersInvalidException e) {
			LOG.error("the job {} does have invalid parameters: {}", jobName, e, "");
			LOG.debug("the job {} does have invalid parameters", jobName, e);
		} catch (JobExecutionAlreadyRunningException e) {
			LOG.error("the job {} is already running: {}", jobName, e, "");
			LOG.debug("the job {} is already running", jobName, e);
		} catch (JobRestartException e) {
			LOG.error("the job {} could not be restarted: {}", jobName, e, "");
			LOG.debug("the job {} could not be restarted", jobName, e);
		} catch (JobInstanceAlreadyCompleteException e) {
			LOG.error("the job {} could not restart an already successful instance: {}", jobName, e, "");
			LOG.debug("the job {} could not restart an already successful instance", jobName, e);
		} catch (JobParametersNotFoundException e) {
			LOG.error("the job {} could not find params: {}", jobName, e, "");
			LOG.debug("the job {} could not find params", jobName, e);
		}
	}

}
