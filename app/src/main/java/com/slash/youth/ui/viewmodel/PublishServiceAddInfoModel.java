package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceAddinfoBinding;
import com.slash.youth.domain.PublishServiceResultBean;
import com.slash.youth.engine.ServiceEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MapActivity;
import com.slash.youth.ui.activity.PublishServiceBaseInfoActivity;
import com.slash.youth.ui.activity.PublishServiceSucceddActivity;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.ui.view.SlashAddLabelsLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/11/9.
 */
public class PublishServiceAddInfoModel extends BaseObservable {

    ActivityPublishServiceAddinfoBinding mActivityPublishServiceAddinfoBinding;
    Activity mActivity;

    boolean isOnline = true;//“线上”或者“线下”，默认为线上
    boolean isInstalment = true;//是否开启分期付，默认为true,开启
    public SlashAddLabelsLayout mSallSkillLabels;
    String[] disputeHandingTypes = new String[]{
            "平台方式", "协商处理"
    };//纠纷处理方式
    public int checkedDisputeHandingTypeIndex = 0;//选择的纠纷处理方式
    private NumberPicker mNpChoosePriceUnit;
    String[] optionalPriceUnit;
    private String mChoosePriceUnit;
    private int quoteunit = 9;
    private double lng;
    private double lat;

