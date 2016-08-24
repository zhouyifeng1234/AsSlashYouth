package com.slash.youth.ui.holder;

import android.view.View;

public abstract class BaseHolder<T> {

	private View mRootView;
	private T data;

	public BaseHolder() {

		mRootView = initView();
		mRootView.setTag(this);
	}

	public abstract View initView();

	public void setData(T data) {
		this.data = data;
		refreshView(data);
	}

	public abstract void refreshView(T data);

	public View getRootView() {
		return mRootView;
	}

	public T getData() {
		return data;
	}
}
