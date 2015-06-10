package de.tudarmstadt.informatik.tk.kraken.android.sdk.utils;

import java.util.Calendar;

/**
 * @author Karsten Planz
 */
public class DateUtils {

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
