package com.dev.nora.blerelay.screen.main.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.dev.nora.blerelay.R;
import com.dev.nora.blerelay.screen.BaseFragment;
import com.dev.nora.blerelay.service.ScanningService;
import com.dev.nora.blerelay.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/19/2016
 */
public class ScanFragment extends BaseFragment {
    private MyAdapter mAdapter;
    private FrameLayout mLoadingView;
    /* Broadcast Receiver of bluetooth device data real time*/
    BroadcastReceiver mLotteryRealTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ScanningService.ACTION_SEND_DATA:
                    startLoadingData(intent, false);
                    break;
            }
        }
    };


    void startLoadingData(final Intent intent, final boolean isShow) {
        new AsyncTask<Void, Void, List<BluetoothDevice>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (mLoadingView != null && mLoadingView.getVisibility() == View.GONE && isShow) {
                    mLoadingView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected List<BluetoothDevice> doInBackground(Void... voids) {
                return intent.getParcelableArrayListExtra(ScanningService.ARG_SEND_LIST_DATA);
            }

            @Override
            protected void onPostExecute(List<BluetoothDevice> bluetoothDevices) {
                super.onPostExecute(bluetoothDevices);
                if (mLoadingView != null && mLoadingView.getVisibility() == View.VISIBLE) {
                    mLoadingView.setVisibility(View.GONE);
                }
                mAdapter.setItems(bluetoothDevices);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ScanningService.ACTION_SEND_DATA);
        getActivity().registerReceiver(mLotteryRealTime, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mLotteryRealTime);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_list_view, container, false);
        final Switch autoSwitch = (Switch) findViewById(R.id.switch_auto_scan);
        final Intent intent = new Intent(mContext, ScanningService.class);
        autoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        initViews();
        return mRootView;
    }


    protected void initViews() {
        initRecyclerView();
        initOtherView();
    }

    private void initOtherView() {
        mLoadingView = (FrameLayout) findViewById(R.id.lo_loading_view);
    }

    protected void initRecyclerView() {
        mAdapter = new MyAdapter();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);
    }


    private class MyAdapter extends RecyclerView.Adapter<ScanViewHolder> {
        private List<BluetoothDevice> mBluetoothDevices;

        @Override
        public ScanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ScanViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.holder_bluetooth_device, parent, false));
        }

        @Override
        public void onBindViewHolder(ScanViewHolder holder, int position) {
            holder.setData(mBluetoothDevices.get(position));
        }

        @Override
        public int getItemCount() {
            return mBluetoothDevices == null ? 0 : mBluetoothDevices.size();
        }

        public void setItems(List<BluetoothDevice> items) {
            if (mBluetoothDevices == null) {
                mBluetoothDevices = new ArrayList<>();
            }
            mBluetoothDevices.clear();
            if (Util.isListValid(items)) {
                mBluetoothDevices.addAll(items);
            }
            notifyDataSetChanged();
        }
    }


    public class ScanViewHolder extends RecyclerView.ViewHolder {
        BluetoothDevice mBlueToothDevice;
        TextView mTvMacAddress, mTvName;

        public ScanViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.device_name);
            mTvMacAddress = (TextView) itemView.findViewById(R.id.device_address);
        }

        public void setData(BluetoothDevice object) {
            this.mBlueToothDevice = object;
            if (object != null) {
                String name = "Unknown Name";
                if (object.getName() != null) {
                    name = object.getName();
                }
                mTvName.setText(name);
                mTvMacAddress.setText(object.getAddress());
            }
        }
    }
}

