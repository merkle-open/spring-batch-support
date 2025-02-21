
package com.namics.oss.spring.support.batch.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import io.micrometer.common.lang.Nullable;

public class Job {
	private final String name;
	@Nullable
	private final LocalDateTime startTime;
    @Nullable
    private final String startTimeFormatted;
    @Nullable
	private final LocalDateTime endTime;
    @Nullable
    private final String endTimeFormatted;
    @Nullable
    private final String duration;
    @Nullable
	private final String status;
	private final boolean running;
	@Nullable
	private final String exitStatus;
	private final List<Parameter> jobParameters;
	private final List<SimpleStep> steps;

	public Job(final String name) {
		this(name, null, null, null, null, null, null,false, null, Collections.emptyList(), Collections.emptyList());
	}

	public Job(
			String name,
			@Nullable LocalDateTime startTime,
			@Nullable String startTimeFormatted,
			@Nullable LocalDateTime endTime,
			@Nullable String endTimeFormatted,
			@Nullable String duration,
			@Nullable String status,
			boolean running,
			@Nullable String exitStatus,
			List<Parameter> jobParameters,
			List<SimpleStep> steps
	) {
		this.name = name;
		this.startTime = startTime;
        this.startTimeFormatted = startTimeFormatted;
        this.endTime = endTime;
        this.endTimeFormatted = endTimeFormatted;
        this.duration = duration;
        this.status = status;
		this.running = running;
		this.exitStatus = exitStatus;
		this.jobParameters = jobParameters;
		this.steps = steps;
	}

	public String getName() {
		return name;
	}

	public Optional<LocalDateTime> getStartTime() {
		return Optional.ofNullable(startTime);
	}

	public Optional<String> getStartTimeFormatted() {
		return Optional.ofNullable(startTimeFormatted);
	}

	public Optional<LocalDateTime> getEndTime() {
		return Optional.ofNullable(endTime);
	}

	public Optional<String> getEndTimeFormatted() {
		return Optional.ofNullable(endTimeFormatted);
	}

	public Optional<String> getDuration() {
		return Optional.ofNullable(duration);
	}

	public Optional<String> getStatus() {
		return Optional.ofNullable(status);
	}

	public boolean isRunning() {
		return running;
	}

	public Optional<String> getExitStatus() {
		return Optional.ofNullable(exitStatus);
	}

	public List<SimpleStep> getSteps() {
		return steps;
	}

	public List<Parameter> getJobParameters() {
		return jobParameters;
	}
}