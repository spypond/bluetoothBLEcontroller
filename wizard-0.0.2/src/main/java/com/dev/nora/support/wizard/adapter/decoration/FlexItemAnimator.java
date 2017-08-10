package com.dev.nora.support.wizard.adapter.decoration;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import com.dev.nora.support.wizard.adapter.holder.BaseViewHolder;

/**
 * The item animator for handling customization animations.
 * Author: Dat N. Truong<br>
 * Created date: 5/19/2016<br>
 */
class FlexItemAnimator extends DefaultItemAnimator {

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        if (holder instanceof BaseViewHolder) {
            if (!((BaseViewHolder) holder).isItemAnimatorEnabled()) {
                return false;
            }
        }
        return super.animateAdd(holder);
    }
}
