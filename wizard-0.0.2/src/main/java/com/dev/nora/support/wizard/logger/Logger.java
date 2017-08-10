package com.dev.nora.support.wizard.logger;

import android.util.Log;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/15/2016
 */
public class Logger {
    private static boolean IS_DEBUG = true;

    public void setDebug(boolean IS_DEBUG) {
        this.IS_DEBUG = IS_DEBUG;
    }

    public static void e(String tag, String msg) {
        if (IS_DEBUG) {
            Log.e(tag, msg);
        }
    }


    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d(tag, msg);
        }
    }


    public static void v(String tag, String msg) {
        if (IS_DEBUG) {
            Log.v(tag, msg);
        }
    }


    public static void i(String tag, String msg) {
        if (IS_DEBUG) {
            Log.i(tag, msg);
        }
    }
}
