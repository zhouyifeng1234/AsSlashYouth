package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityTagRecommendBinding;
import com.slash.youth.utils.LogKit;

/**
 * Created by zhouyifeng on 2017/2/27.
 */
public class TagRecommendModel extends BaseObservable {

    ActivityTagRecommendBinding mActivityTagRecommendBinding;
    Activity mActivity;
    long tagId;
    String tagName;

    public TagRecommendModel(ActivityTagRecommendBinding activityTagRecommendBinding, Activity activity) {
        this.mActivityTagRecommendBinding = activityTagRecommendBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {
        tagId = mActivity.getIntent().getLongExtra("tagId", -1);
        tagName = mActivity.getIntent().getStringExtra("tagName");
        LogKit.v("tagId:" + tagId + "    tagName:" + tagName);
    }

    private void initView() {
        setFirstTagName(tagName);
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    private String firstTagName;

    @Bindable
    public String getFirstTagName() {
        return firstTagName;
    }

    public void setFirstTagName(String firstTagName) {
        this.firstTagName = firstTagName;
        notifyPropertyChanged(BR.firstTagName);
    }
}
