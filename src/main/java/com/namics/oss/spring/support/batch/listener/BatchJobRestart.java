package com.namics.oss.spring.support.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Collection;

public class BatchJobRestart implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(BatchJobRestart.class);
    private final JobExplorer jobExplorer;

    public BatchJobRestart(final JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        jobExplorer
                .getJobNames()
                .stream()
                .map(jobExplorer::findRunningJobExecutions)
                .flatMap(Collection::stream)
                .forEach(e ->
                        LOG.warn(
                                "found running job execution on context refresh: name={}, instanceId={}",
                                e.getJobInstance().getJobName(),
                                e.getJobInstance().getInstanceId()
                        )
                );
    }
}
