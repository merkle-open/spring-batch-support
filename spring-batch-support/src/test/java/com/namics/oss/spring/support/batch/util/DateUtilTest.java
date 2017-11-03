/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.util;

import org.joda.time.DateMidnight;
import org.joda.time.MutableDateTime;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DateUtilTest.
 *
 * @author aschaefer, Namics AG
 * @since 02.07.14 14:44
 */
public class DateUtilTest {

	@Test
	public void testFormatDuration() throws Exception {
		MutableDateTime mutableDateTime = new MutableDateTime(DateMidnight.now());
		Date now = mutableDateTime.toDate();
		mutableDateTime.addDays(1);
		mutableDateTime.addHours(2);
		mutableDateTime.addMinutes(3);
		mutableDateTime.addSeconds(4);
		Date then = mutableDateTime.toDate();
		assertEquals("26 h : 3 m : 4.000 s", DateUtil.getDuration(now, then));
	}
}