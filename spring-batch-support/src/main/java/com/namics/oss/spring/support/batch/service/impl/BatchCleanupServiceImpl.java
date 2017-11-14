package com.namics.oss.spring.support.batch.service.impl;

import com.namics.oss.spring.support.batch.service.BatchCleanupService;
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
public class BatchCleanupServiceImpl implements BatchCleanupService {
	private static final Logger LOG = LoggerFactory.getLogger(BatchCleanupServiceImpl.class);

	protected final int keepDays;

	protected final JdbcTemplate jdbcTemplate;

	public BatchCleanupServiceImpl(int keepDays, JdbcTemplate jdbcTemplate) {
		this.keepDays = keepDays;
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	@Transactional
	public int cleanBatchTables() {
		Date referenceDate = from(now().minusDays(keepDays).atZone(systemDefault()).toInstant());

		LOG.info("deleting batch job executions older than [{}] since keepDays is set to [{}]", referenceDate, keepDays);
		List<JobExecution> jobExecutions = this.jdbcTemplate.query(
				"select JOB_EXECUTION_ID, JOB_INSTANCE_ID FROM BATCH_JOB_EXECUTION WHERE END_TIME < ?", JOB_ROW_MAPPER,
				referenceDate);
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
	protected void removeJobExecutions(Collection<JobExecution> list) {
		for (JobExecution jobExecution : list) {
			List<Long> stepExecutionIds = this.jdbcTemplate.query("select STEP_EXECUTION_ID from BATCH_STEP_EXECUTION where JOB_EXECUTION_ID=?",
			                                                      ID_ROW_MAPPER, jobExecution.getId());
			for (Long stepExecutionId : stepExecutionIds) {
				this.jdbcTemplate.update("delete from BATCH_STEP_EXECUTION_CONTEXT where STEP_EXECUTION_ID=?", stepExecutionId);
				this.jdbcTemplate.update("delete from BATCH_STEP_EXECUTION where STEP_EXECUTION_ID=?", stepExecutionId);
			}

			this.jdbcTemplate.update("delete from BATCH_JOB_EXECUTION_CONTEXT where JOB_EXECUTION_ID=?", jobExecution.getId());
			this.jdbcTemplate.update("delete from BATCH_JOB_EXECUTION_PARAMS where JOB_EXECUTION_ID=?", jobExecution.getId());
			this.jdbcTemplate.update("delete from BATCH_JOB_EXECUTION where JOB_EXECUTION_ID=?", jobExecution.getId());
		}

		for (JobExecution jobExecution : list) {
			this.jdbcTemplate.update("delete from BATCH_JOB_INSTANCE where JOB_INSTANCE_ID=?", jobExecution.getJobId());
		}
	}

	protected static final RowMapper<Long> ID_ROW_MAPPER = (rs, rowNum) -> rs.getLong(1);

	protected static final RowMapper<JobExecution> JOB_ROW_MAPPER = (rs, rowNum) -> new JobExecution(rs.getLong(1), rs.getLong(2));

	protected static final class JobExecution {

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
