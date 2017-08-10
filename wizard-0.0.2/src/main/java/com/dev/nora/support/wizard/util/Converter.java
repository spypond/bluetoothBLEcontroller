package com.dev.nora.support.wizard.util;

import android.content.Context;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/15/2016
 */
public class Converter {
    public Converter() {
    }

    public static float dpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
