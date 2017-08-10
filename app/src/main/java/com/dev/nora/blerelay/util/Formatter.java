package com.dev.nora.blerelay.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author by Duy P.Hoang.
 * <p>
 * Created Date 8/19/2016
 */
public class Formatter {

    public static String formatShortTime(long time) {
        long diff = System.currentTimeMillis() - time * 1000L;
        return diff - 172800000L > 0L ? formatDate(time * 1000L, "dd/MM/yyyy") : (diff - 86400000L > 0L ? "Hôm qua" : (diff - 3600000L > 0L ? String.format("%d giờ trước", new Object[]{Long.valueOf(diff / 3600000L)}) : (diff - 60000L > 0L ? String.format("%d phút trước", new Object[]{Long.valueOf(diff / 60000L)}) : "Mới đăng")));
    }

    public static String formatDate(long time, String format) {
        return (new SimpleDateFormat(format, Locale.getDefault())).format(new Date(time));
    }
}
