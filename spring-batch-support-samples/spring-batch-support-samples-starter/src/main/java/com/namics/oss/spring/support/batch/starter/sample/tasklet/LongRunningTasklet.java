package com.namics.oss.spring.support.batch.starter.sample.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * LongRunningTasklet.
 *
 * @author lboesch, Namics AG
 * @since 28.08.17 15:08
 */
public class LongRunningTasklet implements Tasklet {
	private static final Logger LOG = LoggerFactory.getLogger(LongRunningTasklet.class);
	protected final int minutesToSleep;

	public LongRunningTasklet(int minutesToSleep) {
		this.minutesToSleep = minutesToSleep;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LOG.warn("start of long running tasklet");
		try {
			sleep(MINUTES.toMillis(minutesToSleep));
		} catch (RuntimeException e) {
			LOG.error("exception on tasklet!", e);
		}
		LOG.warn("end of long running tasklet");
		return RepeatStatus.FINISHED;
	}
}
