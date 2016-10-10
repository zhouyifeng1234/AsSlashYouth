package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityPerfectInfoBinding;
import com.slash.youth.ui.activity.ChooseSkillActivity;
import com.slash.youth.ui.activity.test.ScaleViewPagerTestActivity;
import com.slash.youth.ui.activity.test.TestActivity;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/12.
 */
public class PerfectInfoModel extends BaseObservable {

    ActivityPerfectInfoBinding mActivityPerfectInfoBinding;

    public PerfectInfoModel(ActivityPerfectInfoBinding activityPerfectInfoBinding) {
        this.mActivityPerfectInfoBinding = activityPerfectInfoBinding;
        initView();
    }

    private void initView() {

    }

    public void okPerfectInfo(View v) {


        Intent intentChooseSkillActivity = new Intent(CommonUtils.getContext(), ChooseSkillActivity.class);
        intentChooseSkillActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentChooseSkillActivity);
    }

    public void openTestActivity(View v) {
        Intent intentTestActivity = new Intent(CommonUtils.getContext(), TestActivity.class);
        intentTestActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentTestActivity);
    }

    public void openScaleViewPagerTestActivity(View v) {
        Intent intentScaleViewPagerTestActivity = new Intent(CommonUtils.getContext(), ScaleViewPagerTestActivity.class);
        intentScaleViewPagerTestActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentScaleViewPagerTestActivity);
    }
}
