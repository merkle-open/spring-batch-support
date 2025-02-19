package com.namics.oss.spring.support.batch.model;

import java.time.LocalDateTime;

public class SimpleStep {
	private final String stepName;
	private final String status;
	private final long readCount;
	private final long writeCount;
	private final long commitCount;
	private final long rollbackCount;
	private final long readSkipCount;
	private final long processSkipCount;
	private final long writeSkipCount;
	private final LocalDateTime startTime;
	private final LocalDateTime endTime;
	private final LocalDateTime lastUpdated;
	private final String exitStatus;

	public SimpleStep(
			String stepName,
			String status,
			long readCount,
			long writeCount,
			long commitCount,
			long rollbackCount,
			long readSkipCount,
			long processSkipCount,
			long writeSkipCount,
			LocalDateTime startTime,
			LocalDateTime endTime,
			LocalDateTime lastUpdated,
			String exitStatus
	) {
		this.stepName = stepName;
		this.status = status;
		this.readCount = readCount;
		this.writeCount = writeCount;
		this.commitCount = commitCount;
		this.rollbackCount = rollbackCount;
		this.readSkipCount = readSkipCount;
		this.processSkipCount = processSkipCount;
		this.writeSkipCount = writeSkipCount;
		this.startTime = startTime;
		this.endTime = endTime;
		this.lastUpdated = lastUpdated;
		this.exitStatus = exitStatus;
	}

	public String getStepName() {
		return stepName;
	}

	public String getStatus() {
		return status;
	}

	public long getReadCount() {
		return readCount;
	}

	public long getWriteCount() {
		return writeCount;
	}

	public long getCommitCount() {
		return commitCount;
	}

	public long getRollbackCount() {
		return rollbackCount;
	}

	public long getReadSkipCount() {
		return readSkipCount;
	}

	public long getProcessSkipCount() {
		return processSkipCount;
	}

	public long getWriteSkipCount() {
		return writeSkipCount;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public String getExitStatus() {
		return exitStatus;
	}
}
