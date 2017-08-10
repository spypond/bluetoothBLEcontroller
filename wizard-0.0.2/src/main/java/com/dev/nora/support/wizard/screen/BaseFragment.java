package com.dev.nora.support.wizard.screen;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dev.nora.support.wizard.util.Util;

import java.util.List;

/**
 * Author by Duy P.Hoang.
 * <p>
 * Created Date 8/15/2016
 */
public class BaseFragment extends Fragment {
    protected Context mContext;
    protected View mRootView;

    public String getScreenView() {
        return null;
    }

    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    protected View findViewById(int id) {
        if (mRootView == null) {
            return null;
        }
        return mRootView.findViewById(id);
    }

    public void onInternetRecovered() {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (Util.isListValid(fragments)) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof BaseFragment) {
                    ((BaseFragment) fragment).onInternetRecovered();
                }
            }
        }
    }
}
