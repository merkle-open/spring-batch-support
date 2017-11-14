
package com.namics.oss.spring.support.batch.model;

import com.namics.oss.spring.support.batch.util.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Spring Batch job information.
 *
 * @author draymann, Namics AG
 * lboesch, Namics AG
 * @since Jun 3, 2013
 */
public class Job {
	protected String name;
	protected Date startTime;
	protected Date endTime;
	protected String status;
	protected boolean running;
	protected String exitStatus;
	protected List<Parameter> jobParameters;
	protected List<SimpleStep> steps;

	/**
	 * Creates a new job object with the name given.
	 *
	 * @param name .
	 */
	public Job(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public String getStartTimeFormatted() {
		return DateUtil.getDate(startTime);
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public String getEndTimeFormatted() {
		return DateUtil.getDate(endTime);
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isRunning() {
		return running;
	}

	public String getExitStatus() {
		return exitStatus;
	}

	public void setExitStatus(String exitStatus) {
		this.exitStatus = exitStatus;
	}

	public List<SimpleStep> getSteps() {
		return steps;
	}

	public void setSteps(List<SimpleStep> steps) {
		this.steps = steps;
	}

	public List<Parameter> getJobParameters() {
		return jobParameters;
	}

	public void setJobParameters(List<Parameter> jobParameters) {
		this.jobParameters = jobParameters;
	}

	public String getDuration() {
		if (getStartTime() == null) {
			return "";
		}
		return DateUtil.getDuration(getStartTime(), getEndTime());
	}

	public Job startTime(Date startTime) {
		setStartTime(startTime);
		return this;
	}

	public Job endTime(Date endTime) {
		setEndTime(endTime);
		return this;
	}

	public Job status(String status) {
		setStatus(status);
		return this;
	}

	public Job running(boolean running) {
		setRunning(running);
		return this;
	}

	public Job exitStatus(String exitStatus) {
		setExitStatus(exitStatus);
		return this;
	}

	public Job jobParameters(List<Parameter> jobParameters) {
		setJobParameters(jobParameters);
		return this;
	}

	public Job steps(List<SimpleStep> steps) {
		setSteps(steps);
		return this;
	}
}
