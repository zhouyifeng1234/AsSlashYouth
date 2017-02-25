package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityChooseSkillBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.ActivityChooseSkillModel;

/**
 * Created by zhouyifeng on 2016/9/12.
 */
public class ChooseSkillActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChooseSkillBinding activityChooseSkillBinding = DataBindingUtil.setContentView(this, R.layout.activity_choose_skill);
        ActivityChooseSkillModel activityChooseSkillModel = new ActivityChooseSkillModel(activityChooseSkillBinding, this);
        activityChooseSkillBinding.setActivityChooseSkillModel(activityChooseSkillModel);
    }
}
