package com.dev.nora.support.wizard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.nora.support.wizard.R;
import com.dev.nora.support.wizard.adapter.holder.BaseViewHolder;
import com.dev.nora.support.wizard.adapter.holder.NoDataHolder;
import com.dev.nora.support.wizard.item.NoDataItem;
import com.dev.nora.support.wizard.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;


/**
 * An adapter that works with a list of {@link IFlexItem} items and the recycler view.<br>
 * Created by DAT on 11/21/2015.
 */
public class FlexAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<IFlexItem> mItems;

    private BaseViewHolder.OnHolderClickedListener mOnHolderClickedListener;
    private BaseViewHolder.OnHolderTouchListener mOnHolderTouchListener;
    private BaseViewHolder.OnHolderChangedListener mOnHolderChangedListener;

    private NoDataItem mEmptyItem;
    private BaseViewHolder mParentViewHolder;
    private View mParentView;

    public List<IFlexItem> getItems() {
        return mItems;
    }

    public void setOnHolderClickedListener(BaseViewHolder.OnHolderClickedListener event) {
        mOnHolderClickedListener = event;
    }

    public BaseViewHolder.OnHolderClickedListener getOnHolderClickedListener() {
        return mOnHolderClickedListener;
    }

    public void setOnHolderTouchListener(BaseViewHolder.OnHolderTouchListener event) {
        mOnHolderTouchListener = event;
    }

    public BaseViewHolder.OnHolderTouchListener getOnHolderTouchListener() {
        return mOnHolderTouchListener;
    }

    public BaseViewHolder.OnHolderChangedListener getOnHolderChangedListener() {
        return mOnHolderChangedListener;
    }

    public void setOnHolderChangedListener(BaseViewHolder.OnHolderChangedListener onHolderChangedListener) {
        mOnHolderChangedListener = onHolderChangedListener;
    }

    public void setParentViewHolder(BaseViewHolder mParentViewHolder) {
        this.mParentViewHolder = mParentViewHolder;
    }

    public void setParentView(View mParentView) {
        this.mParentView = mParentView;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ViewType.NO_DATA:
                return new NoDataHolder(LayoutInflater.
                        from(parent.getContext()).inflate(R.layout.holder_no_data, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        final IFlexItem item = mItems.get(position);
        holder.setData(item);
        holder.onViewBound();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }

    /**
     * Set items for the adapter. Using this method, the old items will be cleared
     *
     * @param src the new list of IFlexItem items
     */
    public void setItems(List<? extends IFlexItem> src) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        } else {
            mItems.clear();
        }
        if (Util.isListValid(src)) {
            mItems.addAll(src);
        }
        makeSureListOk();
        notifyDataSetChanged();
    }

    /**
     * Add an item to the existing adapter at a specific position
     *
     * @param flexItem the item to add
     * @param index    the position
     */
    public void addItem(IFlexItem flexItem, int index) {
        if (hasEmptyItemOnly()) {
            mItems.clear();
            notifyDataSetChanged();
        }
        if (flexItem == null) {
            return;
        }
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        if (index < 0) {
            index = 0;
        }
        if (index >= mItems.size()) {
            index = mItems.size();
        }
        mItems.add(index, flexItem);
        notifyItemInserted(index);
    }

    /**
     * Add a list of IFlexItem items to the existing adapter. Unlike {@link #setItems(List)},
     * using this method does not clear the old data but adding to the tail of the current list
     *
     * @param src the new items to add
     */
    public void addItems(List<? extends IFlexItem> src) {
        if (hasEmptyItemOnly()) {
            mItems.clear();
            notifyDataSetChanged();
        }
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        int oldIndex = mItems.size();
        int index = 0;
        if (Util.isListValid(src)) {
            mItems.addAll(src);
            index = src.size();
        } else {
            makeSureListOk();
            index = mItems.size();
        }
        notifyItemRangeInserted(oldIndex, index);
    }

    private void makeSureListOk() {
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        if (mItems.size() < 1) {
            if (mEmptyItem == null) {
                mItems.add(new NoDataItem());
            } else {
                mItems.add(mEmptyItem);
            }
        }
    }

    public void setEmptyItem(NoDataItem emptyItem) {
        mEmptyItem = emptyItem;
    }

    /**
     * Call this method to make sure the items valid and has at least 1 item when it's empty
     */
    public void makeListFine() {
        if (Util.isListValid(mItems)) {
            return;
        }
        makeSureListOk();
        notifyDataSetChanged();
    }

    public void clear() {
        if (Util.isListValid(mItems)) {
            mItems.clear();
            notifyDataSetChanged();
        }
    }

    public void clearOnly() {
        if (Util.isListValid(mItems)) {
            mItems.clear();
        }
    }

    public void removeItem(int index) {
        if (Util.isListValid(mItems)) {
            mItems.remove(index);
            notifyItemRemoved(index);
        }
    }

    public IFlexItem getItem(int index) {
        if (Util.isListValid(mItems)) {
            return mItems.get(index);
        }
        return null;
    }

    /**
     * Undo the items from a map of data. Useful for some kind of undo function
     *
     * @param src
     */
    public void undo(SortedMap<Integer, ? extends IFlexItem> src) {
        if (src != null && src.size() > 0) {
            for (Integer index : src.keySet()) {
                IFlexItem item = src.get(index);
                if (item != null) {
                    mItems.add(index, item);
                    notifyItemInserted(index);
                }
            }
        }
    }

    public boolean hasEmptyItemOnly() {
        return mItems != null &&
                mItems.size() == 1 &&
                mItems.get(0) instanceof NoDataItem;
    }

    public void requestRebindHolder(int type) {
        if (mItems != null && mItems.size() > 0) {
            for (int i = 0; i < mItems.size(); i++) {
                int viewType = getItemViewType(i);
                if (viewType == type) {
                    notifyItemChanged(i);
                }
            }
        }
    }

    public List<IFlexItem> getItemByViewType(int type) {
        if (Util.isListValid(mItems)) {
            List<IFlexItem> items = new ArrayList<>();
            for (IFlexItem iFlexItem : mItems) {
                int viewType = iFlexItem.getViewType();
                if (viewType == type) {
                    items.add(iFlexItem);
                }
            }
            return items;
        }
        return null;
    }

    public int findItem(IFlexItem item) {
        if (Util.isListValid(mItems)) {
            int index = 0;
            for (IFlexItem iFlexItem : mItems) {
                if (item.equals(iFlexItem)) {
                    return index;
                }
                index++;
            }
            return -1;
        }
        return -1;
    }

    public int getItemCount(Class type) {
        if (Util.isListValid(mItems)) {
            int index = 0;
            for (IFlexItem item : mItems) {
                if (type.isInstance(item)) {
                    index++;
                }
            }
            return index;
        }
        return 0;
    }

    public int getItemCount(int viewType) {
        if (Util.isListValid(mItems)) {
            int count = 0;
            for (IFlexItem iFlexItem : mItems) {
                int type = iFlexItem.getViewType();
                if (viewType == type) {
                    count++;
                }
            }
            return count;
        }
        return 0;
    }
}
