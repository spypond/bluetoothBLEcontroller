package com.dev.nora.blerelay.screen;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dev.nora.blerelay.service.ScanningService;

import java.util.List;

/**
 * Author by Duy P.Hoang.
 * <p>
 * Created Date 8/19/2016
 */
public class BaseFragment extends Fragment {
    protected View mRootView;
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    protected View findViewById(int id) {
        if (mRootView != null) {
            return mRootView.findViewById(id);
        }
        return null;
    }
}
