package com.dev.nora.blerelay.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.dev.nora.blerelay.screen.main.MainActivity;
import com.dev.nora.blerelay.R;
import com.dev.nora.support.wizard.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/18/2016
 */
public class ScanningService extends Service {
    private static final String TAG = ScanningService.class.getSimpleName();
    private static final long TIME_INTERVAL = 1000 * 30;//30 seconds
    private static final long TIME_RESET_INTERVAL = 1000 * 2;//30 seconds
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 1000 * 10;
    private BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDevice> mBluetoothDevices;
    private List<BluetoothDevice> mStoreDevice;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    //    private Handler handler = new Handler(getMainLooper());
    private boolean isRunning = false;
    private Thread mBackgroundThread;

    public static final String ACTION_SEND_DATA = "com.dev.nora.blerelay.service.action_send_data";
    public static final String ARG_SEND_LIST_DATA = "com.dev.nora.blerelay.service.arg_send_data";
    public static final String ARG_STOP_SERVICE = "arg_stop_service";

    @Override
    public void onCreate() {
        super.onCreate();
        mBluetoothDevices = new ArrayList<>();
        mStoreDevice = new ArrayList<>();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mStoreDevice != null) {
                    mStoreDevice.clear();
                }
            }
        }, TIME_RESET_INTERVAL);
        mBackgroundThread = new Thread(mRunnable);
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        this.mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter != null && mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
            mBluetoothAdapter.enable();
        }
    }

    private void storeDevice(BluetoothDevice device) {
        if (mStoreDevice == null) {
            mStoreDevice = new ArrayList<>();
        }
        if (!mStoreDevice.contains(device)) {
            mStoreDevice.add(device);
            showNotification("You are now near the relay!! do something!");
        }
    }

    private Runnable mRunnable = new Runnable() {
        @SuppressWarnings("InfiniteLoopStatement")
        @Override
        public void run() {
            do {
                try {
                    scan();
                    Log.v("looping", "looping now");
                    Thread.sleep(TIME_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (isRunning);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        loop();
        return START_STICKY;
    }

    private void loop() {
        boolean hasSystemFeature = getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        boolean bluetoothAvailable = mBluetoothAdapter != null;
        if (!hasSystemFeature || !bluetoothAvailable) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            stopSelf();
            return;
        }

        if (!mBackgroundThread.isAlive()) {
            mBackgroundThread.start();
        }
        showNotification();
    }

    @SuppressWarnings("deprecation")
    private void scan() {
        if (isRunning) {
            if (Build.VERSION.SDK_INT >= 21) {
                mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                filters = new ArrayList<>();
            }
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            }, SCAN_PERIOD);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        Thread.sleep(SCAN_PERIOD);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (mLEScanner == null) {
                            return;
                        }
                        if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                            mLEScanner.stopScan(mScanCallback);
                        }
                    } else {
                        if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        }
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                if (mLEScanner == null || mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                    return;
                }
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                        mLEScanner.startScan(filters, settings, mScanCallback);
                    }
                }
            }
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            } else {
                if (mLEScanner == null || mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                    return;
                }
                if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    mLEScanner.stopScan(mScanCallback);
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = false;
        if (mBluetoothAdapter != null && mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
            mBluetoothAdapter.disable();
        }
        showNotification();
        scan();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("NewApi")
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i("callbackType", String.valueOf(callbackType));
            Log.i("result", result.toString());

            BluetoothDevice btDevice = result.getDevice();
            if (!isExisted(btDevice)) {
                mBluetoothDevices.add(btDevice);
            }
            storeDevice(btDevice);
            sendData();
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (!isExisted(device)) {
                mBluetoothDevices.add(device);
            }
            storeDevice(device);
        }
    };


    private boolean isExisted(BluetoothDevice device) {
        return mBluetoothDevices.contains(device);
    }


    private void sendData() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION_SEND_DATA);
        broadcastIntent.putParcelableArrayListExtra(ARG_SEND_LIST_DATA,
                (ArrayList<? extends Parcelable>) mBluetoothDevices);
        sendBroadcast(broadcastIntent);
    }


    private void showNotification() {
        // Send Notification
        String notificationTitle = "Auto scan";
        String notificationText = "Running";
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText).setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags = notification.flags
                | Notification.FLAG_ONGOING_EVENT;
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if (isRunning) {
            notificationManager.notify(0, notification);
        } else {
            notificationManager.cancel(0);
        }
    }


    private void showNotification(String content) {
        // Send Notification
        String notificationTitle = "Relay available now";

        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(notificationTitle)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if (isRunning) {
            notificationManager.notify(1, notification);
        } else {
            notificationManager.cancel(1);
        }
    }

}
