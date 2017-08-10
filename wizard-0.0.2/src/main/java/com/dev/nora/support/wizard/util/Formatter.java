package com.dev.nora.support.wizard.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/15/2016
 */
public class Formatter {
    public static long getCurrentDate() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static long getTheDayBefore() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTimeInMillis();
    }

    public static long getLast7Day() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        return cal.getTimeInMillis();
    }

    public static long getLast30Day() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        return cal.getTimeInMillis();
    }

    public static int getCurrentDayInWeek() {
        return getDayInWeek(getCurrentDate());
    }

    public static int getDayInWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static String formatShortTime(long time) {
        long diff = System.currentTimeMillis() - time * 1000L;
        return diff - 172800000L > 0L?formatDate(time * 1000L, "dd/MM/yyyy"):(diff - 86400000L > 0L
                ?"Hôm qua":(diff - 3600000L > 0L?String.format("%d giờ trước", new Object[]{Long.valueOf(diff / 3600000L)})
                :(diff - 60000L > 0L?String.format("%d phút trước", new Object[]{Long.valueOf(diff / 60000L)}):"Mới đăng")));
    }

    public static String formatDate(long time, String format) {
        return (new SimpleDateFormat(format, Locale.getDefault())).format(new Date(time));
    }

    public static String mapToString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Object o : params.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode((String) entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
