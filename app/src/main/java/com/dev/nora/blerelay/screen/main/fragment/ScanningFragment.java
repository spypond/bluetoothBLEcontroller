package com.dev.nora.blerelay.screen.main.fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.dev.nora.blerelay.R;
import com.dev.nora.blerelay.adapter.BLEAdapter;
import com.dev.nora.blerelay.bluetooth.BluetoothBroadCastReceiver;
import com.dev.nora.blerelay.item.BluetoothDevice;
import com.dev.nora.blerelay.screen.main.MainActivity;
import com.dev.nora.blerelay.service.BluetoothAttrs;
import com.dev.nora.blerelay.service.BluetoothLeService;
import com.dev.nora.blerelay.service.ScanningService;
import com.dev.nora.blerelay.util.Util;
import com.dev.nora.support.wizard.adapter.FlexAdapter;
import com.dev.nora.support.wizard.adapter.IFlexItem;
import com.dev.nora.support.wizard.adapter.holder.BaseViewHolder;
import com.dev.nora.support.wizard.screen.list.ListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/20/2016
 */
public class ScanningFragment extends ListFragment implements
        BluetoothBroadCastReceiver.OnBluetoothEvents {
    public static final String TAG = ScanningFragment.class.getSimpleName();
    List<BluetoothDevice> mItems;
    private int mPosition = -1;
    private boolean statusButton = false;

    /* Broadcast Receiver of bluetooth device data real time*/
    /**
     * this method receive data from ScanService.java
     */
    BroadcastReceiver mLotteryRealTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ScanningService.ACTION_SEND_DATA:
                    List<android.bluetooth.BluetoothDevice> items =
                            intent.getParcelableArrayListExtra(ScanningService.ARG_SEND_LIST_DATA);
                    if (mItems == null) {
                        mItems = new ArrayList<>();
                    }
                    mItems.clear();
                    if (Util.isListValid(items)) {
                        for (android.bluetooth.BluetoothDevice item : items) {
                            BluetoothDevice blueDev = new BluetoothDevice();
                            blueDev.setMacAddress(item.getAddress());
                            blueDev.setTitle(item.getName());
                            blueDev.setType(item.getType());
                            blueDev.setUuids(item.getUuids());
                            mItems.add(blueDev);
                        }
                    }
                    requestLoadingData(false);
                    break;
            }
        }
    };

    BluetoothBroadCastReceiver mBluetoothReceiver;


    /***
     * layout id of this fragment.
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_view;
    }


    /***
     * call is onCreateView method called.
     */
    @Override
    protected void initViews() {
        super.initViews();
        initWitchButton();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ScanningService.ACTION_SEND_DATA);
        getActivity().registerReceiver(mLotteryRealTime, intentFilter);
        mBluetoothReceiver = new BluetoothBroadCastReceiver();
        mBluetoothReceiver.setBluetoothEventsCallback(this);
        getActivity().registerReceiver(mBluetoothReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mLotteryRealTime);
        getActivity().unregisterReceiver(mBluetoothReceiver);
    }


    /**
     * create a adapter for recycler view.
     * @return
     */
    @Override
    protected FlexAdapter createAdapter() {
        return new BLEAdapter();
    }

    /**
     * this method is call when user click item of recycler view item.
     * @param v item view
     * @param holder holder of item view.
     */
    @Override
    public void onClicked(View v, BaseViewHolder holder) {
        super.onClicked(v, holder);
        mPosition = holder.getAdapterPosition();
        if (holder.getItemData() instanceof BluetoothDevice) {
            if (getActivity() instanceof MainActivity) {
                MainActivity activity = (MainActivity) getActivity();
                BluetoothDevice item = (BluetoothDevice) holder.getItemData();
                if (v.getId() == R.id.btn_on_off) {
                    controlStartStopDevice(activity,item);
                    mAdapter.notifyItemChanged(holder.getAdapterPosition());
                    return;
                }
                BluetoothLeService bluetoothLeService = activity.getBluetoothLeService();
                if (bluetoothLeService != null) {
                    if (!item.isConnect()) {
                        boolean result = bluetoothLeService.connect(item.getMacAddress());
                        Log.d(TAG, "Connect request result=" + result);
                    } else {
                        bluetoothLeService.disconnect();
                    }
                }
            }
        }
    }

    private void controlStartStopDevice(MainActivity activity, BluetoothDevice item) {
        BluetoothGattCharacteristic characteristic = findCharacteristic(BluetoothAttrs.UUID_WRITE);
        if (characteristic != null) {
            //////// you should change it with a byte of device.
            byte[] value = {-59, 6, 49, 50, 51, 52, 53, 54, 55, 56, -86};
            if (!statusButton) {///// at here, a byte array to start my bluetooth device
                statusButton = true;
                value[1]=4;
                characteristic.setValue(value);
                item.setCommand(BluetoothDevice.START_DEVICE);//update status of button on/off
            } else {////// stop my bluetooth device
                value[1]=6;
                statusButton = false;
                item.setCommand(BluetoothDevice.STOP_DEVICE);
            }
            characteristic.setValue(value);///set value for characteristic
            activity.writeCharacteristic(characteristic);//// send command to device.
        }
    }




    public static IntentFilter makeGattUpdateIntentFilter() {
        // intent filter for broadcast receiver to receive data from service.
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_WRITTEN);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        return intentFilter;
    }

    private void initWitchButton() {//// action of switch button.
        // start and stop service. in this service, stop not working becasue a hard code isRunning always true.
        final Intent intent = new Intent(mContext, ScanningService.class);
        Switch autoScan = (Switch) findViewById(R.id.switch_auto_scan);
        autoScan.setChecked(Util.isServiceRunning(getActivity(), ScanningService.class));
        autoScan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                intent.putExtra(ScanningService.ARG_STOP_SERVICE, b);
                if (b) {
                    getActivity().startService(intent);
                } else {
                    getActivity().stopService(intent);
                }
            }
        });
    }

    @Override
    protected List<? extends IFlexItem> onDataLoading() {
        /////// get data and show into recycler view with a background threads.
        if (Util.isListValid(mItems)) {
            for (BluetoothDevice item : mItems) {
                item.setBottomItemDecorator(mContext);
            }
        }
        return mItems;
    }

    @Override
    public void bluetoothConnected() {
        //// call when bluetooth device is connected
        if (Util.isListValid(mItems)) {
            BluetoothDevice item = mItems.get(mPosition);
            item.setConnect(true);
            mAdapter.notifyItemChanged(mPosition);
        }
    }

    @Override
    public void bluetoothDisConnected() {
        //// call when bluetooth device is disconnected
        if (Util.isListValid(mItems)) {
            BluetoothDevice item = mItems.get(mPosition);
            item.setConnect(false);
            mAdapter.notifyItemChanged(mPosition);
        }
    }

    @Override
    public void serviceDiscovered() {

    }


    /**
     * this method is used to find a BluetoothGattCharacteristic with a special UUID.
     * @param UUID UUID write of your device.
     * @return BluetoothGattCharacteristic if found, null if not found.
     */
    private BluetoothGattCharacteristic findCharacteristic(String UUID) {
        if (!(getActivity() instanceof MainActivity)) {
            return null;
        }
        MainActivity activity = (MainActivity) getActivity();
        List<BluetoothGattService> gattServices = activity.getSupportBluetoothGatt();
        if (Util.isListValid(gattServices)) {
            for (BluetoothGattService gattService : gattServices) {
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                if (Util.isListValid(gattCharacteristics)) {
                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                        if (gattCharacteristic.getUuid().toString().equals(UUID)) {
                            return gattCharacteristic;
                        }
                    }
                }
            }
        }
        return null;
    }
}
