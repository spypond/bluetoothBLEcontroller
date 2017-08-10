package com.dev.nora.blerelay.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dev.nora.blerelay.R;
import com.dev.nora.blerelay.adapter.holder.DisplayBluetoothDeviceViewHolder;
import com.dev.nora.support.wizard.adapter.FlexAdapter;
import com.dev.nora.support.wizard.adapter.holder.BaseViewHolder;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/20/2016
 */
public class BLEAdapter extends FlexAdapter {

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = super.onCreateViewHolder(parent, viewType);
        switch (viewType) {
            case ViewType.VIEW_TYPE_BLUETOOTH_ITEM:
                return new DisplayBluetoothDeviceViewHolder(getOnHolderClickedListener(),
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_bluetooth_device, parent, false));
        }
        return holder;
    }
}
