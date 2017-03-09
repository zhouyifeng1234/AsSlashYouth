package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandBaseinfoBinding;
import com.slash.youth.domain.DemandDetailBean;
import com.slash.youth.domain.UploadFileResultBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.PublishDemandAddInfoActivity;
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
 * 发布需求页面 第二版
 * Created by zhouyifeng on 2016/10/17.
 */
public class PublishDemandBaseInfoModel extends BaseObservable {

    public static final int PUBLISH_ANONYMITY_ANONYMOUS = 0;//匿名发布
    public static final int PUBLISH_ANONYMITY_REALNAME = 1;//实名发布

    //    String isUpdate = "";//是否为修改需求
    ActivityPublishDemandBaseinfoBinding mActivityPublishDemandBaseinfoBinding;
    Activity mActivity;
    public SlashAddPicLayout mSaplAddPic;
    public SlashDateTimePicker mChooseDateTimePicker;

    int anonymity = PUBLISH_ANONYMITY_REALNAME;//是否匿名发布，默认为实名发布
    String demandTitle = "";
    String demandDesc = "";
    long startTime = -1;
    private int mCurrentChooseYear;
    private int mCurrentChooseMonth;
    private int mCurrentChooseDay;
    private int mCurrentChooseHour;
    private int mCurrentChooseMinute;

    DemandDetailBean demandDetailBean;

    public PublishDemandBaseInfoModel(ActivityPublishDemandBaseinfoBinding activityPublishDemandBaseinfoBinding, Activity activity) {
        this.mActivityPublishDemandBaseinfoBinding = activityPublishDemandBaseinfoBinding;
        this.mActivity = activity;
        initData();
        initView();
        initListener();
    }

    private void initData() {

//        isUpdate = mActivity.getIntent().getStringExtra("update");
//        if (TextUtils.equals(isUpdate, "update")) {
//            ToastUtils.shortToast("修改需求");
//            //然后可通过需求详情接口获取需求的数据，并填充到界面上
//        }
        mSaplAddPic = mActivityPublishDemandBaseinfoBinding.saplPublishDemandAddpic;
        demandDetailBean = (DemandDetailBean) mActivity.getIntent().getSerializableExtra("demandDetailBean");
        if (demandDetailBean != null) {
            mActivityPublishDemandBaseinfoBinding.tvPublishDemandText.setText("修改需求");
            mActivityPublishDemandBaseinfoBinding.tvPublishSuccessText.setText("修改成功");
            loadDemandDetailData();
        }
    }

    private void initView() {
        mChooseDateTimePicker = mActivityPublishDemandBaseinfoBinding.sdtpPublishDemandChooseDatetime;

        mSaplAddPic.setActivity(mActivity);
        mSaplAddPic.initPic();
    }

