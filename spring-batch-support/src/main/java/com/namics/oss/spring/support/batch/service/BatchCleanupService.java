package com.namics.oss.spring.support.batch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.time.ZoneId.systemDefault;
import static java.util.Date.from;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * BatchCleanupServiceImpl.
 *
 * @author lboesch, Namics AG
 * @since 24.08.17 16:12
 */
public class BatchCleanupService {
	private static final Logger LOG = LoggerFactory.getLogger(BatchCleanupService.class);
	private static final RowMapper<Long> ID_ROW_MAPPER = (rs, rowNum) -> rs.getLong(1);
	private static final RowMapper<JobExecution> JOB_ROW_MAPPER = (rs, rowNum) -> new JobExecution(rs.getLong(1), rs.getLong(2));
	protected final int keepDays;
	protected final JdbcTemplate jdbcTemplate;

	public BatchCleanupService(int keepDays, JdbcTemplate jdbcTemplate) {
		this.keepDays = keepDays;
		this.jdbcTemplate = jdbcTemplate;
	}

	@Transactional
	public int cleanBatchTables() {
		final Date referenceDate = from(now().minusDays(keepDays).atZone(systemDefault()).toInstant());

		LOG.info("deleting batch job executions older than [{}] since keepDays is set to [{}]", referenceDate, keepDays);
		final List<JobExecution> jobExecutions = this.jdbcTemplate.query("SELECT JOB_EXECUTION_ID, JOB_INSTANCE_ID FROM BATCH_JOB_EXECUTION WHERE END_TIME < ?", JOB_ROW_MAPPER, referenceDate);
		if (isEmpty(jobExecutions)) {
			return 0;
		}

		LOG.debug(jobExecutions.size() + " job executions found");
		removeJobExecutions(jobExecutions);
		return jobExecutions.size();
	}

	/*
	 * This code is more or less the same as here:
	 * org.springframework.batch.test.JobRepositoryTestUtils.removeJobExecutions(Collection<JobExecution>)
	 */
	private void removeJobExecutions(Collection<JobExecution> list) {
		for (JobExecution jobExecution : list) {
			final List<Long> stepExecutionIds = this.jdbcTemplate.query("SELECT STEP_EXECUTION_ID FROM BATCH_STEP_EXECUTION WHERE JOB_EXECUTION_ID=?", ID_ROW_MAPPER, jobExecution.getId());
			for (Long stepExecutionId : stepExecutionIds) {
				this.jdbcTemplate.update("DELETE FROM BATCH_STEP_EXECUTION_CONTEXT WHERE STEP_EXECUTION_ID=?", stepExecutionId);
				this.jdbcTemplate.update("DELETE FROM BATCH_STEP_EXECUTION WHERE STEP_EXECUTION_ID=?", stepExecutionId);
			}

			this.jdbcTemplate.update("DELETE FROM BATCH_JOB_EXECUTION_CONTEXT WHERE JOB_EXECUTION_ID=?", jobExecution.getId());
			this.jdbcTemplate.update("DELETE FROM BATCH_JOB_EXECUTION_PARAMS WHERE JOB_EXECUTION_ID=?", jobExecution.getId());
			this.jdbcTemplate.update("DELETE FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID=?", jobExecution.getId());
		}

		for (JobExecution jobExecution : list) {
			this.jdbcTemplate.update("DELETE FROM BATCH_JOB_INSTANCE WHERE JOB_INSTANCE_ID=?", jobExecution.getJobId());
		}
	}

	public static final class JobExecution {
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
}