package com.dev.nora.blerelay.adapter.holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dev.nora.blerelay.R;
import com.dev.nora.blerelay.item.BluetoothDevice;
import com.dev.nora.support.wizard.adapter.IFlexItem;
import com.dev.nora.support.wizard.adapter.holder.BaseViewHolder;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/20/2016
 */
public class DisplayBluetoothDeviceViewHolder extends BaseViewHolder {
    TextView mTvMacAddress, mTvName, mTvStatus;
    Button mOnOffButton;

    public DisplayBluetoothDeviceViewHolder(OnHolderClickedListener event, View itemView) {
        super(event, itemView);
        setOnClickedListener();
    }

    @Override
    protected void initView() {
        super.initView();
        mTvName = (TextView) itemView.findViewById(R.id.device_name);
        mTvMacAddress = (TextView) itemView.findViewById(R.id.device_address);
        mTvStatus = (TextView) findViewById(R.id.tv_status);
        mOnOffButton = (Button) findViewById(R.id.btn_on_off);
        mOnOffButton.setOnClickListener(this);
    }

    @Override
    public void setData(IFlexItem item) {
        super.setData(item);
        if (item instanceof BluetoothDevice) {
            BluetoothDevice data = (BluetoothDevice) item;
            String name = "Unknown Name";
            String mac = "N/A";
            if (data.getMacAddress() != null) {
                mac = data.getMacAddress();
            }
            if (data.getTitle() != null) {
                name = data.getTitle();
            }
            mTvName.setText(name);
            mTvMacAddress.setText(String.format(getContext().getString(R.string.mac_address), mac));
            mTvStatus.setText(getContext().getString(data.isConnect() ? R.string.connected : R.string.disconnected));
            mOnOffButton.setText(getContext().getString(isStartDevice(data) ? R.string.on : R.string.off));
        }
    }


    private boolean isStartDevice(BluetoothDevice item) {
        return item.getCommand() == BluetoothDevice.START_DEVICE;
    }
}
