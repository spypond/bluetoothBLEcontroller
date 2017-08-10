package com.dev.nora.support.wizard.screen.list;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dev.nora.support.wizard.R;
import com.dev.nora.support.wizard.adapter.FlexAdapter;
import com.dev.nora.support.wizard.adapter.IFlexItem;
import com.dev.nora.support.wizard.adapter.decoration.FlexItemDecoration;
import com.dev.nora.support.wizard.adapter.holder.BaseViewHolder;
import com.dev.nora.support.wizard.screen.BaseFragment;

import java.util.List;

/**
 * Author by Duy P.Hoang.
 * <p/>
 * Created Date 8/15/2016
 */
public abstract class ListFragment extends BaseFragment implements
        BaseViewHolder.OnHolderClickedListener,
        BaseViewHolder.OnHolderChangedListener,
        BaseViewHolder.OnHolderTouchListener {
    private AsyncTask<Void, Void, List<? extends IFlexItem>> mLoader;
    protected FlexAdapter mAdapter;
    protected RecyclerView mRecyclerView;
    private FrameLayout mLoadingView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_view;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        initViews();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startLoadingData(true);
    }

    protected void initViews() {
        initRecyclerView();
        initOtherViews();
    }

    protected void initRecyclerView() {
        initAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(new FlexItemDecoration());
        mRecyclerView.setLayoutManager(getLayoutManagerOfRecyclerView());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initOtherViews() {
        mLoadingView = (FrameLayout) findViewById(R.id.lo_loading_view);
    }

    protected FlexAdapter createAdapter() {
        return new FlexAdapter();
    }

    private void initAdapter() {
        mAdapter = createAdapter();
        mAdapter.setOnHolderClickedListener(this);
        mAdapter.setOnHolderChangedListener(this);
        mAdapter.setOnHolderTouchListener(this);
    }

    public void requestLoadingData(boolean status) {
        startLoadingData(status);
    }

    private void startLoadingData(final boolean status) {
        if (mLoader != null) {
            mLoader.cancel(true);
        }
        mLoader = new AsyncTask<Void, Void, List<? extends IFlexItem>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (status) {
                    showLoadingView();
                }
            }

            @Override
            protected List<? extends IFlexItem> doInBackground(Void... voids) {
                return onDataLoading();
            }

            @Override
            protected void onPostExecute(List<? extends IFlexItem> iFlexItems) {
                super.onPostExecute(iFlexItems);
                hideLoadingView();
                onDataLoadDone(iFlexItems);
            }
        };
        mLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    protected abstract List<? extends IFlexItem> onDataLoading();

    protected void onDataLoadDone(List<? extends IFlexItem> items) {
        if (mAdapter == null) {
            throw new NullPointerException("mAdapter in list fragment is null.");
        }
        mAdapter.setItems(items);
    }

    protected void showLoadingView() {
        if (mLoadingView != null && mLoadingView.getVisibility() == View.GONE) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    protected void hideLoadingView() {
        if (mLoadingView != null && mLoadingView.getVisibility() == View.VISIBLE) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    public RecyclerView.LayoutManager getLayoutManagerOfRecyclerView() {
        return new LinearLayoutManager(mContext);
    }

    @Override
    public void onClicked(View v, BaseViewHolder holder) {

    }

    @Override
    public void onChildClicked(View parentView, BaseViewHolder parentHolder,
                               View childView, BaseViewHolder childHolder) {

    }

    @Override
    public void onViewHolderRecycled(BaseViewHolder holder) {

    }

    @Override
    public void onViewBound(BaseViewHolder holder) {

    }

    @Override
    public void onTouch(MotionEvent event, BaseViewHolder holder) {

    }
}
