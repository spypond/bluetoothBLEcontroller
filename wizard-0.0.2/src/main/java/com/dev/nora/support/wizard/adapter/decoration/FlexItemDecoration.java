package com.dev.nora.support.wizard.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dev.nora.support.wizard.adapter.IFlexItem;
import com.dev.nora.support.wizard.adapter.holder.BaseViewHolder;


public class FlexItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        BaseViewHolder holder = (BaseViewHolder) parent.getChildViewHolder(view);
        FlexItemDecorator item = null;
        if (holder != null) {
            // get item decoration
            IFlexItem itemData = holder.getItemData();
            if (itemData != null) {
                item = itemData.getItemDecorator();
            }
        }
        if (item != null) {
            int top = holder.getAdapterPosition() == 0 ? item.getMarginTop() : 0;
            outRect.set(item.getMarginLeft(), top, item.getMarginRight(), item.getMarginBottom());
        }
    }
}
