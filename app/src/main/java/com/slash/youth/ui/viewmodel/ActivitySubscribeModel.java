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
import com.slash.youth.utils.LogKit;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhouyifeng on 2016/9/8.
 */
public class ActivitySubscribeModel extends BaseObservable {
    ActivitySubscribeBinding mActivitySubscribeBinding;
    SubscribeActivity mActivity;
    private NumberPicker mNpChooseMainLabels;
    String[] mainLabelsArr;
    private Iterator iter;
    private Map.Entry entry;
    private int value;

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
        mActivity.setOnListener(new SubscribeActivity.OnListener() {
            @Override
            public void OnListener(LinkedHashMap map) {
                iter = map.entrySet().iterator();
                mainLabelsArr = new String[map.size()];
                int i=0;
                while (iter.hasNext()) {
                    entry = (LinkedHashMap.Entry) iter.next();
                    String skillLabelValue = (String) entry.getValue();
                    mainLabelsArr[i] = skillLabelValue;
                    i+=1;
                }
                mNpChooseMainLabels.setDisplayedValues(mainLabelsArr);
                mNpChooseMainLabels.setMinValue(0);
                mNpChooseMainLabels.setMaxValue(mainLabelsArr.length - 1);
                mNpChooseMainLabels.setValue(1);
            }
        });
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

    //点击按钮
    public void okChooseMainLabel(View v) {
        setRlChooseMainLabelVisible(View.INVISIBLE);
        value = mNpChooseMainLabels.getValue();
        mActivity.checkedFirstLabel = mainLabelsArr[value];
        listener.OnOkChooseMainLabelListener(value);
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


    //接口回调传递数据
    public interface OnOkChooseMainLabelListener{
        void OnOkChooseMainLabelListener(int tagId);
    }

    private OnOkChooseMainLabelListener listener;
    public void setOnOkChooseMainLabelListener( OnOkChooseMainLabelListener listener) {
        this.listener = listener;
    }

}
