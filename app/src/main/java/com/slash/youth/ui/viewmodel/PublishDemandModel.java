package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandBinding;
import com.slash.youth.ui.activity.MapActivity;
import com.slash.youth.ui.activity.PublishDemandDescribeActivity;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/17.
 */
public class PublishDemandModel extends BaseObservable {

    public static final int PUBLISH_DEMAND_CONSULTATION = 0;//咨询类需求
    public static final int PUBLISH_DEMAND_PAY = 1;//交付类需求

    ActivityPublishDemandBinding mActivityPublishDemandBinding;
    Activity mActivity;
    boolean isRealNamePublish = true;//true实名发布，false匿名发布
    int choosePublishType = PUBLISH_DEMAND_CONSULTATION;//默认选择咨询类
    boolean isCheckAllDay = true;


    public PublishDemandModel(ActivityPublishDemandBinding activityPublishDemandBinding, Activity activity) {
        this.mActivityPublishDemandBinding = activityPublishDemandBinding;
        this.mActivity = activity;
    }

    public void getDetailLocation(View v) {
        Intent intentMapActivity = new Intent(CommonUtils.getContext(), MapActivity.class);
        intentMapActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentMapActivity);
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void nextStep(View v) {
        Intent intentPublishDemandDescActivity = new Intent(CommonUtils.getContext(), PublishDemandDescribeActivity.class);

        Bundle publishDemandDataBundle = new Bundle();
        publishDemandDataBundle.putBoolean("isRealNamePublish", isRealNamePublish);
        publishDemandDataBundle.putInt("choosePublishType", choosePublishType);

        intentPublishDemandDescActivity.putExtra("publishDemandDataBundle", publishDemandDataBundle);
        intentPublishDemandDescActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentPublishDemandDescActivity);
    }

    public void choosePublishRealnameDemand(View v) {
        mActivityPublishDemandBinding.ivbtnPublishDemandRealname.setImageResource(R.mipmap.selected_icon);
        mActivityPublishDemandBinding.ivbtnPublishDemandAnonymous.setImageResource(R.mipmap.service_ptype_moren_icon);
        isRealNamePublish = true;
    }

    public void choosePublishAnonymousDemand(View v) {
        mActivityPublishDemandBinding.ivbtnPublishDemandRealname.setImageResource(R.mipmap.service_ptype_moren_icon);
        mActivityPublishDemandBinding.ivbtnPublishDemandAnonymous.setImageResource(R.mipmap.selected_icon);
        isRealNamePublish = false;
    }

    public void chooseConsultationDemand(View v) {
        mActivityPublishDemandBinding.ivbtnDemandTypeConsultation.setImageResource(R.mipmap.seclted);
        mActivityPublishDemandBinding.ivbtnDemandTypePay.setImageResource(R.mipmap.default_icon);
        mActivityPublishDemandBinding.tvDemandTypeConsultationDesc.setTextColor(0xff31C5E4);
        mActivityPublishDemandBinding.tvDemandTypePayDesc.setTextColor(0xff999999);
        mActivityPublishDemandBinding.ivbtnDemandTypeConsultationIcon.setImageResource(R.mipmap.zixun_seclted_icon);
        mActivityPublishDemandBinding.ivbtnDemandTypePayIcon.setImageResource(R.mipmap.jiaofu_default_icon);
        choosePublishType = PUBLISH_DEMAND_CONSULTATION;
    }

    public void choosePayDemand(View v) {
        mActivityPublishDemandBinding.ivbtnDemandTypeConsultation.setImageResource(R.mipmap.default_icon);
        mActivityPublishDemandBinding.ivbtnDemandTypePay.setImageResource(R.mipmap.seclted);
        mActivityPublishDemandBinding.tvDemandTypeConsultationDesc.setTextColor(0xff999999);
        mActivityPublishDemandBinding.tvDemandTypePayDesc.setTextColor(0xff31C5E4);
        mActivityPublishDemandBinding.ivbtnDemandTypeConsultationIcon.setImageResource(R.mipmap.zixun_default_icon);
        mActivityPublishDemandBinding.ivbtnDemandTypePayIcon.setImageResource(R.mipmap.jiaofu_seclted_icon);
        choosePublishType = PUBLISH_DEMAND_PAY;
    }

    public void checkAllDay(View v) {
        if (isCheckAllDay) {
            mActivityPublishDemandBinding.ivActivityPublishDemandCheckallday.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
        } else {
            mActivityPublishDemandBinding.ivActivityPublishDemandCheckallday.setImageResource(R.mipmap.dui_icon);
        }
        isCheckAllDay = !isCheckAllDay;
    }

}
