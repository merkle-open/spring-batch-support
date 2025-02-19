package com.namics.oss.spring.support.batch.service;

import com.namics.oss.spring.support.batch.converter.JobExecutionToJobConverter;

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
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.util.CollectionUtils.isEmpty;

import jakarta.inject.Inject;

/**
 * Default implementation.
 * used to start/stop jobs without knowing of the specific spring batch job repo/launcher classes. adds timestamp to each job as param to ensure uniqueness.
 *
 * @author lboesch, Namics AG
 * @since Jan 10, 2013
 */
@Component
public class JobService {
	private static final Logger LOG = LoggerFactory.getLogger(JobService.class);
	public static final String EXECUTION_TIMESTAMP = "executionTimestamp";

	private final JobExplorer jobExplorer;
	private final JobOperator jobOperator;
	private final JobLauncher jobLauncher;
	private final JobRegistry jobRegistry;
	private final JobRepository jobRepository;
    private final JobExecutionToJobConverter jobExecutionToJobConverter;

	@Inject
    public JobService(
			final JobExplorer jobExplorer,
			final JobOperator jobOperator,
			final JobLauncher jobLauncher,
			final JobRegistry jobRegistry,
			final JobRepository jobRepository,
			final JobExecutionToJobConverter jobExecutionToJobConverter
	) {
		this.jobExplorer = jobExplorer;
		this.jobOperator = jobOperator;
		this.jobLauncher = jobLauncher;
		this.jobRegistry = jobRegistry;
		this.jobRepository = jobRepository;
        this.jobExecutionToJobConverter = jobExecutionToJobConverter;
    }

	public List<com.namics.oss.spring.support.batch.model.Job> getJobs() {
		return jobOperator.getJobNames()
				.stream()
				.map(this::getJobOfJobName)
				.toList();
	}

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
		JobExecution execution = getLatestJobExecution(jobName);
		if (execution != null) {
			return jobExecutionToJobConverter.convert(execution);
		}
		return new com.namics.oss.spring.support.batch.model.Job(jobName);
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
			Long jobInstanceId = jobInstances.getFirst();
			JobInstance jobInstance = jobExplorer.getJobInstance(jobInstanceId);
			if (jobInstance == null) {
				return null;
			}
			List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
			if (isEmpty(jobExecutions)) {
				return null;
			}
			return jobExecutions.getFirst();
		} catch (NoSuchJobException e) {
			return null;
		}

	}

	public void stopJob(String jobName) {
		JobExecution execution = getLatestJobExecution(jobName);
		if (execution != null) {
			try {
				jobOperator.stop(execution.getId());
			} catch (NoSuchJobExecutionException e) {
				LOG.error("there's no job execution for job " + jobName, e);
			} catch (JobExecutionNotRunningException e) {
				LOG.warn("can't stop job, is not running", e);
			}
		}
	}

	public void abandonJob(String jobName) {
		JobExecution execution = getLatestJobExecution(jobName);
		if (execution != null) {
			execution.upgradeStatus(BatchStatus.ABANDONED);
			execution.setEndTime(LocalDateTime.now());
			jobRepository.update(execution);
		}
	}

	public void startJob(String jobName) {
		startJob(jobName, new JobParameters());
	}

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
			LOG.error("the job " + jobName + " does not exist", e);
		} catch (JobParametersInvalidException e) {
			LOG.error("the job " + jobName + " does have invalid parameters:", e);
		} catch (JobExecutionAlreadyRunningException e) {
			LOG.error("the job " + jobName + " is already running:", e);
		} catch (JobRestartException e) {
			LOG.error("the job " + jobName + " could not be restarted:", e);
		} catch (JobInstanceAlreadyCompleteException e) {
			LOG.error("the job " + jobName + " could not restart an already successful instance:", e);
		}
	}

	public void startNextInstance(String jobName) {
		try {
			jobOperator.startNextInstance(jobName);
		} catch (NoSuchJobException e) {
			LOG.error("the job " + jobName + " does not exist:", e);
		} catch (JobParametersInvalidException e) {
			LOG.error("the job " + jobName + " does have invalid parameters:", e);
		} catch (JobExecutionAlreadyRunningException e) {
			LOG.error("the job " + jobName + " is already running:", e);
		} catch (JobRestartException e) {
			LOG.error("the job " + jobName + " could not be restarted:", e);
		} catch (JobInstanceAlreadyCompleteException e) {
			LOG.error("the job " + jobName + " could not restart an already successful instance:", e);
		} catch (JobParametersNotFoundException e) {
			LOG.error("the job " + jobName + " could not find params:", e);
		}
	}

}
