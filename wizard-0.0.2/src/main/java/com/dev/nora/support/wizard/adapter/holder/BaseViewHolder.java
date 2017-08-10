package com.dev.nora.support.wizard.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.dev.nora.support.wizard.adapter.FlexAdapter;
import com.dev.nora.support.wizard.adapter.IFlexItem;


/**
 * The base view holder for all view holder that used with {@link FlexAdapter}<br>
 * Author: Dat N. Truong<br>
 * Created date: 3/15/2016<br>
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /**
     * Triggered when there is a click event occurs on the View Holder
     */
    public interface OnHolderClickedListener {

        /**
         * Triggered when there is a click event occurs on the holder
         *
         * @param v      the view that is received click event
         * @param holder the holder that occurs this event
         */
        void onClicked(View v, BaseViewHolder holder);

        /**
         * For some holders that support inner list view style (recycler view),
         * this event get triggered when there is a click action occurs on this list item
         *
         * @param parentView   the item view of this holder
         * @param parentHolder this holder
         * @param childView    the item view of the holder of the list item
         * @param childHolder  the holder of the list item
         */
        void onChildClicked(View parentView, BaseViewHolder parentHolder,
                            View childView, BaseViewHolder childHolder);
    }

    public interface OnHolderTouchListener {
        void onTouch(MotionEvent event, BaseViewHolder holder);
    }

    /**
     * Trigger when there is changed action (view bound, view recycled, ...) on this holder
     */
    public interface OnHolderChangedListener {
        /**
         * Triggered when the holder get recycled
         *
         * @param holder the holder that occurs this action
         */
        void onViewHolderRecycled(BaseViewHolder holder);

        /**
         * Triggered when the holder get bound
         *
         * @param holder the holder that occers this action
         */
        void onViewBound(BaseViewHolder holder);
    }

    protected IFlexItem mItemData;

    protected OnHolderClickedListener mClickedEvent;
    protected OnHolderTouchListener mTouchEvent;
    protected OnHolderChangedListener mHolderChangedListener;

    public BaseViewHolder(OnHolderClickedListener event, View itemView) {
        super(itemView);
        mClickedEvent = event;
        initView();
    }

    public BaseViewHolder(View itemView) {
        super(itemView);
        initView();
    }

    /**
     * Initialize the views
     */
    protected void initView() {

    }

    /**
     * When the holder get bound, this method get called to set up data and update UI. <br>
     * The implementor must call the super method
     *
     * @param item the item data that contains data and will be bound to the holder
     */
    public void setData(IFlexItem item) {
        mItemData = item;
    }

    /**
     * Triggered when the holder get recycled
     */
    public void onViewRecycled() {
        if (mHolderChangedListener != null) {
            mHolderChangedListener.onViewHolderRecycled(this);
        }
    }

    /**
     * Triggered when the holder get bound. This method get called right after the {@link #setData(IFlexItem)} method
     */
    public void onViewBound() {
        if (mHolderChangedListener != null) {
            mHolderChangedListener.onViewBound(this);
        }
    }

    public View findViewById(int id) {
        if (itemView == null) {
            return null;
        }
        return itemView.findViewById(id);
    }

    public IFlexItem getItemData() {
        return mItemData;
    }

    @Override
    public void onClick(View v) {
        if (mClickedEvent != null) {
            mClickedEvent.onClicked(v, this);
        }
    }

    protected void setOnClickedListener() {
        itemView.setOnClickListener(this);
    }

    public void setOnHolderTouchEvent(OnHolderTouchListener event) {
        mTouchEvent = event;
    }

    public boolean isUseDefaultAction() {
        if (mItemData != null) {
            return mItemData.isUseDefaultAction();
        }
        return false;
    }

    public Context getContext() {
        if (itemView != null) {
            return itemView.getContext();
        }
        return null;
    }

    public boolean isItemAnimatorEnabled() {
        return mItemData == null || mItemData.isItemAnimatorEnabled();
    }

    public OnHolderChangedListener getOnHolderChangedListener() {
        return mHolderChangedListener;
    }

    public void setOnHolderChangedListener(OnHolderChangedListener holderChangedListener) {
        mHolderChangedListener = holderChangedListener;
    }
}
