package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandAddinfoBinding;
import com.slash.youth.ui.activity.MapActivity;
import com.slash.youth.ui.activity.PublishDemandBaseInfoActivity;
import com.slash.youth.ui.activity.PublishDemandSuccessActivity;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.ui.view.SlashAddLabelsLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class PublishDemandAddInfoModel extends BaseObservable {

    String isUpdate = "";//是否为修改需求
    ActivityPublishDemandAddinfoBinding mActivityPublishDemandAddinfoBinding;
    Activity mActivity;
    boolean isOnline = true;//“线上”或者“线下”，默认为线上
    boolean isInstalment = true;//是否开启分期付，默认为true,开启
    private Bundle mPublishDemandData;
    public SlashAddLabelsLayout mSallSkillLabels;
    String[] disputeHandingTypes = new String[]{
            "平台方式", "协商处理"
    };//纠纷处理方式
    public int checkedDisputeHandingTypeIndex = 0;//选择的纠纷处理方式

    public PublishDemandAddInfoModel(ActivityPublishDemandAddinfoBinding activityPublishDemandAddinfoBinding, Activity activity) {
        this.mActivityPublishDemandAddinfoBinding = activityPublishDemandAddinfoBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {
        isUpdate = mActivity.getIntent().getStringExtra("update");
        if (TextUtils.equals(isUpdate, "update")) {
            ToastUtils.shortToast("修改需求");
        }
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
        mActivity.startActivity(intentPublishDemandSuccessActivity);
        mActivity.finish();
        if (PublishDemandBaseInfoActivity.mActivity != null) {
            PublishDemandBaseInfoActivity.mActivity.finish();
            PublishDemandBaseInfoActivity.mActivity = null;
        }
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

    public void openMapActivity(View v) {
        Intent intentMapActivity = new Intent(CommonUtils.getContext(), MapActivity.class);
//        intentMapActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivityForResult(intentMapActivity, 20);
    }

    public void chooseDisputeHandingType(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setSingleChoiceItems(disputeHandingTypes, checkedDisputeHandingTypeIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkedDisputeHandingTypeIndex = which;
                mActivityPublishDemandAddinfoBinding.tvDisputeHandingType.setText(disputeHandingTypes[which]);
                mActivityPublishDemandAddinfoBinding.tvDisputeHandingType.setTextColor(0xff333333);
                dialog.dismiss();
            }
        });
        builder.show();
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

    public void setLocationAddress(String address) {
//        ToastUtils.shortToast(address);
        mActivityPublishDemandAddinfoBinding.etPublishDemandAddress.setText(address);
    }

    public String getLocationAddress() {
        return mActivityPublishDemandAddinfoBinding.etPublishDemandAddress.getText().toString();
    }
}