    private void initListener() {
        mActivityPublishDemandBaseinfoBinding.etPublishDemandDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textCount = s.length();
                mActivityPublishDemandBaseinfoBinding.tvDescTextCount.setText(textCount + "/300");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**
     * 修改需求时回填需求详情数据
     */
    private void loadDemandDetailData() {
        DemandDetailBean.Demand demand = demandDetailBean.data.demand;
        //回填匿名实名
        if (demand.anonymity == 0) {//匿名
            checkAnonymous(null);
        } else if (demand.anonymity == 1) {//实名
            checkRealName(null);
        }
        //回填标题
        mActivityPublishDemandBaseinfoBinding.etPublishDemandTitle.setText(demand.title);
        //回填描述
        mActivityPublishDemandBaseinfoBinding.etPublishDemandDesc.setText(demand.desc);
        int descWordsCount = demand.desc.length();
        mActivityPublishDemandBaseinfoBinding.tvDescTextCount.setText(descWordsCount + "/300");
        //回填开始时间
        if (demand.starttime <= 0) {
            startTime = -1;
        } else {
            startTime = demand.starttime;
        }
        if (startTime > 0) {
            SimpleDateFormat sdfStartTime = new SimpleDateFormat("MM月dd日-HH:mm");
            setStartTimeStr(sdfStartTime.format(startTime));
            setDeleteStartTimeIconVisibility(View.VISIBLE);
            setSetStartTimeBtnVisibility(View.GONE);
        }
        //回填图片
        String[] picFileIds = demand.pic.split(",");
        final String picCachePath = mActivity.getCacheDir().getAbsoluteFile() + "/picache/";
        File cacheDir = new File(picCachePath);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        for (String fileId : picFileIds) {
            DemandEngine.downloadFile(new BaseProtocol.IResultExecutor<byte[]>() {
                @Override
                public void execute(byte[] dataBean) {

                    Bitmap bitmap = BitmapFactory.decodeByteArray(dataBean, 0, dataBean.length);
                    if (bitmap != null) {
                        File tempFile = new File(picCachePath + System.currentTimeMillis() + ".jpeg");
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(tempFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
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

    //选择实名发布
    public void checkRealName(View v) {
        mActivityPublishDemandBaseinfoBinding.ivPublicDemandRealnameIcon.setImageResource(R.mipmap.pitchon_btn);
        mActivityPublishDemandBaseinfoBinding.ivPublishDemandAnonymousIcon.setImageResource(R.mipmap.default_btn);
        anonymity = PUBLISH_ANONYMITY_REALNAME;
    }

    //选择匿名发布
    public void checkAnonymous(View v) {
        mActivityPublishDemandBaseinfoBinding.ivPublicDemandRealnameIcon.setImageResource(R.mipmap.default_btn);
        mActivityPublishDemandBaseinfoBinding.ivPublishDemandAnonymousIcon.setImageResource(R.mipmap.pitchon_btn);
        anonymity = PUBLISH_ANONYMITY_ANONYMOUS;
    }

    public void setStartTime(View v) {
        if (TextUtils.isEmpty(startTimeStr)) {
            setChooseDateTimeLayerVisibility(View.VISIBLE);
        }
    }

    public void deleteStartTime(View v) {
        startTime = -1;
        setStartTimeStr("");
        setDeleteStartTimeIconVisibility(View.GONE);
        setSetStartTimeBtnVisibility(View.VISIBLE);
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
        setStartTimeStr(dateTimeStr);
        convertTimeToMillis();
        setDeleteStartTimeIconVisibility(View.VISIBLE);
        setSetStartTimeBtnVisibility(View.GONE);
//        LogKit.v(startTime + "");
//        LogKit.v(System.currentTimeMillis() + "");
    }

    public void convertTimeToMillis() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, mCurrentChooseYear);
        calendar.set(Calendar.MONTH, mCurrentChooseMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, mCurrentChooseDay);
        calendar.set(Calendar.HOUR_OF_DAY, mCurrentChooseHour);
        calendar.set(Calendar.MINUTE, mCurrentChooseMinute);
        startTime = calendar.getTimeInMillis();
    }

    boolean isClickNext = false;

    //下一步操作
    public void nextStep(View v) {
        if (isClickNext) {
            return;
        }
        isClickNext = true;

        final Intent intentPublishDemandAddInfoActivity = new Intent(CommonUtils.getContext(), PublishDemandAddInfoActivity.class);

        //标记是否为修改需求
//        intentPublishDemandAddInfoActivity.putExtra("update", isUpdate);
        if (demandDetailBean != null) {
            intentPublishDemandAddInfoActivity.putExtra("demandDetailBean", demandDetailBean);
        }
        //设置发布需求的相关信息
        final Bundle publishDemandData = new Bundle();
        publishDemandData.putInt("anonymity", anonymity);
        demandTitle = mActivityPublishDemandBaseinfoBinding.etPublishDemandTitle.getText().toString();
        if (demandTitle.length() < 5 || demandTitle.length() > 20) {
            ToastUtils.shortToast("标题必须为5-20字之间");
            isClickNext = false;
            return;
        }
        publishDemandData.putString("demandTitle", demandTitle);
        demandDesc = mActivityPublishDemandBaseinfoBinding.etPublishDemandDesc.getText().toString();
        if (demandDesc.length() <= 0) {
            ToastUtils.shortToast("请输入需求描述");
            isClickNext = false;
            return;
        } else if (demandDesc.length() < 5) {
            ToastUtils.shortToast("需求描述必须是5-300字之间");
            isClickNext = false;
            return;
        } else if (demandDesc.length() > 300) {
            ToastUtils.shortToast("需求描述不能超过300字");
            isClickNext = false;
            return;
        }
        publishDemandData.putString("demandDesc", demandDesc);
//        if (startTime == -1) {
//            ToastUtils.shortToast("请选择开始时间");
//            return;
//        }
        if (startTime != -1) {
            if (startTime < System.currentTimeMillis() + 2 * 60 * 60 * 1000) {
                ToastUtils.shortToast("开始时间必须大于当前时间两个小时");
                isClickNext = false;
                return;
            }
        }
        publishDemandData.putLong("startTime", startTime);
        //上传选择的图片，并把服务端返回的fileId提交上去
        final ArrayList<String> imgUrl = new ArrayList<String>();
        publishDemandData.putStringArrayList("pic", imgUrl);
        final ArrayList<String> addedPicTempPath = mSaplAddPic.getAddedPicTempPath();
        if (addedPicTempPath.size() <= 0) {
//            ToastUtils.shortToast("至少上传一张图片");
//            return;
            isClickNext = false;
            intentPublishDemandAddInfoActivity.putExtras(publishDemandData);
            mActivity.startActivity(intentPublishDemandAddInfoActivity);
            return;
        }
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
                        intentPublishDemandAddInfoActivity.putExtras(publishDemandData);
                        mActivity.startActivity(intentPublishDemandAddInfoActivity);
                    }
                }

                @Override
                public void executeResultError(String result) {
                    LogKit.v(filePath + ":上传失败");
                    uploadCount[0]++;
                    LogKit.v("uploadCount:" + uploadCount[0]);
                    if (uploadCount[0] >= addedPicTempPath.size()) {
                        isClickNext = false;
                        intentPublishDemandAddInfoActivity.putExtras(publishDemandData);
                        mActivity.startActivity(intentPublishDemandAddInfoActivity);
                    }
                }
            }, filePath);
        }

//        intentPublishDemandAddInfoActivity.putExtras(publishDemandData);
//        mActivity.startActivity(intentPublishDemandAddInfoActivity);
    }

    //返回操作
    public void gotoBack(View v) {
        mActivity.finish();
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
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_REQUIREMENT_PUBLISH_MANNER_QUESTION);

        DialogUtils.showDialogOne(mActivity, new DialogUtils.DialogCallUnderStandBack() {
            @Override
            public void OkDown() {

            }
        }, publishWayContent, publishWayTitle);
    }

    private int chooseDateTimeLayerVisibility = View.GONE;
    private String startTimeStr;

    private int setStartTimeBtnVisibility = View.VISIBLE;
    private int deleteStartTimeIconVisibility = View.GONE;

    @Bindable
    public int getSetStartTimeBtnVisibility() {
        return setStartTimeBtnVisibility;
    }

    public void setSetStartTimeBtnVisibility(int setStartTimeBtnVisibility) {
        this.setStartTimeBtnVisibility = setStartTimeBtnVisibility;
        notifyPropertyChanged(BR.setStartTimeBtnVisibility);
    }

    @Bindable
    public int getDeleteStartTimeIconVisibility() {
        return deleteStartTimeIconVisibility;
    }

    public void setDeleteStartTimeIconVisibility(int deleteStartTimeIconVisibility) {
        this.deleteStartTimeIconVisibility = deleteStartTimeIconVisibility;
        notifyPropertyChanged(BR.deleteStartTimeIconVisibility);
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
    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
        notifyPropertyChanged(BR.startTimeStr);
    }
}
