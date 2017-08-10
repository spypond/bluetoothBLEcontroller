package com.dev.nora.support.wizard.adapter.decoration;

import android.os.Parcel;
import android.os.Parcelable;

public class FlexItemDecorator implements Parcelable{

    private int mMarginLeft;
    private int mMarginRight;
    private int mMarginTop;
    private int mMarginBottom;

    private int mDrawableId;
    private boolean mUseCharacterIcon;

    public FlexItemDecorator() {

    }

    public FlexItemDecorator(int left, int top, int right, int bottom) {
        mMarginLeft = left;
        mMarginTop = top;
        mMarginRight = right;
        mMarginBottom = bottom;
    }

    protected FlexItemDecorator(Parcel in) {
        mMarginLeft = in.readInt();
        mMarginRight = in.readInt();
        mMarginTop = in.readInt();
        mMarginBottom = in.readInt();
        mDrawableId = in.readInt();
        mUseCharacterIcon = in.readByte() != 0;
    }

    public static final Creator<FlexItemDecorator> CREATOR = new Creator<FlexItemDecorator>() {
        @Override
        public FlexItemDecorator createFromParcel(Parcel in) {
            return new FlexItemDecorator(in);
        }

        @Override
        public FlexItemDecorator[] newArray(int size) {
            return new FlexItemDecorator[size];
        }
    };

    public int getMarginLeft() {
        return mMarginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.mMarginLeft = marginLeft;
    }

    public int getMarginRight() {
        return mMarginRight;
    }

    public void setMarginRight(int marginRight) {
        this.mMarginRight = marginRight;
    }

    public int getMarginTop() {
        return mMarginTop;
    }

    public void setMarginTop(int marginTop) {
        this.mMarginTop = marginTop;
    }

    public int getMarginBottom() {
        return mMarginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        this.mMarginBottom = marginBottom;
    }

    public int getDrawableId() {
        return mDrawableId;
    }

    /**
     * Set a drawable id as icon for this item.
     * Note that this id will be ignored if the valid small thumb url has been set.
     * <br>
     * The order of icon is:<br>
     * 1. Small thumb url<br>
     * 2. Drawable id<br>
     * 3. First character
     *
     * @param drawableId the drawable id to set
     */
    public void setDrawableId(int drawableId) {
        mDrawableId = drawableId;
    }

    public boolean isUseCharacterIcon() {
        return mUseCharacterIcon;
    }

    /**
     * For the thumb of an item that expect to use the first character of its name to represent an icon.
     * Set this flag to true<br>
     * <b>Note: If the item that has a valid small thumb url, this flag will be ignored</b>
     *
     * @param useCharacterIcon true to use the first character as an icon, false to use the drawable id
     */
    public void setUseCharacterIcon(boolean useCharacterIcon) {
        mUseCharacterIcon = useCharacterIcon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mMarginLeft);
        parcel.writeInt(mMarginRight);
        parcel.writeInt(mMarginTop);
        parcel.writeInt(mMarginBottom);
        parcel.writeInt(mDrawableId);
        parcel.writeByte((byte) (mUseCharacterIcon ? 1 : 0));
    }
}
