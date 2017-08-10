package com.dev.nora.support.wizard.screen.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.nora.support.wizard.R;
import com.dev.nora.support.wizard.screen.BaseFragment;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/15/2016
 */
public class MenuFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_menu_view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        return mRootView;
    }
}
