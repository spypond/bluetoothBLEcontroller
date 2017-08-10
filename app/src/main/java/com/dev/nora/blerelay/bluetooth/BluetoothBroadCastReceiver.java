package com.dev.nora.blerelay.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dev.nora.blerelay.item.BluetoothDevice;
import com.dev.nora.blerelay.service.BluetoothLeService;

import java.util.List;

/**
 * Created by nguoi on 8/20/2016.
 */
public class BluetoothBroadCastReceiver extends BroadcastReceiver {
    private OnBluetoothEvents mEvent;

    public void setBluetoothEventsCallback(OnBluetoothEvents event) {
        this.mEvent = event;
    }

    public interface OnBluetoothEvents {
        void bluetoothConnected();

        void bluetoothDisConnected();

        void serviceDiscovered();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (mEvent == null) {
            return;
        }
        switch (action) {
            case BluetoothLeService.ACTION_GATT_CONNECTED:
                mEvent.bluetoothConnected();
                break;
            case BluetoothLeService.ACTION_GATT_DISCONNECTED:
                mEvent.bluetoothDisConnected();
                break;
            case BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED:
                mEvent.serviceDiscovered();
                break;
        }
    }
}
