package com.namics.oss.spring.support.batch.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

/**
 * Removes old job instances and executions from the Spring Batch database tables.
 * SpringBatchDatabaseCleanUpTasklet.
 *
 * @author draymann, Namics AG
 * lboesch, Namics AG
 * @since Jun 3, 2013
 * @deprecated use @see BatchCleanupServiceImpl with own trigger outside of batch context.
 */
@Deprecated
public class SpringBatchDatabaseCleanUpTasklet implements Tasklet {

	private static final Logger LOG = LoggerFactory.getLogger(SpringBatchDatabaseCleanUpTasklet.class);

	protected int keepDays = -1;

	protected JdbcTemplate jdbcTemplate;

	private static final long DAY_IN_MILLIS = 24L * 60 * 60 * 1000;

	private static final RowMapper<Long> ID_ROW_MAPPER = (rs, rowNum) -> rs.getLong(1);

	private static final RowMapper<JobExecution> JOB_ROW_MAPPER = (rs, rowNum) -> new JobExecution(rs.getLong(1), rs.getLong(2));

	private static final class JobExecution {

		private final Long id;

		private final Long jobId;

		private JobExecution(Long id, Long jobId) {
			this.id = id;
			this.jobId = jobId;
		}

		private Long getId() {
			return id;
		}

		private Long getJobId() {
			return jobId;
		}

	}

	public SpringBatchDatabaseCleanUpTasklet(int keepDays, JdbcTemplate jdbcTemplate) {
		isTrue(keepDays > 0);
		notNull(jdbcTemplate);
		this.keepDays = keepDays;
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Deletes all spring batch database entries, which has an older job execution end date as xDaysOld away.
	 *
	 * @param sc StepContribution
	 * @param cc ChunkContext
	 * @return RepeatStatus
	 */
	@Override
	public RepeatStatus execute(StepContribution sc, ChunkContext cc) {
		Date currentDate = new Date();
		Date referenceDate = new Date(currentDate.getTime() - (DAY_IN_MILLIS * keepDays));
		LOG.info("deleting batch job executions older than [{}] since keepDays is set to [{}]", referenceDate, keepDays);
		List<JobExecution> jobExecutions = this.jdbcTemplate.query(
				"select JOB_EXECUTION_ID, JOB_INSTANCE_ID FROM BATCH_JOB_EXECUTION WHERE END_TIME < ?", JOB_ROW_MAPPER,
				new Object[] { referenceDate });

		LOG.info(jobExecutions.size() + " job executions found");
		removeJobExecutions(jobExecutions);
		return RepeatStatus.FINISHED;
	}

	/*
	 * This code is more or less the same as here:
	 * org.springframework.batch.test.JobRepositoryTestUtils.removeJobExecutions(Collection<JobExecution>)
	 */
	private void removeJobExecutions(Collection<JobExecution> list) {
		for (JobExecution jobExecution : list) {
			List<Long> stepExecutionIds = this.jdbcTemplate.query("select STEP_EXECUTION_ID from BATCH_STEP_EXECUTION where JOB_EXECUTION_ID=?",
			                                                      ID_ROW_MAPPER, new Object[] { jobExecution.getId() });
			for (Long stepExecutionId : stepExecutionIds) {
				this.jdbcTemplate.update("delete from BATCH_STEP_EXECUTION_CONTEXT where STEP_EXECUTION_ID=?", new Object[] { stepExecutionId });
				this.jdbcTemplate.update("delete from BATCH_STEP_EXECUTION where STEP_EXECUTION_ID=?", new Object[] { stepExecutionId });
			}

			this.jdbcTemplate.update("delete from BATCH_JOB_EXECUTION_CONTEXT where JOB_EXECUTION_ID=?", new Object[] { jobExecution.getId() });
			this.jdbcTemplate.update("delete from BATCH_JOB_EXECUTION_PARAMS where JOB_EXECUTION_ID=?", new Object[] { jobExecution.getId() });
			this.jdbcTemplate.update("delete from BATCH_JOB_EXECUTION where JOB_EXECUTION_ID=?", new Object[] { jobExecution.getId() });
		}

		for (JobExecution jobExecution : list) {
			this.jdbcTemplate.update("delete from BATCH_JOB_INSTANCE where JOB_INSTANCE_ID=?", new Object[] { jobExecution.getJobId() });
		}
	}
}
