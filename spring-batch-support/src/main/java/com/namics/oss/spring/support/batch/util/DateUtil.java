/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.util;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateUtil.
 *
 * @author lboesch, Namics AG
 * @since 17.04.2014
 */
public class DateUtil {

	//todo rewrite to new time api and skip joda time!

	public static final String DATE_STRING = "dd.MM.yyyy HH:mm:SS";
	protected static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_STRING);
	private static final DateUtil INSTANCE = new DateUtil();
	private static final PeriodFormatter daysHoursMinutes = new PeriodFormatterBuilder().appendDays()
	                                                                                    .appendSuffix(" d", " d")
	                                                                                    .appendSeparator(" : ")
	                                                                                    .appendHours()
	                                                                                    .appendSuffix(" h", " h")
	                                                                                    .appendSeparator(" : ")
	                                                                                    .appendMinutes()
	                                                                                    .appendSuffix(" m", " m")
	                                                                                    .appendSeparator(" : ")
	                                                                                    .appendSecondsWithMillis()
	                                                                                    .appendSuffix(" s", " s")
	                                                                                    .toFormatter();

	public static String getDuration(Date from, Date to) {
		return INSTANCE.formatDuration(from, to);
	}

	public static String getDate(Date date) {
		return INSTANCE.formatDate(date);
	}

	public String formatDuration(Date from, Date to) {
		DateTime endTime = new DateTime();
		if (to != null) {
			endTime = new DateTime(to);
		}
		return daysHoursMinutes.print(new Duration(new DateTime(from), endTime).toPeriod());
	}

	public String formatDate(Date date) {
		if (date != null) {
			return DATE_FORMAT.format(date);
		}
		return "";
	}
}
