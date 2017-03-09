package com.slash.youth.ui.holder;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by zss on 2016/12/7.
 */
public abstract class SearchViewHolder<T> {
    private View mRootView;
    private T data;

    public SearchViewHolder() {
        mRootView = initView();
        if (mRootView != null) {
            mRootView.setTag(this);
        }
    }

    public abstract View initView();//加载view

    public void setData(T data, int position) {
        this.data = data;
        if (mRootView != null) {
            refreshView(data,position);
        }
    }

    public abstract void refreshView(T data,int position);

    public View getRootView() {
        return mRootView;
    }




}
