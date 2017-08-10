package com.dev.nora.support.wizard.screen.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.nora.support.wizard.R;
import com.dev.nora.support.wizard.item.TabItem;
import com.dev.nora.support.wizard.screen.BaseFragment;
import com.dev.nora.support.wizard.util.Util;

import java.util.List;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/15/2016
 */
public abstract class PagerFragment extends BaseFragment {

    protected static final int SINGLE_PAGER_FRAGMENT = 10000;
    protected static final int NAVIGATION_PAGER_FRAGMENT = 10001;
    protected static final int NAVIGATION_FRAGMENT = 10002;

    protected PagerAdapter mAdapter;
    protected ViewPager mPager;
    private List<TabItem> mTabItems;

    protected abstract List<TabItem> createTabItems();

    protected abstract Fragment getPagerFragment(int pos, int tabId);

    protected int getTypeLayout() {
        return SINGLE_PAGER_FRAGMENT;
    }

    @Override
    protected int getLayoutId() {
        switch (getTypeLayout()) {
            case SINGLE_PAGER_FRAGMENT:
                return R.layout.fragment_pager_view;
            case NAVIGATION_PAGER_FRAGMENT:
                return R.layout.fragment_navigation_pager_view;
            case NAVIGATION_FRAGMENT:
                return R.layout.fragment_navigation_view;
            default:
                return R.layout.fragment_pager_view;
        }
    }

    private void handlePagerLayout() {
        switch (getTypeLayout()) {
            case SINGLE_PAGER_FRAGMENT:
                initTabs();
                break;
            case NAVIGATION_FRAGMENT:
                break;
            case NAVIGATION_PAGER_FRAGMENT:
                initTabs();
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        initViews();
        return mRootView;
    }

    protected void initViews() {
        handlePagerLayout();
        //initToolbar();
    }

    protected void initToolbar() {
        TextView tvAppName = (TextView) findViewById(R.id.tv_app_name);
        tvAppName.setText(getScreenView());
    }

    protected void initTabs() {
        mTabItems = createTabItems();
        initViewPager();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);
    }

    public void setCurrentPage(int page) {
        mPager.setCurrentItem(page);
    }

    protected void initViewPager() {
        mAdapter = createPagerAdapter();
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
    }

    protected PagerAdapter createPagerAdapter() {
        return new PagerAdapter(getChildFragmentManager());
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {
        private SparseArray<Fragment> mAvailableFragments;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            mAvailableFragments = new SparseArray<>();
        }

        @Override
        public Fragment getItem(int position) {
            int id = 0;
            if (Util.isListValid(mTabItems)) {
                id = mTabItems.get(position).getId();
            }
            return getPagerFragment(position, id);
        }

        @Override
        public int getCount() {
            return createTabItems() == null ? 0 : createTabItems().size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            mAvailableFragments.put(position, (Fragment) obj);
            return obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mAvailableFragments.remove(position);
        }

        public Fragment getAvailableFragment(int position) {
            return mAvailableFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (Util.isListValid(mTabItems)) {
                return mTabItems.get(position).getName();
            }
            return null;
        }
    }
}
