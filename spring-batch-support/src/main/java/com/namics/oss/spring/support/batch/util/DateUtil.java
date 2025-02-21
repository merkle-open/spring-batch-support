/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.util;


import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.micrometer.common.lang.Nullable;
import jakarta.inject.Inject;

@Component
public class DateUtil {
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
	private final ZoneId zoneId;

	@Inject
	public DateUtil(final ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public Optional<String> formatDuration(@Nullable Temporal from, @Nullable Temporal to) {
		return Optional
				.ofNullable(from).flatMap(f -> Optional.ofNullable(to).map(t -> Duration.between(f, t)))
				.map(diff ->
						String.format("%d h : %d m : %.3f s",
								diff.toHours(),
								diff.toMinutesPart(),
								(float)diff.toSecondsPart()
						)
				);
	}

	public LocalDateTime asLocalDateTime(final Date date) {
		final Instant instant = Instant.ofEpochMilli(date.getTime());
		return LocalDateTime.ofInstant(instant, zoneId);
	}

	public String formatDate(TemporalAccessor date) {
		if (date != null) {
			return DATE_FORMAT.format(date);
		}
		return "";
	}
}
