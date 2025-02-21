package com.namics.oss.spring.support.batch.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class DateUtilTest {

	@Test
	public void testFormatDuration() {
		final LocalDateTime now = LocalDateTime.now();
		final LocalDateTime then = now.plusHours(26).plusMinutes(3).plusSeconds(4);

		assertEquals(Optional.of("26 h : 3 m : 4.000 s"), new DateUtil(ZoneId.systemDefault()).formatDuration(now, then));
	}

	@Test
	void formatDate() {
		assertEquals(
				"20.02.2025 14:15:59",
				new DateUtil(ZoneId.systemDefault()).formatDate(LocalDateTime.of(2025, 2, 20, 14, 15, 59))
		);
	}
}