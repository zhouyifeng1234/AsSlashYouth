package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandBinding;
import com.slash.youth.ui.activity.MapActivity;
import com.slash.youth.ui.activity.PublishDemandDescribeActivity;
import com.slash.youth.ui.view.SlashAddLabelsLayout;
import com.slash.youth.ui.view.SlashDateTimePicker;
import com.slash.youth.utils.CommonUtils;

import java.util.Calendar;
import java.util.Date;

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

    private String chooseStartDateTimeText;
    private String chooseEndDateTimeText;

    private int mStartDisplayMonth;
    private int mStartDisplayDay;
    private int mStartDisplayHour;
    private int mStartDisplayMinute;
    private int mEndDisplayMonth;
    private int mEndDisplayDay;
    private int mEndDisplayHour;
    private int mEndDisplayMinute;
    private SlashDateTimePicker mChooseDateTimePicker;
    private boolean isSetStartDateTime;//true为设置开始时间 ，false为设置结束时间

    private int chooseDateTimeLayerVisibility = View.INVISIBLE;//选择时间浮层时候显示，默认为不显示
    public SlashAddLabelsLayout mSallSkillLabels;

    public PublishDemandModel(ActivityPublishDemandBinding activityPublishDemandBinding, Activity activity) {
        this.mActivityPublishDemandBinding = activityPublishDemandBinding;
        this.mActivity = activity;
        initView();
    }

    private void initView() {
        mChooseDateTimePicker = mActivityPublishDemandBinding.sdtpPublishDemandChooseDatetime;
        mSallSkillLabels = mActivityPublishDemandBinding.sallPublishDemandAddedSkilllabels;
        mSallSkillLabels.setActivity(mActivity);
        mSallSkillLabels.initSkillLabels();
        initData();
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        mStartDisplayMonth = calendar.get(Calendar.MONTH) + 1;
        mStartDisplayDay = calendar.get(Calendar.DAY_OF_MONTH);
        mStartDisplayHour = calendar.get(Calendar.HOUR_OF_DAY);
        mStartDisplayMinute = calendar.get(Calendar.MINUTE);
        mEndDisplayMonth = mStartDisplayMonth;
        mEndDisplayDay = mStartDisplayDay;
        mEndDisplayHour = mStartDisplayHour;
        mEndDisplayMinute = mStartDisplayMinute;

        setChooseStartDateTimeText(mStartDisplayMonth + "月" + mStartDisplayDay + "日" + "-" + mStartDisplayHour + ":" + (mStartDisplayMinute < 10 ? "0" + mStartDisplayMinute : mStartDisplayMinute));
        setChooseEndDateTimeText(mEndDisplayMonth + "月" + mEndDisplayDay + "日" + "-" + mEndDisplayHour + ":" + (mEndDisplayMinute < 10 ? "0" + mEndDisplayMinute : mEndDisplayMinute));

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
        publishDemandDataBundle.putStringArrayList("listTotalAddedTagsNames", mSallSkillLabels.getAddedTagsName());
        publishDemandDataBundle.putInt("mStartDisplayMonth", mStartDisplayMonth);
        publishDemandDataBundle.putInt("mStartDisplayDay", mStartDisplayDay);
        publishDemandDataBundle.putInt("mStartDisplayHour", mStartDisplayHour);
        publishDemandDataBundle.putInt("mStartDisplayMinute", mStartDisplayMinute);

        publishDemandDataBundle.putInt("mEndDisplayMonth", mEndDisplayMonth);
        publishDemandDataBundle.putInt("mEndDisplayDay", mEndDisplayDay);
        publishDemandDataBundle.putInt("mEndDisplayHour", mEndDisplayHour);
        publishDemandDataBundle.putInt("mEndDisplayMinute", mEndDisplayMinute);

        publishDemandDataBundle.putBoolean("isCheckAllDay", isCheckAllDay);


        intentPublishDemandDescActivity.putExtra("publishDemandDataBundle", publishDemandDataBundle);
        intentPublishDemandDescActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentPublishDemandDescActivity);
    }

    public void choosePublishRealnameDemand(View v) {
        mActivityPublishDemandBinding.ivbtnPublishDemandRealname.setImageResource(R.mipmap.btn_employer_treat);
        mActivityPublishDemandBinding.ivbtnPublishDemandAnonymous.setImageResource(R.mipmap.service_ptype_moren_icon);
        isRealNamePublish = true;
    }

    public void choosePublishAnonymousDemand(View v) {
        mActivityPublishDemandBinding.ivbtnPublishDemandRealname.setImageResource(R.mipmap.service_ptype_moren_icon);
        mActivityPublishDemandBinding.ivbtnPublishDemandAnonymous.setImageResource(R.mipmap.btn_employer_treat);
        isRealNamePublish = false;
    }

    public void chooseConsultationDemand(View v) {
        mActivityPublishDemandBinding.ivbtnDemandTypeConsultation.setImageResource(R.mipmap.btn_employer_treat);
        mActivityPublishDemandBinding.ivbtnDemandTypePay.setImageResource(R.mipmap.default_icon);
        mActivityPublishDemandBinding.tvDemandTypeConsultationDesc.setTextColor(0xff31C5E4);
        mActivityPublishDemandBinding.tvDemandTypePayDesc.setTextColor(0xff999999);
        mActivityPublishDemandBinding.ivbtnDemandTypeConsultationIcon.setImageResource(R.mipmap.zixun_seclted_icon);
        mActivityPublishDemandBinding.ivbtnDemandTypePayIcon.setImageResource(R.mipmap.jiaofu_default_icon);
        choosePublishType = PUBLISH_DEMAND_CONSULTATION;
    }

    public void choosePayDemand(View v) {
        mActivityPublishDemandBinding.ivbtnDemandTypeConsultation.setImageResource(R.mipmap.default_icon);
        mActivityPublishDemandBinding.ivbtnDemandTypePay.setImageResource(R.mipmap.btn_employer_treat);
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
            mStartDisplayHour = 0;
            mStartDisplayMinute = 0;
            setChooseStartDateTimeText(mStartDisplayMonth + "月" + mStartDisplayDay + "日" + "-" + mStartDisplayHour + ":" + (mStartDisplayMinute < 10 ? "0" + mStartDisplayMinute : mStartDisplayMinute));
            mEndDisplayHour = 23;
            mEndDisplayMinute = 59;
            setChooseEndDateTimeText(mEndDisplayMonth + "月" + mEndDisplayDay + "日" + "-" + mEndDisplayHour + ":" + (mEndDisplayMinute < 10 ? "0" + mEndDisplayMinute : mEndDisplayMinute));
        }
        isCheckAllDay = !isCheckAllDay;
    }

    @Bindable
    public String getChooseEndDateTimeText() {
        return chooseEndDateTimeText;
    }

    public void setChooseEndDateTimeText(String chooseEndDateTimeText) {
        this.chooseEndDateTimeText = chooseEndDateTimeText;
        notifyPropertyChanged(BR.chooseEndDateTimeText);
    }

    @Bindable
    public String getChooseStartDateTimeText() {
        return chooseStartDateTimeText;
    }

    public void setChooseStartDateTimeText(String chooseStartDateTimeText) {
        this.chooseStartDateTimeText = chooseStartDateTimeText;
        notifyPropertyChanged(BR.chooseStartDateTimeText);
    }

    @Bindable
    public int getChooseDateTimeLayerVisibility() {
        return chooseDateTimeLayerVisibility;
    }

    public void setChooseDateTimeLayerVisibility(int chooseDateTimeLayerVisibility) {
        this.chooseDateTimeLayerVisibility = chooseDateTimeLayerVisibility;
        notifyPropertyChanged(BR.chooseDateTimeLayerVisibility);
    }

    public void cancelChooseTime(View v) {
        setChooseDateTimeLayerVisibility(View.INVISIBLE);
    }

    public void okChooseTime(View v) {
        setChooseDateTimeLayerVisibility(View.INVISIBLE);
        int currentChooseMonth = mChooseDateTimePicker.getCurrentChooseMonth();
        int currentChooseDay = mChooseDateTimePicker.getCurrentChooseDay();
        int currentChooseHour = mChooseDateTimePicker.getCurrentChooseHour();
        int currentChooseMinute = mChooseDateTimePicker.getCurrentChooseMinute();
        if (isSetStartDateTime) {
            setChooseStartDateTimeText(currentChooseMonth + "月" + currentChooseDay + "日" + "-" + currentChooseHour + ":" + (currentChooseMinute < 10 ? "0" + currentChooseMinute : currentChooseMinute));
            mStartDisplayMonth = currentChooseMonth;
            mStartDisplayDay = currentChooseDay;
            mStartDisplayHour = currentChooseHour;
            mStartDisplayMinute = currentChooseMinute;
        } else {
            setChooseEndDateTimeText(currentChooseMonth + "月" + currentChooseDay + "日" + "-" + currentChooseHour + ":" + (currentChooseMinute < 10 ? "0" + currentChooseMinute : currentChooseMinute));
            mEndDisplayMonth = currentChooseMonth;
            mEndDisplayDay = currentChooseDay;
            mEndDisplayHour = currentChooseHour;
            mEndDisplayMinute = currentChooseMinute;
        }

    }

    public void chooseStartDateTime(View v) {
        isSetStartDateTime = true;
        setChooseDateTimeLayerVisibility(View.VISIBLE);
    }

    public void chooseEndDateTime(View v) {
        isSetStartDateTime = false;
        setChooseDateTimeLayerVisibility(View.VISIBLE);
    }
}
