package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityPublishServiceInfoBinding;
import com.slash.youth.ui.activity.PublishServiceModeActivity;
import com.slash.youth.ui.view.SlashAddLabelsLayout;
import com.slash.youth.ui.view.SlashAddPicLayout;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/20.
 */
public class PublishServiceInfoModel extends BaseObservable {
    ActivityPublishServiceInfoBinding mActivityPublishServiceInfoBinding;
    Activity mActivity;
    public SlashAddLabelsLayout mSallSkillLabels;
    public SlashAddPicLayout mSaplAddPic;

    public PublishServiceInfoModel(ActivityPublishServiceInfoBinding activityPublishServiceInfoBinding, Activity activity) {
        this.mActivityPublishServiceInfoBinding = activityPublishServiceInfoBinding;
        this.mActivity = activity;
        mSallSkillLabels = mActivityPublishServiceInfoBinding.sallPublishServiceAddedSkilllabels;
        mSallSkillLabels.setActivity(activity);
        mSallSkillLabels.initSkillLabels();
        mSaplAddPic = mActivityPublishServiceInfoBinding.saplPublishServiceAddpic;
        mSaplAddPic.setActivity(mActivity);
        mSaplAddPic.initPic();
        initView();
    }

    private void initView() {

    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void nextStep(View v) {
        Intent intentPublishServiceModeActivity = new Intent(CommonUtils.getContext(), PublishServiceModeActivity.class);
        intentPublishServiceModeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentPublishServiceModeActivity);
    }

}
