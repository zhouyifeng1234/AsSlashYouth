package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityMyAddSkillBinding;

/**
 * Created by zss on 2016/11/3.
 */
public class MyAddSkillModel extends BaseObservable {

    private ActivityMyAddSkillBinding activityMyAddSkillBinding;

    public MyAddSkillModel(ActivityMyAddSkillBinding activityMyAddSkillBinding) {
        this.activityMyAddSkillBinding = activityMyAddSkillBinding;
    }

}
