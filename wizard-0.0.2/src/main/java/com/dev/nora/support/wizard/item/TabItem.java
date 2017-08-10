package com.dev.nora.support.wizard.item;

public class TabItem {

    private int mId;
    private String mName;
    private int mDrawableId;

    public TabItem(int id, String name, int drawable) {
        mId = id;
        mName = name;
        mDrawableId = drawable;
    }

    public TabItem(int id, String name) {
        mId = id;
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getDrawableId() {
        return mDrawableId;
    }

    public void setDrawableId(int drawableId) {
        mDrawableId = drawableId;
    }
}
