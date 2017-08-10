package com.dev.nora.blerelay.screen.main;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dev.nora.blerelay.R;
import com.dev.nora.blerelay.screen.BaseActivity;
import com.dev.nora.blerelay.screen.main.fragment.ScanFragment;
import com.dev.nora.blerelay.screen.main.fragment.ScanningFragment;
import com.dev.nora.blerelay.service.BluetoothLeService;
import com.dev.nora.blerelay.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private BluetoothLeService mBluetoothLeService;
    private final ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");

            }
            // Automatically connects to the device upon successful start-up initialization.
            //mBluetoothLeService.connect("84:DD:20:EB:B3:C2");
            //Toast.makeText(getApplicationContext(),"Please waiting...",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, connection, BIND_AUTO_CREATE);
        replaceFragment(R.id.container, new ScanningFragment());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    public BluetoothLeService getBluetoothLeService() {
        return mBluetoothLeService;
    }

    private void replaceFragment(int resource, Fragment fragment) {
        // Create new fragment and transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(resource, fragment);
        // Commit the transaction
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<BluetoothGattService> getSupportBluetoothGatt() {
        if (mBluetoothLeService != null) {
            return mBluetoothLeService.getSupportedGattServices();
        }
        return null;
    }


    /**
     * write a byte code to device with a characteristic.
     * @param characteristic
     */
    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        mBluetoothLeService.writeCharacteristic(characteristic);
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        mBluetoothLeService.setCharacteristicNotification(characteristic, enabled);
    }

}
