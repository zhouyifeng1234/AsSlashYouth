package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandAddinfoBinding;
import com.slash.youth.ui.activity.PublishDemandSuccessActivity;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.ui.view.SlashAddLabelsLayout;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class PublishDemandAddInfoModel extends BaseObservable {

    ActivityPublishDemandAddinfoBinding mActivityPublishDemandAddinfoBinding;
    Activity mActivity;
    boolean isOnline = true;//“线上”或者“线下”，默认为线上
    boolean isInstalment = true;//是否开启分期付，默认为true,开启
    private Bundle mPublishDemandData;
    public SlashAddLabelsLayout mSallSkillLabels;

    public PublishDemandAddInfoModel(ActivityPublishDemandAddinfoBinding activityPublishDemandAddinfoBinding, Activity activity) {
        this.mActivityPublishDemandAddinfoBinding = activityPublishDemandAddinfoBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {
        mPublishDemandData = mActivity.getIntent().getExtras();
    }

    private void initView() {
        mActivityPublishDemandAddinfoBinding.svPublishDemandLabels.setVerticalScrollBarEnabled(false);
        mSallSkillLabels = mActivityPublishDemandAddinfoBinding.sallPublishDemandAddedSkilllabels;
        mSallSkillLabels.setActivity(mActivity);
        mSallSkillLabels.initSkillLabels();
    }

    public void gotoBack(View v) {
        mActivity.finish();
    }

    public void publish(View v) {
        Intent intentPublishDemandSuccessActivity = new Intent(CommonUtils.getContext(), PublishDemandSuccessActivity.class);
        intentPublishDemandSuccessActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentPublishDemandSuccessActivity);
        mActivity.finish();
    }

    public void checkOnline(View v) {
        isOnline = true;
        setOfflineItemVisibility(View.GONE);
        mActivityPublishDemandAddinfoBinding.ivPublishDemandOnlineIcon.setImageResource(R.mipmap.pitchon_btn);
        mActivityPublishDemandAddinfoBinding.ivPublishDemandOfflineIcon.setImageResource(R.mipmap.default_btn);
    }

    public void checkOffline(View v) {
        isOnline = false;
        setOfflineItemVisibility(View.VISIBLE);
        mActivityPublishDemandAddinfoBinding.ivPublishDemandOnlineIcon.setImageResource(R.mipmap.default_btn);
        mActivityPublishDemandAddinfoBinding.ivPublishDemandOfflineIcon.setImageResource(R.mipmap.pitchon_btn);
    }

    public void toggleInstalment(View v) {
        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentHandle.getLayoutParams();
        if (isInstalment) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle_weijihuo);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle);
        }
        mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentHandle.setLayoutParams(layoutParams);
        isInstalment = !isInstalment;
    }

    public void openLabelsActivity(View v) {
        Intent intentSubscribeActivity = new Intent(CommonUtils.getContext(), SubscribeActivity.class);
        ArrayList<String> addedSkillLabels = mSallSkillLabels.getAddedSkillLabels();
        intentSubscribeActivity.putStringArrayListExtra("addedSkillLabels", addedSkillLabels);
        mActivity.startActivityForResult(intentSubscribeActivity, 10);
    }

    private int offlineItemVisibility = View.GONE;

    @Bindable
    public int getOfflineItemVisibility() {
        return offlineItemVisibility;
    }

    public void setOfflineItemVisibility(int offlineItemVisibility) {
        this.offlineItemVisibility = offlineItemVisibility;
        notifyPropertyChanged(BR.offlineItemVisibility);
    }

}