    public PublishServiceAddInfoModel(ActivityPublishServiceAddinfoBinding activityPublishServiceAddinfoBinding, Activity activity) {
        this.mActivityPublishServiceAddinfoBinding = activityPublishServiceAddinfoBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {
        optionalPriceUnit = new String[]{"次", "个", "幅", "份", "单", "小时", "分钟", "天", "其他"};
    }

    private void initView() {
        mNpChoosePriceUnit = mActivityPublishServiceAddinfoBinding.npChoosePriceUnit;

        mActivityPublishServiceAddinfoBinding.svPublishServiceLabels.setVerticalScrollBarEnabled(false);
        mSallSkillLabels = mActivityPublishServiceAddinfoBinding.sallPublishServiceAddedSkilllabels;
        mSallSkillLabels.setActivity(mActivity);
        mSallSkillLabels.initSkillLabels();
    }

    public void gotoBack(View v) {
        mActivity.finish();
    }

    public void openLabelsActivity(View v) {
        Intent intentSubscribeActivity = new Intent(CommonUtils.getContext(), SubscribeActivity.class);
        ArrayList<String> addedSkillLabels = mSallSkillLabels.getAddedSkillLabels();
        intentSubscribeActivity.putStringArrayListExtra("addedSkillLabels", addedSkillLabels);
        mActivity.startActivityForResult(intentSubscribeActivity, 10);
    }

    //打开或者关闭分期
    public void toggleInstalment(View v) {
        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) mActivityPublishServiceAddinfoBinding.ivPublishServiceInstalmentHandle.getLayoutParams();
        if (isInstalment) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            mActivityPublishServiceAddinfoBinding.ivPublishServiceInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle_weijihuo);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            mActivityPublishServiceAddinfoBinding.ivPublishServiceInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle);
        }
        mActivityPublishServiceAddinfoBinding.ivPublishServiceInstalmentHandle.setLayoutParams(layoutParams);
        isInstalment = !isInstalment;
    }

    public void checkOnline(View v) {
        isOnline = true;
        setOfflineItemVisibility(View.GONE);
        mActivityPublishServiceAddinfoBinding.ivPublishServiceOnlineIcon.setImageResource(R.mipmap.pitchon_btn);
        mActivityPublishServiceAddinfoBinding.ivPublishServiceOfflineIcon.setImageResource(R.mipmap.default_btn);
    }

    public void checkOffline(View v) {
        isOnline = false;
        setOfflineItemVisibility(View.VISIBLE);
        mActivityPublishServiceAddinfoBinding.ivPublishServiceOnlineIcon.setImageResource(R.mipmap.default_btn);
        mActivityPublishServiceAddinfoBinding.ivPublishServiceOfflineIcon.setImageResource(R.mipmap.pitchon_btn);
    }

    public void chooseDisputeHandingType(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setSingleChoiceItems(disputeHandingTypes, checkedDisputeHandingTypeIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkedDisputeHandingTypeIndex = which;
                mActivityPublishServiceAddinfoBinding.tvDisputeHandingType.setText(disputeHandingTypes[which]);
                mActivityPublishServiceAddinfoBinding.tvDisputeHandingType.setTextColor(0xff333333);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void publish(View v) {
        //发布成功以后才能跳转到成功页面
//        Intent intentPublishServiceSuccessActivity = new Intent(CommonUtils.getContext(), PublishServiceSucceddActivity.class);
//        mActivity.startActivity(intentPublishServiceSuccessActivity);
//        mActivity.finish();
//        if (PublishServiceBaseInfoActivity.activity != null) {
//            PublishServiceBaseInfoActivity.activity.finish();
//            PublishServiceBaseInfoActivity.activity = null;
//        }

        Bundle bundleServiceData = mActivity.getIntent().getExtras();
        String title = bundleServiceData.getString("title");
        String desc = bundleServiceData.getString("desc");
        int anonymity = bundleServiceData.getInt("anonymity");
        int timetype = bundleServiceData.getInt("timetype");
        long starttime = bundleServiceData.getLong("starttime");
        long endtime = bundleServiceData.getLong("endtime");
        ArrayList<String> listPic = bundleServiceData.getStringArrayList("pic");


        ArrayList<String> addedSkillLabels = mSallSkillLabels.getAddedSkillLabels();
        double quote;
        try {
            quote = Double.parseDouble(mActivityPublishServiceAddinfoBinding.etServiceQuote.getText().toString());
        } catch (Exception ex) {
            quote = 0;
        }

        int instalment = isInstalment == true ? 1 : 0;//1开启，0关闭
        int pattern = isOnline == true ? 0 : 1;//1线下 0线上
        int bp = checkedDisputeHandingTypeIndex + 1;//1平台 2协商
        String place = getLocationAddress();

        ServiceEngine.publishService(new BaseProtocol.IResultExecutor<PublishServiceResultBean>() {
            @Override
            public void execute(PublishServiceResultBean dataBean) {
                LogKit.v("发布成功，id:" + dataBean.data.id);
                //发布成功以后才能跳转到成功页面
                Intent intentPublishServiceSuccessActivity = new Intent(CommonUtils.getContext(), PublishServiceSucceddActivity.class);
                mActivity.startActivity(intentPublishServiceSuccessActivity);
                mActivity.finish();
                if (PublishServiceBaseInfoActivity.activity != null) {
                    PublishServiceBaseInfoActivity.activity.finish();
                    PublishServiceBaseInfoActivity.activity = null;
                }
            }

            @Override
            public void executeResultError(String result) {

            }
        }, title, addedSkillLabels, starttime, endtime, anonymity, desc, timetype, listPic, instalment, bp, pattern, place, lng, lat, quote, quoteunit);
    }

    public void openMapActivity(View v) {
        Intent intentMapActivity = new Intent(CommonUtils.getContext(), MapActivity.class);
        mActivity.startActivityForResult(intentMapActivity, 20);
    }

    public void setLocationInfo(String address, double lng, double lat) {
        mActivityPublishServiceAddinfoBinding.etPublishServiceAddress.setText(address);
        this.lng = lng;
        this.lat = lat;
    }

    public String getLocationAddress() {
        return mActivityPublishServiceAddinfoBinding.etPublishServiceAddress.getText().toString();
    }

    public void openChoosePriceUnit(View v) {
        setChoosePriceUnitLayerVisibility(View.VISIBLE);
        mNpChoosePriceUnit.setDisplayedValues(optionalPriceUnit);
        mNpChoosePriceUnit.setMaxValue(optionalPriceUnit.length - 1);
        mNpChoosePriceUnit.setMinValue(0);
        mNpChoosePriceUnit.setValue(1);
    }

    public void okChoosePriceUnit(View v) {
        setChoosePriceUnitLayerVisibility(View.INVISIBLE);
        int value = mNpChoosePriceUnit.getValue();
        mChoosePriceUnit = optionalPriceUnit[value];
        if (value < 8) {
            setPriceUnit("元/" + mChoosePriceUnit);
        } else {
            setPriceUnit("元");
        }
        quoteunit = value + 1;
    }


    private int offlineItemVisibility = View.GONE;
    private int choosePriceUnitLayerVisibility = View.GONE;
    private String priceUnit = "元";

    @Bindable
    public int getOfflineItemVisibility() {
        return offlineItemVisibility;
    }

    public void setOfflineItemVisibility(int offlineItemVisibility) {
        this.offlineItemVisibility = offlineItemVisibility;
        notifyPropertyChanged(BR.offlineItemVisibility);
    }

    @Bindable
    public int getChoosePriceUnitLayerVisibility() {
        return choosePriceUnitLayerVisibility;
    }

    public void setChoosePriceUnitLayerVisibility(int choosePriceUnitLayerVisibility) {
        this.choosePriceUnitLayerVisibility = choosePriceUnitLayerVisibility;
        notifyPropertyChanged(BR.choosePriceUnitLayerVisibility);
    }

    @Bindable
    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
        notifyPropertyChanged(BR.priceUnit);
    }
}
