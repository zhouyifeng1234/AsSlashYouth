package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.RelativeLayout;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandModeBinding;
import com.slash.youth.ui.activity.MapActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/9/21.
 */
public class PublishDemandModeModel extends BaseObservable {

    public static final int OFFLINE_COST_TYPE_EMPLOYER = 0;//线下消费方式：雇主请客
    public static final int OFFLINE_COST_TYPE_AA = 1;//线下消费方式：AA制

    ActivityPublishDemandModeBinding mActivityPublishDemandModeBinding;
    Activity mActivity;
    int chooseOfflineCostType = OFFLINE_COST_TYPE_EMPLOYER;///线下消费方式,默认雇主请客
    boolean isOpenSafeBox = true;//默认开启保险箱
    boolean isOnlineDemand = true;//发布线上需求还是线下需求，默认为true,线上需求


    public PublishDemandModeModel(ActivityPublishDemandModeBinding activityPublishDemandModeBinding, Activity activity) {
        this.mActivityPublishDemandModeBinding = activityPublishDemandModeBinding;
        this.mActivity = activity;
        initView();
    }

    private void initView() {
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void publishDemand(View v) {
        ToastUtils.shortToast("发布需求成功");
    }

    public void getDetailLocation(View v) {
        Intent intentMapActivity = new Intent(CommonUtils.getContext(), MapActivity.class);
        intentMapActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentMapActivity);
    }


    /**
     * 选择线下消费方式为：雇主请客
     *
     * @param v
     */
    public void checkOfflineCostTypeEmployer(View v) {
        mActivityPublishDemandModeBinding.ivbtnOfflineCosttypeEmployer.setImageResource(R.mipmap.btn_employer_treat);
        mActivityPublishDemandModeBinding.ivbtnOfflineCosttypeAa.setImageResource(R.mipmap.ellipse_4);
        chooseOfflineCostType = OFFLINE_COST_TYPE_EMPLOYER;
    }


    /**
     * 选择线下消费方式为：AA制
     *
     * @param v
     */
    public void checkOfflineCostTypeAA(View v) {
        mActivityPublishDemandModeBinding.ivbtnOfflineCosttypeEmployer.setImageResource(R.mipmap.ellipse_4);
        mActivityPublishDemandModeBinding.ivbtnOfflineCosttypeAa.setImageResource(R.mipmap.btn_employer_treat);
        chooseOfflineCostType = OFFLINE_COST_TYPE_AA;
    }


    /**
     * 打开或关闭保险箱
     *
     * @param v
     */
    public void toogleSafeBox(View v) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mActivityPublishDemandModeBinding.ivDemandSafeboxToggleHandle.getLayoutParams();
        if (isOpenSafeBox) {
            mActivityPublishDemandModeBinding.ivDemandSafeboxToggleBg.setImageResource(R.mipmap.background_safebox_toggle_weijihuo);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mActivityPublishDemandModeBinding.ivDemandSafeboxToggleHandle.setLayoutParams(params);
        } else {
            mActivityPublishDemandModeBinding.ivDemandSafeboxToggleBg.setImageResource(R.mipmap.background_safebox_toggle);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            mActivityPublishDemandModeBinding.ivDemandSafeboxToggleHandle.setLayoutParams(params);
        }

        isOpenSafeBox = !isOpenSafeBox;
    }

    public void tabOnlineDemand(View v) {
        mActivityPublishDemandModeBinding.tvDemandOnlineText.setTextColor(0xff31C5E4);
        mActivityPublishDemandModeBinding.tvDemandOfflineText.setTextColor(0xff666666);
        mActivityPublishDemandModeBinding.vDemandOnlineUnderline.setBackgroundColor(0xff31C5E4);
        mActivityPublishDemandModeBinding.vDemandOfflineUnderline.setBackgroundColor(0xffE5E5E5);
        RelativeLayout.LayoutParams paramsOnline = (RelativeLayout.LayoutParams) mActivityPublishDemandModeBinding.vDemandOnlineUnderline.getLayoutParams();
        paramsOnline.height = CommonUtils.dip2px(2);
        mActivityPublishDemandModeBinding.vDemandOnlineUnderline.setLayoutParams(paramsOnline);
        RelativeLayout.LayoutParams paramsOffline = (RelativeLayout.LayoutParams) mActivityPublishDemandModeBinding.vDemandOfflineUnderline.getLayoutParams();
        paramsOffline.height = CommonUtils.dip2px(1);
        mActivityPublishDemandModeBinding.vDemandOfflineUnderline.setLayoutParams(paramsOffline);

        isOnlineDemand = true;
    }

    public void tabOfflineDemand(View v) {
        mActivityPublishDemandModeBinding.tvDemandOnlineText.setTextColor(0xff666666);
        mActivityPublishDemandModeBinding.tvDemandOfflineText.setTextColor(0xff31C5E4);
        mActivityPublishDemandModeBinding.vDemandOnlineUnderline.setBackgroundColor(0xffE5E5E5);
        mActivityPublishDemandModeBinding.vDemandOfflineUnderline.setBackgroundColor(0xff31C5E4);
        RelativeLayout.LayoutParams paramsOnline = (RelativeLayout.LayoutParams) mActivityPublishDemandModeBinding.vDemandOnlineUnderline.getLayoutParams();
        paramsOnline.height = CommonUtils.dip2px(1);
        mActivityPublishDemandModeBinding.vDemandOnlineUnderline.setLayoutParams(paramsOnline);
        RelativeLayout.LayoutParams paramsOffline = (RelativeLayout.LayoutParams) mActivityPublishDemandModeBinding.vDemandOfflineUnderline.getLayoutParams();
        paramsOffline.height = CommonUtils.dip2px(2);
        mActivityPublishDemandModeBinding.vDemandOfflineUnderline.setLayoutParams(paramsOffline);
        isOnlineDemand = false;
    }

    /**
     * 根据 用户所选择的 “咨询”还是“交付”类的需求，以及“线上”还是“线下”，进行相应需求条目的展示和隐藏
     */
    public void displayDemandModeItem() {

    }

}
