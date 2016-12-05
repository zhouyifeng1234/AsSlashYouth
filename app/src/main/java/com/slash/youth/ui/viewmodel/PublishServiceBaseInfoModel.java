package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceBaseinfoBinding;
import com.slash.youth.domain.ServiceDetailBean;
import com.slash.youth.domain.UploadFileResultBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.PublishServiceAddInfoActivity;
import com.slash.youth.ui.view.SlashAddPicLayout;
import com.slash.youth.ui.view.SlashDateTimePicker;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.IOUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhouyifeng on 2016/11/8.
 */
public class PublishServiceBaseInfoModel extends BaseObservable {
    public static final int PUBLISH_ANONYMITY_ANONYMOUS = 0;//匿名发布
    public static final int PUBLISH_ANONYMITY_REALNAME = 1;//实名发布

    public static final int SERVICE_TIMETYPE_USER_DEFINED = 0;
    public static final int SERVICE_TIMETYPE_AFTER_WORK = 1;
    public static final int SERVICE_TIMETYPE_WEEKEND = 2;
    public static final int SERVICE_TIMETYPE_AFTER_WORK_AND_WEEKEND = 3;
    public static final int SERVICE_TIMETYPE_ANYTIME = 4;

    ActivityPublishServiceBaseinfoBinding mActivityPublishServiceBaseinfoBinding;
    Activity mActivity;
    public SlashAddPicLayout mSaplAddPic;
    public SlashDateTimePicker mChooseDateTimePicker;

    int anonymity = PUBLISH_ANONYMITY_REALNAME;//是否匿名发布，默认为实名发布
    private int mCurrentChooseMonth;
    private int mCurrentChooseDay;
    private int mCurrentChooseHour;
    private int mCurrentChooseMinute;
    private boolean mIsChooseStartTime;
    int timetype;//闲时类型
    long starttime;
    long endtime;
    String starttimeStr;
    String endtimeStr;
    ServiceDetailBean serviceDetailBean;

    public PublishServiceBaseInfoModel(ActivityPublishServiceBaseinfoBinding activityPublishServiceBaseinfoBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityPublishServiceBaseinfoBinding = activityPublishServiceBaseinfoBinding;
        initData();
        initView();
    }

    private void initData() {
        mSaplAddPic = mActivityPublishServiceBaseinfoBinding.saplPublishServiceAddpic;//在loadOriginServiceData()中会使用，所以必须在这里初始化
        serviceDetailBean = (ServiceDetailBean) mActivity.getIntent().getSerializableExtra("serviceDetailBean");
        if (serviceDetailBean != null) {//表示是修改服务，首先需要把服务的数据填充
            loadOriginServiceData();
        }
    }


    private void initView() {
        mChooseDateTimePicker = mActivityPublishServiceBaseinfoBinding.sdtpPublishServiceChooseDatetime;

        mSaplAddPic.setActivity(mActivity);
        mSaplAddPic.initPic();
    }


