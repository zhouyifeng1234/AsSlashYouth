package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceBaseinfoBinding;
import com.slash.youth.ui.activity.PublishServiceAddInfoActivity;
import com.slash.youth.ui.view.SlashAddPicLayout;
import com.slash.youth.ui.view.SlashDateTimePicker;
import com.slash.youth.utils.CommonUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhouyifeng on 2016/11/8.
 */
public class PublishServiceBaseInfoModel extends BaseObservable {
    public static final int PUBLISH_ANONYMITY_ANONYMOUS = 0;//匿名发布
    public static final int PUBLISH_ANONYMITY_REALNAME = 1;//实名发布

    ActivityPublishServiceBaseinfoBinding mActivityPublishServiceBaseinfoBinding;
    Activity mActivity;
    public SlashAddPicLayout mSaplAddPic;
    public SlashDateTimePicker mChooseDateTimePicker;

    int anonymity = PUBLISH_ANONYMITY_REALNAME;//是否匿名发布，默认为实名发布
    private int mCurrentChooseMonth;
    private int mCurrentChooseDay;
    private int mCurrentChooseHour;
    private int mCurrentChooseMinute;

    public PublishServiceBaseInfoModel(ActivityPublishServiceBaseinfoBinding activityPublishServiceBaseinfoBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityPublishServiceBaseinfoBinding = activityPublishServiceBaseinfoBinding;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        mChooseDateTimePicker = mActivityPublishServiceBaseinfoBinding.sdtpPublishServiceChooseDatetime;
        mSaplAddPic = mActivityPublishServiceBaseinfoBinding.saplPublishServiceAddpic;
        mSaplAddPic.setActivity(mActivity);
        mSaplAddPic.initPic();
    }


    public void gotoBack(View v) {
        mActivity.finish();
    }

    //选择实名发布
    public void checkRealName(View v) {
        mActivityPublishServiceBaseinfoBinding.ivPublicServiceRealnameIcon.setImageResource(R.mipmap.pitchon_btn);
        mActivityPublishServiceBaseinfoBinding.ivPublishServiceAnonymousIcon.setImageResource(R.mipmap.default_btn);
        anonymity = PUBLISH_ANONYMITY_REALNAME;
    }

    //选择匿名发布
    public void checkAnonymous(View v) {
        mActivityPublishServiceBaseinfoBinding.ivPublicServiceRealnameIcon.setImageResource(R.mipmap.default_btn);
        mActivityPublishServiceBaseinfoBinding.ivPublishServiceAnonymousIcon.setImageResource(R.mipmap.pitchon_btn);
        anonymity = PUBLISH_ANONYMITY_ANONYMOUS;
    }

    public void nextStep(View v) {
        Intent intentPublishServiceAddInfoActivity = new Intent(CommonUtils.getContext(), PublishServiceAddInfoActivity.class);
        mActivity.startActivity(intentPublishServiceAddInfoActivity);
    }

    public void cancelChooseTime(View v) {
        setChooseDateTimeLayerVisibility(View.GONE);
    }

    public void okChooseTime(View v) {
        setChooseDateTimeLayerVisibility(View.GONE);
        mCurrentChooseMonth = mChooseDateTimePicker.getCurrentChooseMonth();
        mCurrentChooseDay = mChooseDateTimePicker.getCurrentChooseDay();
        mCurrentChooseHour = mChooseDateTimePicker.getCurrentChooseHour();
        mCurrentChooseMinute = mChooseDateTimePicker.getCurrentChooseMinute();
        //String dateTimeStr = mCurrentChooseMonth + "月" + mCurrentChooseDay + "日" + "-" + mCurrentChooseHour + ":" + (mCurrentChooseMinute < 10 ? "0" + mCurrentChooseMinute : mCurrentChooseMinute);
        convertTimeToMillis();
    }

    public long convertTimeToMillis() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, mCurrentChooseMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, mCurrentChooseDay);
        calendar.set(Calendar.HOUR_OF_DAY, mCurrentChooseHour);
        calendar.set(Calendar.MINUTE, mCurrentChooseMinute);
        return calendar.getTimeInMillis();
    }


    public void chooseCustomIdleTime(View v) {
        setSetStartTimeAndEndTimeLayerVisibility(View.VISIBLE);

    }

    public void okChooseIdleStartTimeAndEndTime(View v) {
        setSetStartTimeAndEndTimeLayerVisibility(View.GONE);

    }

    public void closeStartTimeAndEndTimeLayer(View v) {
        setSetStartTimeAndEndTimeLayerVisibility(View.GONE);
    }

    public void chooseStartTime(View v) {

    }

    public void chooseEndTime(View v) {

    }

    private int chooseDateTimeLayerVisibility = View.GONE;
    private int setStartTimeAndEndTimeLayerVisibility = View.GONE;

    @Bindable
    public int getChooseDateTimeLayerVisibility() {
        return chooseDateTimeLayerVisibility;
    }

    public void setChooseDateTimeLayerVisibility(int chooseDateTimeLayerVisibility) {
        this.chooseDateTimeLayerVisibility = chooseDateTimeLayerVisibility;
        notifyPropertyChanged(BR.chooseDateTimeLayerVisibility);
    }

    @Bindable
    public int getSetStartTimeAndEndTimeLayerVisibility() {
        return setStartTimeAndEndTimeLayerVisibility;
    }

    public void setSetStartTimeAndEndTimeLayerVisibility(int setStartTimeAndEndTimeLayerVisibility) {
        this.setStartTimeAndEndTimeLayerVisibility = setStartTimeAndEndTimeLayerVisibility;
        notifyPropertyChanged(BR.setStartTimeAndEndTimeLayerVisibility);
    }
}
