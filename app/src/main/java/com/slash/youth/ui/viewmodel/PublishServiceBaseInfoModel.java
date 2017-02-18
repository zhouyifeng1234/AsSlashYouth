package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceBaseinfoBinding;
import com.slash.youth.domain.ServiceDetailBean;
import com.slash.youth.domain.UploadFileResultBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.activity.PublishServiceAddInfoActivity;
import com.slash.youth.ui.view.SlashAddPicLayout;
import com.slash.youth.ui.view.SlashDateTimePicker;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.IOUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

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
    private int mCurrentChooseYear;
    private int mCurrentChooseMonth;
    private int mCurrentChooseDay;
    private int mCurrentChooseHour;
    private int mCurrentChooseMinute;
    private boolean mIsChooseStartTime;
    int timetype = 0;//闲时类型
    long starttime = -1;
    long endtime = -1;
    String starttimeStr;
    String endtimeStr;
    ServiceDetailBean serviceDetailBean;
    boolean isFromSkillManager;

    public PublishServiceBaseInfoModel(ActivityPublishServiceBaseinfoBinding activityPublishServiceBaseinfoBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityPublishServiceBaseinfoBinding = activityPublishServiceBaseinfoBinding;
        initData();
        initView();
        initListener();
    }

    private void initData() {
        mSaplAddPic = mActivityPublishServiceBaseinfoBinding.saplPublishServiceAddpic;//在loadOriginServiceData()中会使用，所以必须在这里初始化
        serviceDetailBean = (ServiceDetailBean) mActivity.getIntent().getSerializableExtra("serviceDetailBean");
        isFromSkillManager = mActivity.getIntent().getBooleanExtra("isFromSkillManager", false);
        if (isFromSkillManager) {
            setGotoSkillManagerVisibility(View.GONE);
        }
        if (serviceDetailBean != null) {//表示是修改服务，首先需要把服务的数据填充
            if (!isFromSkillManager) {
                mActivityPublishServiceBaseinfoBinding.tvPublishServiceText.setText("修改服务");
                mActivityPublishServiceBaseinfoBinding.tvPublishSuccessText.setText("修改成功");
            }
            loadOriginServiceData();
        }
    }


    private void initView() {
        mChooseDateTimePicker = mActivityPublishServiceBaseinfoBinding.sdtpPublishServiceChooseDatetime;

        mSaplAddPic.setActivity(mActivity);
        mSaplAddPic.initPic();
    }

    private void initListener() {
        mActivityPublishServiceBaseinfoBinding.etPublishServiceDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                int textCount = text.length();
                mActivityPublishServiceBaseinfoBinding.tvDescTextCount.setText(textCount + "/300");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        int descWordsCount = service.desc.length();
        mActivityPublishServiceBaseinfoBinding.tvDescTextCount.setText(descWordsCount + "/300");
        if (service.timetype == SERVICE_TIMETYPE_USER_DEFINED) {//自定义
            timetype = 0;
            starttime = service.starttime;
            endtime = service.endtime;
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
            starttimeStr = sdf.format(starttime);
            endtimeStr = sdf.format(endtime);
//            mActivityPublishServiceBaseinfoBinding.tvServiceStarttime.setText(starttimeStr);
//            mActivityPublishServiceBaseinfoBinding.tvServiceEndtime.setText(endtimeStr);
            mActivityPublishServiceBaseinfoBinding.tvPublicServiceTime.setText(starttimeStr + " " + endtimeStr);
            mActivityPublishServiceBaseinfoBinding.tvPublicServiceTime.setTextColor(0xff333333);
            setDeleteCustomTimeIconVisibility(View.VISIBLE);
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

    public void gotoSkillManagerActivity(View v) {
        Intent intentMySkillManageActivity = new Intent(CommonUtils.getContext(), MySkillManageActivity.class);
        intentMySkillManageActivity.putExtra("Title", "技能列表");
        intentMySkillManageActivity.putExtra("avater", LoginManager.currentLoginUserAvatar);
        intentMySkillManageActivity.putExtra("name", LoginManager.currentLoginUserName);
        mActivity.startActivity(intentMySkillManageActivity);
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
        setDeleteCustomTimeIconVisibility(View.VISIBLE);
        mActivityPublishServiceBaseinfoBinding.tvPublicServiceTime.setText("下班后");
        mActivityPublishServiceBaseinfoBinding.tvPublicServiceTime.setTextColor(0xff333333);
    }

    //选址闲置时间类型 周末
    public void checkIdleTimeWeekend(View v) {
        setIdleTimeItemBg(R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg, R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected);
        timetype = SERVICE_TIMETYPE_WEEKEND;
        setDeleteCustomTimeIconVisibility(View.VISIBLE);
        mActivityPublishServiceBaseinfoBinding.tvPublicServiceTime.setText("周末");
        mActivityPublishServiceBaseinfoBinding.tvPublicServiceTime.setTextColor(0xff333333);
    }

    //选址闲置时间类型 下班后和周末
    public void checkIdleTimeAfterWorkAndWeekend(View v) {
        setIdleTimeItemBg(R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg, R.drawable.shape_idletime_label_bg_unselected);
        timetype = SERVICE_TIMETYPE_AFTER_WORK_AND_WEEKEND;
        setDeleteCustomTimeIconVisibility(View.VISIBLE);
        mActivityPublishServiceBaseinfoBinding.tvPublicServiceTime.setText("下班后和周末");
        mActivityPublishServiceBaseinfoBinding.tvPublicServiceTime.setTextColor(0xff333333);
    }

    //选址闲置时间类型 随时
    public void checkIdleTimeAnytime(View v) {
        setIdleTimeItemBg(R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg);
        timetype = SERVICE_TIMETYPE_ANYTIME;
        setDeleteCustomTimeIconVisibility(View.VISIBLE);
        mActivityPublishServiceBaseinfoBinding.tvPublicServiceTime.setText("随时");
        mActivityPublishServiceBaseinfoBinding.tvPublicServiceTime.setTextColor(0xff333333);
    }

    private void setIdleTimeItemBg(int afterWorkBg, int weekendBg, int afterWorkAndWeekendBg, int anytimeBg) {
        mActivityPublishServiceBaseinfoBinding.tvIdletimeAfterwork.setBackgroundResource(afterWorkBg);
        mActivityPublishServiceBaseinfoBinding.tvIdletimeWeekend.setBackgroundResource(weekendBg);
        mActivityPublishServiceBaseinfoBinding.tvIdletimeAfterworkAndWeekend.setBackgroundResource(afterWorkAndWeekendBg);
        mActivityPublishServiceBaseinfoBinding.tvIdletimeAnytime.setBackgroundResource(anytimeBg);
    }

    boolean isClickNext = false;

    public void nextStep(View v) {
        if (isClickNext) {
            return;
        }
        isClickNext = true;

        final Intent intentPublishServiceAddInfoActivity = new Intent(CommonUtils.getContext(), PublishServiceAddInfoActivity.class);
        intentPublishServiceAddInfoActivity.putExtra("isFromSkillManager", isFromSkillManager);
        if (serviceDetailBean != null) {
            intentPublishServiceAddInfoActivity.putExtra("serviceDetailBean", serviceDetailBean);
        }
        final Bundle bundleServiceData = new Bundle();
        String title = mActivityPublishServiceBaseinfoBinding.etPublishServiceTitle.getText().toString();
        if (title.length() < 5 || title.length() > 20) {
            ToastUtils.shortToast("标题必须为5-20字之间");
            isClickNext = false;
            return;
        }
        bundleServiceData.putString("title", title);
        String desc = mActivityPublishServiceBaseinfoBinding.etPublishServiceDesc.getText().toString();
        if (desc.length() <= 0) {
            ToastUtils.shortToast("请输入服务描述");
            isClickNext = false;
            return;
        } else if (desc.length() < 5) {
            ToastUtils.shortToast("服务描述必须是5-300字之间");
            isClickNext = false;
            return;
        } else if (desc.length() > 300) {
            ToastUtils.shortToast("服务描述不能超过300字");
            isClickNext = false;
            return;
        }
        bundleServiceData.putString("desc", desc);
        bundleServiceData.putInt("anonymity", anonymity);//取值只能1或者0 (1实名 0匿名)
        if (timetype < 0 || timetype > 4) {
            ToastUtils.shortToast("时间类型错误");
            isClickNext = false;
            return;
        }
        bundleServiceData.putInt("timetype", timetype);
        if (timetype == 0) {
            if (starttime == -1 || endtime == -1) {
                ToastUtils.shortToast("请选择闲置时间标签");
                isClickNext = false;
                return;
            }
            if (starttime < System.currentTimeMillis() + 2 * 60 * 60 * 1000) {
                ToastUtils.shortToast("开始时间必须大于当前时间两个小时");
                isClickNext = false;
                return;
            }
            if (endtime <= starttime) {
                ToastUtils.shortToast("结束时间必须大于开始时间");
                isClickNext = false;
                return;
            }
        }
        bundleServiceData.putLong("starttime", starttime);
        bundleServiceData.putLong("endtime", endtime);
        final ArrayList<String> imgUrl = new ArrayList<String>();
        bundleServiceData.putStringArrayList("pic", imgUrl);
        final ArrayList<String> addedPicTempPath = mSaplAddPic.getAddedPicTempPath();
        if (addedPicTempPath.size() <= 0) {
//            ToastUtils.shortToast("至少上传一张图片");
//            return;
            intentPublishServiceAddInfoActivity.putExtras(bundleServiceData);
            mActivity.startActivity(intentPublishServiceAddInfoActivity);
            isClickNext = false;
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
                        isClickNext = false;
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
                        isClickNext = false;
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
        mCurrentChooseYear = mChooseDateTimePicker.getCurrentChooseYear();
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
        calendar.set(Calendar.YEAR, mCurrentChooseYear);
        calendar.set(Calendar.MONTH, mCurrentChooseMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, mCurrentChooseDay);
        calendar.set(Calendar.HOUR_OF_DAY, mCurrentChooseHour);
        calendar.set(Calendar.MINUTE, mCurrentChooseMinute);
        return calendar.getTimeInMillis();
    }

    public void deleteCustomTime(View v) {
        setDeleteCustomTimeIconVisibility(View.GONE);
        setIdleTimeItemBg(R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected);
        mActivityPublishServiceBaseinfoBinding.tvPublicServiceTime.setText("选择下面标签");
        mActivityPublishServiceBaseinfoBinding.tvPublicServiceTime.setTextColor(0xffCCCCCC);
        starttime = -1;
        endtime = -1;
        starttimeStr = "";
        endtimeStr = "";
        timetype = 0;
    }

    public void chooseCustomIdleTime(View v) {
        setSetStartTimeAndEndTimeLayerVisibility(View.VISIBLE);
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日-hh:mm");
        String tmpStartTimeStr = "开始时间";
        if (starttime != -1) {
            tmpStartTimeStr = sdf.format(starttime);
        }
        String tmpEndTimeStr = "结束时间";
        if (endtime != -1) {
            tmpEndTimeStr = sdf.format(endtime);
        }
        mActivityPublishServiceBaseinfoBinding.tvStartTime.setText(tmpStartTimeStr);
        mActivityPublishServiceBaseinfoBinding.tvEndTime.setText(tmpEndTimeStr);
    }

    public void okChooseIdleStartTimeAndEndTime(View v) {
        if (starttime == -1 || endtime == -1) {
            ToastUtils.shortToast("请填写开始时间和结束时间");
            return;
        }
        if (starttime < System.currentTimeMillis()) {
            ToastUtils.shortToast("开始时间必须大于当前时间");
            return;
        }
        if (endtime < starttime) {
            ToastUtils.shortToast("结束时间必须大于开始时间");
            return;
        }
        setDeleteCustomTimeIconVisibility(View.VISIBLE);
        setSetStartTimeAndEndTimeLayerVisibility(View.GONE);
        timetype = SERVICE_TIMETYPE_USER_DEFINED;
        mActivityPublishServiceBaseinfoBinding.tvPublicServiceTime.setText(starttimeStr + " " + endtimeStr);
        mActivityPublishServiceBaseinfoBinding.tvPublicServiceTime.setTextColor(0xff333333);
        setIdleTimeItemBg(R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected, R.drawable.shape_idletime_label_bg_unselected);
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

    private static final String publishWayTitle = "发布方式（实名/匿名）";
    private static final String publishWayContent = "1 匿名发布任务是什么？\n" +
            "用户在自己不便于公开身份发布服务或者需求时，可使用匿名功能进行发布。当你匿名发布服务或者需求后，其他用户浏览该任务时，则无法查看你的姓名、头像等个人信息；通过搜索、点击头像等方式查看你的个人信息时，你匿名状态下发布的任务将会被隐藏。\n" +
            "\n" +
            "2 匿名状态有什么影响？\n" +
            "匿名发布任务后，其他用户暂时看不到你的姓名、头像等个人信息。在合作意向达成后，匿名状态解除，交易双方恢复实名状态，可查看双方的个人信息。\n" +
            "\n" +
            "匿名发布不影响交易流程，但可能会降低其他用户对你发布服务或者需求的信任程度。";

    /**
     * 发布方式的问号
     *
     * @param v
     */
    public void openPublishWayInfo(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_SERVICE_PUBLISH_MANNER_QUESTION);

        DialogUtils.showDialogOne(mActivity, new DialogUtils.DialogCallUnderStandBack() {
            @Override
            public void OkDown() {

            }
        }, publishWayContent, publishWayTitle);
    }

    private int chooseDateTimeLayerVisibility = View.GONE;
    private int setStartTimeAndEndTimeLayerVisibility = View.GONE;
    private int deleteCustomTimeIconVisibility = View.GONE;
    private int gotoSkillManagerVisibility;

    @Bindable
    public int getGotoSkillManagerVisibility() {
        return gotoSkillManagerVisibility;
    }

    public void setGotoSkillManagerVisibility(int gotoSkillManagerVisibility) {
        this.gotoSkillManagerVisibility = gotoSkillManagerVisibility;
        notifyPropertyChanged(BR.gotoSkillManagerVisibility);
    }

    @Bindable
    public int getDeleteCustomTimeIconVisibility() {
        return deleteCustomTimeIconVisibility;
    }

    public void setDeleteCustomTimeIconVisibility(int deleteCustomTimeIconVisibility) {
        this.deleteCustomTimeIconVisibility = deleteCustomTimeIconVisibility;
        notifyPropertyChanged(BR.deleteCustomTimeIconVisibility);
    }

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
