package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import com.slash.youth.BR;
import com.slash.youth.databinding.ActivitySubscribeBinding;
import com.slash.youth.ui.activity.SubscribeActivity;

/**
 * Created by zhouyifeng on 2016/9/8.
 */
public class ActivitySubscribeModel extends BaseObservable {
    ActivitySubscribeBinding mActivitySubscribeBinding;
    SubscribeActivity mActivity;
    private NumberPicker mNpChooseMainLabels;
    String[] mainLabelsArr;

    public ActivitySubscribeModel(ActivitySubscribeBinding activitySubscribeBinding, SubscribeActivity activity) {
        this.mActivitySubscribeBinding = activitySubscribeBinding;
        this.mActivity = activity;
        initView();
    }

    private void initView() {
        mNpChooseMainLabels = mActivitySubscribeBinding.npPublishServiceMainLabels;
        initData();
    }

    private void initData() {
        mainLabelsArr = new String[]{"金融", "IT", "农业", "水产", "文学"};//大类标签内容实际应该由服务端接口返回
        mNpChooseMainLabels.setDisplayedValues(mainLabelsArr);
        mNpChooseMainLabels.setMinValue(0);
        mNpChooseMainLabels.setMaxValue(mainLabelsArr.length - 1);
        mNpChooseMainLabels.setValue(1);
    }


    private int rlChooseMainLabelVisible = View.INVISIBLE;

    @Bindable
    public int getRlChooseMainLabelVisible() {
        return rlChooseMainLabelVisible;
    }

    public void setRlChooseMainLabelVisible(int rlChooseMainLabelVisible) {
        this.rlChooseMainLabelVisible = rlChooseMainLabelVisible;
        notifyPropertyChanged(BR.rlChooseMainLabelVisible);
    }

    public void openRlChooseMainLabel(View v) {
        setRlChooseMainLabelVisible(View.VISIBLE);
    }

    public void okChooseMainLabel(View v) {
        setRlChooseMainLabelVisible(View.INVISIBLE);
        int value = mNpChooseMainLabels.getValue();
        mActivity.checkedFirstLabel = mainLabelsArr[value];
    }

    public void submitChooseSkillLabel(View v) {
        Intent intentResult = new Intent();
        Bundle bundleCheckedLabelsData = new Bundle();
        bundleCheckedLabelsData.putString("checkedFirstLabel", mActivity.checkedFirstLabel);
        bundleCheckedLabelsData.putString("checkedSecondLabel", mActivity.checkedSecondLabel);
        bundleCheckedLabelsData.putStringArrayList("listCheckedLabelName", mActivity.listCheckedLabelName);
        intentResult.putExtra("bundleCheckedLabelsData", bundleCheckedLabelsData);
        mActivity.setResult(10, intentResult);
        mActivity.finish();
    }

    public void goBack(View v) {
        mActivity.finish();
    }
}
