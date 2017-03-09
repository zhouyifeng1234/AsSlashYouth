package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceInfoBinding;
import com.slash.youth.ui.activity.PublishServiceModeActivity;
import com.slash.youth.ui.view.SlashAddLabelsLayout;
import com.slash.youth.ui.view.SlashAddPicLayout;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/20.
 */
public class PublishServiceInfoModel extends BaseObservable {
    public static final int PUBLISH_SERVICE_TYPE_CONSULTATION = 0;//咨询类服务
    public static final int PUBLISH_SERVICE_TYPE_PAY = 1;//交付类服务

    ActivityPublishServiceInfoBinding mActivityPublishServiceInfoBinding;
    Activity mActivity;
    public SlashAddLabelsLayout mSallSkillLabels;
    public SlashAddPicLayout mSaplAddPic;
    private final Bundle mPublishServiceDataBundle;
    int publishServiceType = PUBLISH_SERVICE_TYPE_CONSULTATION;//发布服务的类型，默认为“咨询类”
    private String mPublishServiceTitle;
    private String mPublishServiceDesc;

    public PublishServiceInfoModel(ActivityPublishServiceInfoBinding activityPublishServiceInfoBinding, Activity activity) {
        this.mActivityPublishServiceInfoBinding = activityPublishServiceInfoBinding;
        this.mActivity = activity;
        mSallSkillLabels = mActivityPublishServiceInfoBinding.sallPublishServiceAddedSkilllabels;
        mSallSkillLabels.setActivity(activity);
        mSallSkillLabels.initSkillLabels();
        mSaplAddPic = mActivityPublishServiceInfoBinding.saplPublishServiceAddpic;
        mSaplAddPic.setActivity(mActivity);
        mSaplAddPic.initPic();
        mPublishServiceDataBundle = mActivity.getIntent().getBundleExtra("publishServiceDataBundle");
        initView();
    }

    private void initView() {

    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void nextStep(View v) {
        Intent intentPublishServiceModeActivity = new Intent(CommonUtils.getContext(), PublishServiceModeActivity.class);

        mPublishServiceTitle = mActivityPublishServiceInfoBinding.etPublishServiceTitle.getText().toString();
        mPublishServiceDesc = mActivityPublishServiceInfoBinding.etPublishServiceDesc.getText().toString();
        ArrayList<String> listAddedPicTempPath = mSaplAddPic.getAddedPicTempPath();
        ArrayList<String> listAddedSkillLabels = mSallSkillLabels.getAddedTagsName();
        mPublishServiceDataBundle.putInt("publishServiceType", publishServiceType);
        mPublishServiceDataBundle.putString("publishServiceTitle", mPublishServiceTitle);
        mPublishServiceDataBundle.putString("publishServiceDesc", mPublishServiceDesc);
        mPublishServiceDataBundle.putStringArrayList("listAddedPicTempPath", listAddedPicTempPath);
        mPublishServiceDataBundle.putStringArrayList("listAddedSkillLabels",listAddedSkillLabels);

        intentPublishServiceModeActivity.putExtra("publishServiceDataBundle", mPublishServiceDataBundle);
        intentPublishServiceModeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentPublishServiceModeActivity);
    }

    /**
     * 选择咨询类服务
     *
     * @param v
     */
    public void setPublishConsultationService(View v) {
        mActivityPublishServiceInfoBinding.ivbtnConsultationServiceChecked.setImageResource(R.mipmap.btn_employer_treat);
        mActivityPublishServiceInfoBinding.ivbtnPayServiceChecked.setImageResource(R.mipmap.default_icon);
        mActivityPublishServiceInfoBinding.tvConsultationServiceText.setTextColor(0xff31C5E4);
        mActivityPublishServiceInfoBinding.tvPayServiceText.setTextColor(0xff999999);
        mActivityPublishServiceInfoBinding.ivbtnConsultationServiceIcon.setImageResource(R.mipmap.zixun_seclted_icon);
        mActivityPublishServiceInfoBinding.ivbtnPayServiceIcon.setImageResource(R.mipmap.jiaofu_default_icon);

        publishServiceType = PUBLISH_SERVICE_TYPE_CONSULTATION;
    }

    /**
     * 选择交付类服务
     *
     * @param v
     */
    public void setPublishPayService(View v) {
        mActivityPublishServiceInfoBinding.ivbtnConsultationServiceChecked.setImageResource(R.mipmap.default_icon);
        mActivityPublishServiceInfoBinding.ivbtnPayServiceChecked.setImageResource(R.mipmap.btn_employer_treat);
        mActivityPublishServiceInfoBinding.tvConsultationServiceText.setTextColor(0xff999999);
        mActivityPublishServiceInfoBinding.tvPayServiceText.setTextColor(0xff31C5E4);
        mActivityPublishServiceInfoBinding.ivbtnConsultationServiceIcon.setImageResource(R.mipmap.zixun_default_icon);
        mActivityPublishServiceInfoBinding.ivbtnPayServiceIcon.setImageResource(R.mipmap.jiaofu_seclted_icon);

        publishServiceType = PUBLISH_SERVICE_TYPE_PAY;
    }
}
