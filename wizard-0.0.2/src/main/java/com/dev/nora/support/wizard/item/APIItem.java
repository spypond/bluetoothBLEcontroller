package com.dev.nora.support.wizard.item;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/15/2016
 */
public class APIItem implements Parcelable, Serializable {
    private int mStatus;
    private String mMessage;


    protected APIItem(Parcel in) {
        mStatus = in.readInt();
        mMessage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mStatus);
        dest.writeString(mMessage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<APIItem> CREATOR = new Creator<APIItem>() {
        @Override
        public APIItem createFromParcel(Parcel in) {
            return new APIItem(in);
        }

        @Override
        public APIItem[] newArray(int size) {
            return new APIItem[size];
        }
    };

    public void resolveData() {
    }
}
