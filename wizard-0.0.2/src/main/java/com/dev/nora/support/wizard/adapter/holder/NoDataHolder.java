package com.dev.nora.support.wizard.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.dev.nora.support.wizard.R;
import com.dev.nora.support.wizard.adapter.IFlexItem;
import com.dev.nora.support.wizard.item.NoDataItem;


/**
 * Author: Dat N. Truong<br>
 * Created date: 3/15/2016<br>
 */
public class NoDataHolder extends BaseViewHolder {

    private TextView mTvDesc;

    public NoDataHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView() {
        super.initView();
        mTvDesc = (TextView) findViewById(R.id.tv_desc);
    }

    @Override
    public void setData(IFlexItem item) {
        super.setData(item);
        if (item instanceof NoDataItem) {
            NoDataItem data = (NoDataItem) item;
            if (data.getDesc() != null && data.getDesc().length() > 0) {
                mTvDesc.setText(item.getDesc());
            }
        }
    }
}
