package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.slash.youth.databinding.ActivityPublishDemandDescribeBinding;
import com.slash.youth.ui.activity.PublishDemandModeActivity;
import com.slash.youth.ui.view.SlashAddPicLayout;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/21.
 */
public class PublishDemandDescModel extends BaseObservable {
    ActivityPublishDemandDescribeBinding mActivityPublishDemandDescribeBinding;
    Activity mActivity;
    Bundle publishDemandDataBundle;
    public SlashAddPicLayout mSaplAddPic;
    public String demandTitle;
    public String demandDesc;
    private EditText mEtDemandTitle;
    private EditText mEtDemandDesc;

    public PublishDemandDescModel(ActivityPublishDemandDescribeBinding activityPublishDemandDescribeBinding, Activity activity, Bundle publishDemandDataBundle) {
        this.mActivityPublishDemandDescribeBinding = activityPublishDemandDescribeBinding;
        this.mActivity = activity;
        this.publishDemandDataBundle = publishDemandDataBundle;
        initView();
    }

    private void initView() {
        mSaplAddPic = mActivityPublishDemandDescribeBinding.saplPublishDemandAddpic;
        mSaplAddPic.setActivity(mActivity);
        mSaplAddPic.initPic();

        mEtDemandTitle = mActivityPublishDemandDescribeBinding.etPublishDemandTitle;
        mEtDemandDesc = mActivityPublishDemandDescribeBinding.etPublishDemandDesc;
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void nextStep(View v) {
        Intent intentPublishDemandModeActivity = new Intent(CommonUtils.getContext(), PublishDemandModeActivity.class);
        demandTitle = mEtDemandTitle.getText().toString();
        demandDesc = mEtDemandDesc.getText().toString();
        publishDemandDataBundle.putString("demandTitle", demandTitle);
        publishDemandDataBundle.putString("demandDesc", demandDesc);
        publishDemandDataBundle.putStringArrayList("listAddedPicTempPath", mSaplAddPic.getAddedPicTempPath());
        intentPublishDemandModeActivity.putExtra("publishDemandDataBundle", publishDemandDataBundle);
        intentPublishDemandModeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentPublishDemandModeActivity);
    }


}
