package de.tudarmstadt.informatik.tk.assistance.sdk.util;

import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Karsten Planz
 * @edited on 30.08.2015 by  Wladimir Schmidt (wlsc.dev@gmail.com)
 */
public enum DateUtils {
    ;

    /**
     * http://www.w3.org/TR/NOTE-datetime
     */
    private static final String DATE_ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * Converts given date to ISO 8601 format
     *
     * @param date
     * @param locale
     * @return
     */
    @Nullable
    public static String dateToISO8601String(Date date, Locale locale) {

        if (date == null) {
            return null;
        }

        DateFormat dateFormat;

        if (locale == null) {
            dateFormat = new SimpleDateFormat(DATE_ISO8601_FORMAT, Locale.US);
        } else {
            dateFormat = new SimpleDateFormat(DATE_ISO8601_FORMAT, locale);
        }

        TimeZone timeZoneUTC = TimeZone.getTimeZone("UTC");

        dateFormat.setTimeZone(timeZoneUTC);
        return dateFormat.format(date);
    }

    /**
     * Converts timestamp to ISO 8601 format
     *
     * @param timestamp
     * @param localeStr
     * @return
     */
    public static String timestampToISO8601String(long timestamp, String localeStr) {

        if (timestamp < 0) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_ISO8601_FORMAT);

        if (localeStr == null || localeStr.trim().isEmpty()) {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        } else {
            sdf.setTimeZone(TimeZone.getTimeZone(localeStr));
        }

        return sdf.format(new Date(timestamp));
    }

    public static long[] getDayStartEnd(Calendar day) {

        Calendar temp = (Calendar) day.clone();
        long[] startEnd = new long[2];
        temp.set(Calendar.HOUR_OF_DAY, 0);
        temp.set(Calendar.MINUTE, 0);
        temp.set(Calendar.SECOND, 0);
        temp.set(Calendar.MILLISECOND, 0);
        startEnd[0] = temp.getTimeInMillis();
        temp.add(Calendar.DATE, 1);
        temp.add(Calendar.SECOND, -1);
        startEnd[1] = temp.getTimeInMillis();

        return startEnd;
    }

    public static boolean isToday(Calendar date) {
        Calendar today = Calendar.getInstance();
        today = resetTime(today);
        date = resetTime(date);
        return today.compareTo(date) == 0;
    }

    public static boolean isYesterday(Calendar date) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        yesterday = resetTime(yesterday);
        date = resetTime(date);
        return yesterday.compareTo(date) == 0;
    }

    public static Calendar resetTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

}
