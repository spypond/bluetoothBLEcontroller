package com.dev.nora.support.wizard.adapter;

import android.content.Context;
import android.os.Parcel;

import com.dev.nora.support.wizard.adapter.decoration.FlexItemDecorator;
import com.dev.nora.support.wizard.util.Converter;
import com.dev.nora.support.wizard.util.Formatter;

/**
 * The basic implementor of {@link IFlexItem}<br>
 * <p/>
 * Created by DAT on 11/21/2015.
 */
public class BaseFlexItem implements IFlexItem {

    protected String mId;
    protected String mTitle;
    protected String mDesc;
    protected String mThumbBig;
    protected String mThumbMedium;
    protected String mThumbSmall;
    protected long mCreatedDate;
    protected long mUpdatedDate;
    protected int mViewType;
    protected FlexItemDecorator mItemDecorator;
    protected String mSource;
    protected boolean mUseDefaultAction;
    protected boolean mItemAnimatorEnabled;

    public BaseFlexItem() {

    }

    protected BaseFlexItem(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mDesc = in.readString();
        mThumbBig = in.readString();
        mThumbMedium = in.readString();
        mThumbSmall = in.readString();
        mCreatedDate = in.readLong();
        mUpdatedDate = in.readLong();
        mViewType = in.readInt();
        mSource = in.readString();
        mUseDefaultAction = in.readByte() == 1;
        mItemAnimatorEnabled = in.readByte() == 1;
        mItemDecorator = in.readParcelable(FlexItemDecorator.class.getClassLoader());
    }

    public static final Creator<BaseFlexItem> CREATOR = new Creator<BaseFlexItem>() {
        @Override
        public BaseFlexItem createFromParcel(Parcel in) {
            return new BaseFlexItem(in);
        }

        @Override
        public BaseFlexItem[] newArray(int size) {
            return new BaseFlexItem[size];
        }
    };

    public void setDefaultItemDecorator(Context context) {
        int margin = (int) Converter.dpToPx(context, 12);
        mItemDecorator = new FlexItemDecorator(margin, margin, margin, margin);
    }

    public void setDefaultItemDecorator(int... position) {
        mItemDecorator = new FlexItemDecorator(position[0], position[1], position[2], position[3]);
    }

    public void setBottomItemDecorator(Context context) {
        int margin = (int) Converter.dpToPx(context, 12);
        mItemDecorator = new FlexItemDecorator(0, 0, 0, margin);
    }

    public void setTopItemDecorator(Context context) {
        int margin = (int) Converter.dpToPx(context, 12);
        mItemDecorator = new FlexItemDecorator(0, margin, 0, 0);
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getDesc() {
        return mDesc;
    }

    @Override
    public int getViewType() {
        return mViewType;
    }

    @Override
    public void setViewType(int viewType) {
        mViewType = viewType;
    }

    @Override
    public String getThumbSmall() {
        return mThumbSmall;
    }

    @Override
    public String getThumbMedium() {
        return mThumbMedium;
    }

    @Override
    public String getThumbBig() {
        return mThumbBig;
    }

    @Override
    public long getCreatedDate() {
        return mCreatedDate;
    }

    public String getFormattedCreatedDate() {
        return getFormattedDate(mCreatedDate);
    }

    @Override
    public long getUpdatedDate() {
        return mUpdatedDate;
    }

    public String getFormattedUpdatedDate() {
        return getFormattedDate(mUpdatedDate);
    }

    @Override
    public String getFormattedDate(long time) {
        return Formatter.formatShortTime(time);
    }

    @Override
    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    @Override
    public FlexItemDecorator getItemDecorator() {
        return mItemDecorator;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mDesc);
        dest.writeString(mThumbBig);
        dest.writeString(mThumbMedium);
        dest.writeString(mThumbSmall);
        dest.writeLong(mCreatedDate);
        dest.writeLong(mUpdatedDate);
        dest.writeInt(mViewType);
        dest.writeString(mSource);
        dest.writeByte((byte) (mUseDefaultAction ? 1 : 0));
        dest.writeByte((byte) (mItemAnimatorEnabled ? 1 : 0));
        dest.writeParcelable(mItemDecorator, flags);
    }

    public void setId(String id) {
        mId = id;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    public void setThumbBig(String thumbBig) {
        mThumbBig = thumbBig;
    }

    public void setThumbMedium(String thumbMedium) {
        mThumbMedium = thumbMedium;
    }

    public void setThumbSmall(String thumbSmall) {
        mThumbSmall = thumbSmall;
    }

    public void setCreatedDate(long createdDate) {
        mCreatedDate = createdDate;
    }

    public void setUpdatedDate(long updatedDate) {
        mUpdatedDate = updatedDate;
    }

    public void setItemDecorator(FlexItemDecorator itemDecorator) {
        mItemDecorator = itemDecorator;
    }

    public boolean isUseDefaultAction() {
        return mUseDefaultAction;
    }

    public void setUseDefaultAction(boolean useDefaultAction) {
        mUseDefaultAction = useDefaultAction;
    }

    @Override
    public boolean isItemAnimatorEnabled() {
        return mItemAnimatorEnabled;
    }

    public void setItemAnimatorEnabled(boolean itemAnimatorEnabled) {
        mItemAnimatorEnabled = itemAnimatorEnabled;
    }
}
