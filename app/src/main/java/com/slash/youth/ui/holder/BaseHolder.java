package com.slash.youth.ui.holder;

import android.view.View;

import com.slash.youth.ui.adapter.SlashBaseAdapter;
import com.slash.youth.utils.LogKit;

public abstract class BaseHolder<T> {

    private View mRootView;
    private T data;
    private int currentPosition;

    public BaseHolder() {
        mRootView = initView();
        if (mRootView != null) {
            mRootView.setTag(this);
        }
    }

    public abstract View initView();//加载view

    public void setData(T data, int position) {
        this.data = data;
        this.currentPosition = position;
        if (mRootView != null) {
            refreshView(data);
        }
    }

    public abstract void refreshView(T data);

    public View getRootView() {
        return mRootView;
    }

    public T getData() {
        return data;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }


}