    /**
     * 修改服务，首先回填服务数据
     */
    private void loadOriginServiceData() {
        ServiceDetailBean.Service service = serviceDetailBean.data.service;
        if (service.anonymity == PUBLISH_ANONYMITY_ANONYMOUS) {//匿名
            checkAnonymous(null);
        } else if (service.anonymity == PUBLISH_ANONYMITY_REALNAME) {//实名
            checkRealName(null);
        }
        mActivityPublishServiceBaseinfoBinding.etPublishServiceTitle.setText(service.title);
        mActivityPublishServiceBaseinfoBinding.etPublishServiceDesc.setText(service.desc);
        if (service.timetype == SERVICE_TIMETYPE_USER_DEFINED) {//自定义
            timetype = 0;
            starttime = service.starttime;
            endtime = service.endtime;
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日-hh:mm");
            starttimeStr = sdf.format(starttime);
            endtimeStr = sdf.format(endtime);
            mActivityPublishServiceBaseinfoBinding.tvServiceStarttime.setText(starttimeStr);
            mActivityPublishServiceBaseinfoBinding.tvServiceEndtime.setText(endtimeStr);
        } else if (service.timetype == SERVICE_TIMETYPE_AFTER_WORK) {//下班后
            checkIdleTimeAfterWork(null);
        } else if (service.timetype == SERVICE_TIMETYPE_WEEKEND) {//周末
            checkIdleTimeWeekend(null);
        } else if (service.timetype == SERVICE_TIMETYPE_AFTER_WORK_AND_WEEKEND) {//下班后及周末
            checkIdleTimeAfterWorkAndWeekend(null);
        } else if (service.timetype == SERVICE_TIMETYPE_ANYTIME) {//随时
            checkIdleTimeAnytime(null);
        }
        //还有图片没有处理
        String[] picFileIds = service.pic.split(",");
        final String picCachePath = mActivity.getCacheDir().getAbsoluteFile() + "/picache/";
        File cacheDir = new File(picCachePath);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        for (String fileId : picFileIds) {
            DemandEngine.downloadFile(new BaseProtocol.IResultExecutor<byte[]>() {
                @Override
                public void execute(byte[] dataBean) {
//                    if (dataBean.length < 50) {
//
//                    }

                    Bitmap bitmap = BitmapFactory.decodeByteArray(dataBean, 0, dataBean.length);
                    if (bitmap != null) {


                        File tempFile = new File(picCachePath + System.currentTimeMillis() + ".jpeg");
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(tempFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                            fos = new FileOutputStream(tempFile);
//                            fos.write(dataBean);
                            mSaplAddPic.reloadPic(tempFile.getAbsolutePath());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            IOUtils.close(fos);
                        }
                    }
                }

                @Override
                public void executeResultError(String result) {

                }
            }, fileId);
        }

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

    //选址闲置时间类型 下班后
    public void checkIdleTimeAfterWork(View v) {
        setIdleTimeItemBg(R.drawable.shape_idletime_label_bg, R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected);
        timetype = SERVICE_TIMETYPE_AFTER_WORK;
    }

    //选址闲置时间类型 周末
    public void checkIdleTimeWeekend(View v) {
        setIdleTimeItemBg(R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg, R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected);
        timetype = SERVICE_TIMETYPE_WEEKEND;
    }

    //选址闲置时间类型 下班后和周末
    public void checkIdleTimeAfterWorkAndWeekend(View v) {
        setIdleTimeItemBg(R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg, R.drawable.shape_idletime_label_bg_unselected);
        timetype = SERVICE_TIMETYPE_AFTER_WORK_AND_WEEKEND;
    }

    //选址闲置时间类型 随时
    public void checkIdleTimeAnytime(View v) {
        setIdleTimeItemBg(R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg);
        timetype = SERVICE_TIMETYPE_ANYTIME;
    }

    private void setIdleTimeItemBg(int afterWorkBg, int weekendBg, int afterWorkAndWeekendBg, int anytimeBg) {
        mActivityPublishServiceBaseinfoBinding.tvIdletimeAfterwork.setBackgroundResource(afterWorkBg);
        mActivityPublishServiceBaseinfoBinding.tvIdletimeWeekend.setBackgroundResource(weekendBg);
        mActivityPublishServiceBaseinfoBinding.tvIdletimeAfterworkAndWeekend.setBackgroundResource(afterWorkAndWeekendBg);
        mActivityPublishServiceBaseinfoBinding.tvIdletimeAnytime.setBackgroundResource(anytimeBg);
    }

    public void nextStep(View v) {
        final Intent intentPublishServiceAddInfoActivity = new Intent(CommonUtils.getContext(), PublishServiceAddInfoActivity.class);
        if (serviceDetailBean != null) {
            intentPublishServiceAddInfoActivity.putExtra("serviceDetailBean", serviceDetailBean);
        }
        final Bundle bundleServiceData = new Bundle();
        String title = mActivityPublishServiceBaseinfoBinding.etPublishServiceTitle.getText().toString();
        bundleServiceData.putString("title", title);
        String desc = mActivityPublishServiceBaseinfoBinding.etPublishServiceDesc.getText().toString();
        bundleServiceData.putString("desc", desc);
        bundleServiceData.putInt("anonymity", anonymity);//取值只能1或者0 (1实名 0匿名)
        bundleServiceData.putInt("timetype", timetype);
        bundleServiceData.putLong("starttime", starttime);
        bundleServiceData.putLong("endtime", endtime);
        final ArrayList<String> imgUrl = new ArrayList<String>();
        bundleServiceData.putStringArrayList("pic", imgUrl);
        final ArrayList<String> addedPicTempPath = mSaplAddPic.getAddedPicTempPath();
        if (addedPicTempPath.size() <= 0) {
            ToastUtils.shortToast("至少上传一张图片");
            return;
        }
        LogKit.v(addedPicTempPath.size() + "");
        final int[] uploadCount = {0};
        for (final String filePath : addedPicTempPath) {
            DemandEngine.uploadFile(new BaseProtocol.IResultExecutor<UploadFileResultBean>() {
                @Override
                public void execute(UploadFileResultBean dataBean) {
                    LogKit.v(filePath + ":上传成功");
                    uploadCount[0]++;
                    LogKit.v("uploadCount:" + uploadCount[0]);
                    LogKit.v(dataBean + "");
                    imgUrl.add(dataBean.data.fileId);

                    if (uploadCount[0] >= addedPicTempPath.size()) {
                        intentPublishServiceAddInfoActivity.putExtras(bundleServiceData);
                        mActivity.startActivity(intentPublishServiceAddInfoActivity);
                    }
                }

                @Override
                public void executeResultError(String result) {
                    LogKit.v(filePath + ":上传失败");
                    uploadCount[0]++;
                    LogKit.v("uploadCount:" + uploadCount[0]);
                    if (uploadCount[0] >= addedPicTempPath.size()) {
                        intentPublishServiceAddInfoActivity.putExtras(bundleServiceData);
                        mActivity.startActivity(intentPublishServiceAddInfoActivity);
                    }
                }
            }, filePath);
        }

//        Intent intentPublishServiceAddInfoActivity = new Intent(CommonUtils.getContext(), PublishServiceAddInfoActivity.class);
//        intentPublishServiceAddInfoActivity.putExtra("serviceDetailBean", serviceDetailBean);
//        mActivity.startActivity(intentPublishServiceAddInfoActivity);
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
        String dateTimeStr = mCurrentChooseMonth + "月" + mCurrentChooseDay + "日" + "-" + mCurrentChooseHour + ":" + (mCurrentChooseMinute < 10 ? "0" + mCurrentChooseMinute : mCurrentChooseMinute);
        if (mIsChooseStartTime) {
            mActivityPublishServiceBaseinfoBinding.tvStartTime.setText(dateTimeStr);
            starttime = convertTimeToMillis();
            starttimeStr = dateTimeStr;
        } else {
            mActivityPublishServiceBaseinfoBinding.tvEndTime.setText(dateTimeStr);
            endtime = convertTimeToMillis();
            endtimeStr = dateTimeStr;
        }

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
        timetype = SERVICE_TIMETYPE_USER_DEFINED;
        mActivityPublishServiceBaseinfoBinding.tvServiceStarttime.setText(starttimeStr);
        mActivityPublishServiceBaseinfoBinding.tvEndTime.setText(endtimeStr);
    }

    public void closeStartTimeAndEndTimeLayer(View v) {
        setSetStartTimeAndEndTimeLayerVisibility(View.GONE);
    }

    public void chooseStartTime(View v) {
        mIsChooseStartTime = true;
        setChooseDateTimeLayerVisibility(View.VISIBLE);
    }

    public void chooseEndTime(View v) {
        mIsChooseStartTime = false;
        setChooseDateTimeLayerVisibility(View.VISIBLE);
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
