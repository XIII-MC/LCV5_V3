package com.xiii.libertycity.core.utils.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    private static final DateFormat basicFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
    private static final DateFormat fullFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final DateFormat logFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
    private static final DateFormat logDateFormat = new SimpleDateFormat("dd_MM_yyy");

    public static String convertMillis(final long millis, TimeFormat formatType) {

        final Date date = getDate(millis);

        if (formatType == TimeFormat.BASIC) return basicFormat.format(date);
        if (formatType == TimeFormat.HOUR) return hourFormat.format(date);
        if (formatType == TimeFormat.FULL) return fullFormat.format(date);
        if (formatType == TimeFormat.LOG) return logFormat.format(date);
        if (formatType == TimeFormat.LOG_DATE) return logDateFormat.format(date);

        return null;
    }

    public static Date getDate(final long millis) {
        return new Date(millis);
    }

    public static String getLogTime(final long millis) {
        int seconds = (int) (millis / 1000) % 60 ;
        int minutes = (int) ((millis / (1000*60)) % 60);
        int hours = (int) ((millis / (1000*60*60)) % 24);
        return hours + ":" + minutes + ":" + seconds;
    }
}
