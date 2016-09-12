package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityChooseSkillBinding;

/**
 * Created by zhouyifeng on 2016/9/12.
 */
public class ActivityChooseSkillModel extends BaseObservable {

    ActivityChooseSkillBinding mActivityChooseSkillBinding;

    public ActivityChooseSkillModel(ActivityChooseSkillBinding activityChooseSkillBinding) {
        this.mActivityChooseSkillBinding = activityChooseSkillBinding;
        initView();
    }

    private void initView() {
    }
}
