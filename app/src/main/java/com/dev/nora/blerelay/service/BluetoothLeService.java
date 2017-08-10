package com.dev.nora.blerelay.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by nguoi on 8/20/2016.
 */
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public static final String ACTION_DATA_AVAILABLE = "com.dev.nora.blerelay.service.le.ACTION_DATA_AVAILABLE";
    public static final String ACTION_GATT_WRITTEN = "com.dev.nora.blerelay.service.le.ACTION_DATA_WRITEN";
    public static final String ACTION_GATT_CONNECTED = "com.dev.nora.blerelay.service.le.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_CONNECTING = "com.example.bluetooth.le.ACTION_GATT_CONNECTING";
    public static final String ACTION_GATT_DISCONNECTED = "com.dev.nora.blerelay.service.le.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.dev.nora.blerelay.service.le.ACTION_GATT_SERVICES_DISCOVERED";
    public static final String EXTRA_DATA = "com.dev.nora.blerelay.service.le.EXTRA_DATA";
    public static final String EXTRA_LIST_GATT_DATA = "com.dev.nora.blerelay.service.le.EXTRA_LIST_GATT_DATA";
    public static final String EXTRA_WRITTEN = "ccom.dev.nora.blerelay.service.le.EXTRA_WRITTEN";

    public final static UUID UUID_SPPLE_NOTIFY;
    //\public final static UUID UUID_SPPLE_WRITTEN;
    public final static UUID UUID_SPPLE_SERV;

    static {
        UUID_SPPLE_NOTIFY = UUID.fromString(BluetoothAttrs.UUID_NOTIFY);
        //UUID_SPPLE_WRITTEN = UUID.fromString(BluetoothGattAttributes.SPPLE_WRITE);
        UUID_SPPLE_SERV = UUID.fromString(BluetoothAttrs.UUID_SERVICE);
    }

    private final IBinder mBinder = new LocalBinder();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        return true;
    }


    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);

    }

    /**
     * Request a write on a given {@code BluetoothGattCharacteristic}. The write result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicWrite(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to write from.
     */
    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        }

        final StringBuilder stringBuilder = new StringBuilder(characteristic.getValue().length);

        for (byte byteChar : characteristic.getValue()) {
            stringBuilder.append(String.format("%02X ", byteChar)).toString();
        }
        Log.v(TAG, stringBuilder.toString());
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    public void writeBytes(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        }

        Log.e(TAG, "characteristic");
        mBluetoothGatt.writeCharacteristic(characteristic);
    }


    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;
        return mBluetoothGatt.getServices();
    }

    public BluetoothDevice getDevice(String address) {
        return mBluetoothAdapter.getRemoteDevice(address);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                UUID.fromString(BluetoothAttrs.CLIENT_CHARACTERISTIC_CONFIG));
        if (descriptor == null) Log.v(TAG, "client Config is null");
        // This is specific to Heart Rate Measurement.
        if (enabled) {
            Log.i(TAG, "enable notification");
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        } else {
            Log.i(TAG, "disable notification");
            descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
        mBluetoothGatt.writeDescriptor(descriptor);
    }

    ///////////////////////////////callback of bluetooth///////////////////////////////////////////
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            String intentAction;
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    intentAction = ACTION_GATT_CONNECTED;
                    mConnectionState = STATE_CONNECTED;
                    broadcastUpdate(intentAction);
                    Log.i(TAG, "Attempting to start service discovery:" +
                            mBluetoothGatt.discoverServices());
                    Log.i(TAG, "Connected to GATT server.");
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    intentAction = ACTION_GATT_DISCONNECTED;
                    mConnectionState = STATE_DISCONNECTED;
                    Log.i(TAG, "Disconnected from GATT server.");
                    broadcastUpdate(intentAction);
                    break;
                default:
                    intentAction = ACTION_GATT_CONNECTING;
                    mConnectionState = STATE_CONNECTING;
                    Toast.makeText(getApplicationContext(), "Connecting", Toast.LENGTH_SHORT).show();
                    broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            switch (status) {
                case BluetoothGatt.GATT_SUCCESS:
                    ArrayList<BluetoothGattService> ListGatt = new ArrayList<>(getSupportedGattServices());
                    broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED, ListGatt);
                    break;
                default:
                    Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            switch (status) {
                case BluetoothGatt.GATT_SUCCESS:
                    broadcastUpdate(ACTION_GATT_WRITTEN, characteristic);
                    Log.w(TAG, "onServicesWritten with characteristic: " + characteristic.getUuid());
                    break;
                default:
                    Log.w(TAG, "onServicesWritten with characteristic failed: " + status);
            }
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }


    private void broadcastUpdate(final String action, ArrayList<BluetoothGattService> gattServices) {
        final Intent intent = new Intent(action);
        com.dev.nora.blerelay.item.BluetoothDevice device = new com.dev.nora.blerelay.item.BluetoothDevice();
        device.setBluetoothGattServices(getSupportedGattServices());
        intent.putExtra(EXTRA_LIST_GATT_DATA, (Parcelable) device);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        // region This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (UUID_SPPLE_NOTIFY.equals(characteristic.getUuid())) {
            byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                intent.putExtra(EXTRA_DATA, data);
            }
        }else if(UUID_SPPLE_NOTIFY.equals(characteristic.getUuid())){
            byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                intent.putExtra(EXTRA_WRITTEN, data);
            }
        }

        //endregion
        sendBroadcast(intent);
    }

}
