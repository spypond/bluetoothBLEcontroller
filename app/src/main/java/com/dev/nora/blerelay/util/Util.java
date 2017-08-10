package com.dev.nora.blerelay.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Author by Duy P.Hoang.
 * <p>
 * Created Date 8/19/2016
 */
public class Util {
    public static boolean isListValid(List<?> src) {
        return src != null && src.size() > 0;
    }

    public static boolean isServiceRunning(Activity activity, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
