package com.dev.nora.blerelay.item;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattService;
import android.os.ParcelUuid;

import com.dev.nora.blerelay.adapter.ViewType;
import com.dev.nora.support.wizard.adapter.BaseFlexItem;

import java.util.List;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/20/2016
 */
@SuppressLint("ParcelCreator")
public class BluetoothDevice extends BaseFlexItem {
    private String mMacAddress;
    private ParcelUuid[] mUuids;
    private int mType;
    private boolean isConnect = false;
    private int mCommand;

    public static final int START_DEVICE = 100;
    public static final int STOP_DEVICE = 101;


    List<BluetoothGattService> mBluetoothGattServices;

    public BluetoothDevice() {
        setViewType(ViewType.VIEW_TYPE_BLUETOOTH_ITEM);
    }

    public void setMacAddress(String macAddress) {
        this.mMacAddress = macAddress;
    }

    public String getMacAddress() {
        return mMacAddress;
    }

    public void setUuids(ParcelUuid[] mUuids) {
        this.mUuids = mUuids;
    }

    public ParcelUuid[] getUuids() {
        return mUuids;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public int getType() {
        return mType;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setBluetoothGattServices(List<BluetoothGattService> mBluetoothGattServices) {
        this.mBluetoothGattServices = mBluetoothGattServices;
    }

    public List<BluetoothGattService> getBluetoothGattServices() {
        return mBluetoothGattServices;
    }

    public void setCommand(int mCommand) {
        this.mCommand = mCommand;
    }

    public int getCommand() {
        return mCommand;
    }
}
