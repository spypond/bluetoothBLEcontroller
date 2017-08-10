package com.dev.nora.support.wizard.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/15/2016
 */
public class SharePrefUtil {
    private static String PF_NAME = "temp";

    public SharePrefUtil() {
    }

    public static void saveIntTempPref(Context context, String name, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PF_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public static int readIntTempPref(Context context, String name, int def) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PF_NAME, 0);
        int val = sharedPreferences.getInt(name, def);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(name);
        editor.apply();
        return val;
    }

    public static void saveStringTempPref(Context context, String name, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PF_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static String readStringTempPref(Context context, String name, String def) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PF_NAME, 0);
        String val = sharedPreferences.getString(name, def);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(name);
        editor.apply();
        return val;
    }

    public static void saveBooleanPref(Context context, String name, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PF_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public static String readStringPref(Context context, String name, String def) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PF_NAME, 0);
        return sharedPreferences.getString(name, def);
    }

    public static boolean readBooleanPref(Context context, String name, boolean def) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PF_NAME, 0);
        return sharedPreferences.getBoolean(name, def);
    }
}
