/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.model;

import com.namics.oss.spring.support.batch.util.DateUtil;

import java.util.Date;

/**
 * SimpleStep.
 *
 * @author lboesch, Namics AG
 * @since 07.04.2014
 */
public class SimpleStep {

	protected String stepName;
	protected String status;
	protected int readCount;
	protected int writeCount;
	protected int commitCount;
	protected int rollbackCount;
	protected int readSkipCount;
	protected int processSkipCount;
	protected int writeSkipCount;
	protected Date startTime;
	protected Date endTime;
	protected Date lastUpdated;
	protected String exitStatus;

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}

	public int getWriteCount() {
		return writeCount;
	}

	public SimpleStep setWriteCount(int writeCount) {
		this.writeCount = writeCount;
		return this;
	}

	public int getCommitCount() {
		return commitCount;
	}

	public void setCommitCount(int commitCount) {
		this.commitCount = commitCount;
	}

	public int getRollbackCount() {
		return rollbackCount;
	}

	public void setRollbackCount(int rollbackCount) {
		this.rollbackCount = rollbackCount;
	}

	public int getReadSkipCount() {
		return readSkipCount;
	}

	public void setReadSkipCount(int readSkipCount) {
		this.readSkipCount = readSkipCount;
	}

	public int getProcessSkipCount() {
		return processSkipCount;
	}

	public void setProcessSkipCount(int processSkipCount) {
		this.processSkipCount = processSkipCount;
	}

	public int getWriteSkipCount() {
		return writeSkipCount;
	}

	public void setWriteSkipCount(int writeSkipCount) {
		this.writeSkipCount = writeSkipCount;
	}

	public Date getStartTime() {
		return startTime;
	}

	public String getStartTimeFormatted() {
		return DateUtil.getDate(startTime);
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public String getEndTimeFormatted() {
		return DateUtil.getDate(endTime);
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public String getLastUpdatedFormatted() {
		return DateUtil.getDate(lastUpdated);
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getExitStatus() {
		return exitStatus;
	}

	public void setExitStatus(String exitStatus) {
		this.exitStatus = exitStatus;
	}

	public String getDuration() {
		if (getStartTime() == null) {
			return "";
		}
		return DateUtil.getDuration(getStartTime(), getEndTime());
	}

	public SimpleStep stepName(String stepName) {
		setStepName(stepName);
		return this;
	}

	public SimpleStep status(String status) {
		setStatus(status);
		return this;
	}

	public SimpleStep readCount(int readCount) {
		setReadCount(readCount);
		return this;
	}

	public SimpleStep writeCount(int writeCount) {
		setWriteCount(writeCount);
		return this;
	}

	public SimpleStep commitCount(int commitCount) {
		setCommitCount(commitCount);
		return this;
	}

	public SimpleStep rollbackCount(int rollbackCount) {
		setRollbackCount(rollbackCount);
		return this;
	}

	public SimpleStep readSkipCount(int readSkipCount) {
		setReadSkipCount(readSkipCount);
		return this;
	}

	public SimpleStep processSkipCount(int processSkipCount) {
		setProcessSkipCount(processSkipCount);
		return this;
	}

	public SimpleStep writeSkipCount(int writeSkipCount) {
		setWriteSkipCount(writeSkipCount);
		return this;
	}

	public SimpleStep startTime(Date startTime) {
		setStartTime(startTime);
		return this;
	}

	public SimpleStep endTime(Date endTime) {
		setEndTime(endTime);
		return this;
	}

	public SimpleStep lastUpdated(Date lastUpdated) {
		setLastUpdated(lastUpdated);
		return this;
	}

	public SimpleStep exitStatus(String exitStatus) {
		setExitStatus(exitStatus);
		return this;
	}
}
