package com.namics.oss.spring.support.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * BatchJobRestart.
 *
 * @author lboesch, Namics AG
 * @since 28.08.17 14:28
 */
public class BatchJobRestart implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(BatchJobRestart.class);

	protected final JobExplorer jobExplorer;

	public BatchJobRestart(JobExplorer jobExplorer) {
		this.jobExplorer = jobExplorer;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		List<JobExecution> runningJobInstancesOnStartup = jobExplorer.getJobNames()
		                                                             .stream()
		                                                             .map(jobExplorer::findRunningJobExecutions)
		                                                             .flatMap(Collection::stream)
		                                                             .collect(toList());
		runningJobInstancesOnStartup.forEach(e -> LOG.warn("found running job execution on context refresh: name={}, instanceId={}",
		                                                   e.getJobConfigurationName(), e.getJobInstance().getInstanceId()));
	}
}
