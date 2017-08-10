package com.dev.nora.support.wizard.adapter;

import android.os.Parcelable;

import com.dev.nora.support.wizard.adapter.decoration.FlexItemDecorator;

import java.io.Serializable;


/**
 * Created by DAT on 11/21/2015.
 */
public interface IFlexItem extends Parcelable, Serializable {

    /**
     * Return the id of this item
     *
     * @return the item id
     */
    String getId();

    /**
     * Return the title of this item.
     * The title could be the name, the title or any string that names this item
     *
     * @return the item name
     */
    String getTitle();

    /**
     * Return the description for this item
     *
     * @return the item description
     */
    String getDesc();

    /**
     * The view type for this item. the same type of item might have a different view type.
     * This view type is used by {@link FlexAdapter} to determent the corresponding view holder
     *
     * @return the item view type
     */
    int getViewType();

    /**
     * Set the view type for this item
     *
     * @param viewType the view type to set
     */
    void setViewType(int viewType);

    /**
     * Return the small thumbnail of this item.
     *
     * @return the item small thumbnail
     */
    String getThumbSmall();

    /**
     * Return the medium thumbnail of this item.
     *
     * @return the item medium thumbnail
     */
    String getThumbMedium();

    /**
     * Return the big thumbnail of this item.
     *
     * @return the item big thumbnail
     */
    String getThumbBig();

    /**
     * Return the created date of this item
     *
     * @return the item created date
     */
    long getCreatedDate();

    /**
     * Return the updated date of this item
     *
     * @return the item updated date
     */
    long getUpdatedDate();

    /**
     * Return the formatted date
     *
     * @param time the time in milliseconds
     * @return the formatted date
     */
    String getFormattedDate(long time);

    /**
     * Return the source link of this item. This should be the web link of the web version for this item
     *
     * @return the item source link
     */
    String getSource();

    /**
     * Return the item decorator
     *
     * @return the item decorator
     */
    FlexItemDecorator getItemDecorator();

    /**
     * Some click or touch action can be handle internal by the library.
     *
     * @return true to let the library handle these action internally
     */
    boolean isUseDefaultAction();

    boolean isItemAnimatorEnabled();
}
