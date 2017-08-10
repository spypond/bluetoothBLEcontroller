package com.dev.nora.support.wizard.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/15/2016
 */
public class NetworkUtil {
    public NetworkUtil() {
    }

    public static boolean hasInternetConnection(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static boolean isOnWifi(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.getType() == 1 && info.isConnected();
    }
}
