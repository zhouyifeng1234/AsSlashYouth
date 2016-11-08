package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.RelativeLayout;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMySettingBinding;
import com.slash.youth.ui.activity.FindPassWordActivity;
import com.slash.youth.ui.activity.ReportTAActivity;
import com.slash.youth.ui.activity.RevisePasswordActivity;
import com.slash.youth.ui.activity.SheildActivity;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zss on 2016/11/3.
 */
public class MySettingModel extends BaseObservable {
    private ActivityMySettingBinding activityMySettingBinding;
    private boolean isInstalment = false;//消息
    private boolean isInstalment1 = true;//时间

    public MySettingModel(ActivityMySettingBinding activityMySettingBinding) {
        this.activityMySettingBinding = activityMySettingBinding;
    }

    //消息设置
    public void newsSetting(View view){

        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) activityMySettingBinding.ivPublishDemandInstalmentHandle.getLayoutParams();
        if (isInstalment) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            activityMySettingBinding.ivPublishDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle_weijihuo);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            activityMySettingBinding.ivPublishDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle);
        }
        activityMySettingBinding.ivPublishDemandInstalmentHandle.setLayoutParams(layoutParams);
        isInstalment = !isInstalment;

    }

    //时间设置
    public void timeSetting(View view){

        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) activityMySettingBinding.ivPublishDemandInstalmentHandle1.getLayoutParams();
        if (isInstalment1) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            activityMySettingBinding.ivPublishDemandInstalmentBg1.setImageResource(R.mipmap.background_safebox_toggle_weijihuo);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            activityMySettingBinding.ivPublishDemandInstalmentBg1.setImageResource(R.mipmap.background_safebox_toggle);
        }
        activityMySettingBinding.ivPublishDemandInstalmentHandle1.setLayoutParams(layoutParams);
        isInstalment1 = !isInstalment1;
    }

    //屏蔽
    public void sheild(View view){
        Intent intentSheildActivity = new Intent(CommonUtils.getContext(), SheildActivity.class);
        intentSheildActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentSheildActivity);
    }

    //修改密码
    public void revisePassWord(View view){
        Intent intentRevisePasswordActivity = new Intent(CommonUtils.getContext(), RevisePasswordActivity.class);
        intentRevisePasswordActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentRevisePasswordActivity);
    }

    //找回密码
    public void findPassWord(View view){
        Intent intentFindPassWordActivity = new Intent(CommonUtils.getContext(), FindPassWordActivity.class);
        intentFindPassWordActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentFindPassWordActivity);
    }



}
