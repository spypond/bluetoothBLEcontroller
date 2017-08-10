package com.dev.nora.support.wizard.item;

import android.os.Parcel;
import android.os.Parcelable;

import com.dev.nora.support.wizard.adapter.BaseFlexItem;
import com.dev.nora.support.wizard.adapter.ViewType;

/**
 * Author: Dat N. Truong<br>
 * Created date: 3/15/2016<br>
 */
public class NoDataItem extends BaseFlexItem {

    public NoDataItem() {
        mViewType = ViewType.NO_DATA;
    }

    protected NoDataItem(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<NoDataItem> CREATOR = new Parcelable.Creator<NoDataItem>() {
        @Override
        public NoDataItem createFromParcel(Parcel in) {
            return new NoDataItem(in);
        }

        @Override
        public NoDataItem[] newArray(int size) {
            return new NoDataItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
}
