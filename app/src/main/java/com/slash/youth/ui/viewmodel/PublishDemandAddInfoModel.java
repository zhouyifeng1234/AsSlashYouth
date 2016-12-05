package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandAddinfoBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.DemandDetailBean;
import com.slash.youth.domain.PublishDemandResultBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MapActivity;
import com.slash.youth.ui.activity.PublishDemandBaseInfoActivity;
import com.slash.youth.ui.activity.PublishDemandSuccessActivity;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.ui.view.SlashAddLabelsLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class PublishDemandAddInfoModel extends BaseObservable {

    ActivityPublishDemandAddinfoBinding mActivityPublishDemandAddinfoBinding;
    Activity mActivity;
    boolean isOnline = true;//“线上”或者“线下”，默认为线上
    boolean isInstalment = true;//是否开启分期付，默认为true,开启
    public SlashAddLabelsLayout mSallSkillLabels;
    String[] disputeHandingTypes = new String[]{
            "平台方式", "协商处理"
    };//纠纷处理方式
    public int checkedDisputeHandingTypeIndex = 0;//选择的纠纷处理方式
    private double lng;
    private double lat;
    DemandDetailBean demandDetailBean;

    public PublishDemandAddInfoModel(ActivityPublishDemandAddinfoBinding activityPublishDemandAddinfoBinding, Activity activity) {
        this.mActivityPublishDemandAddinfoBinding = activityPublishDemandAddinfoBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {
        demandDetailBean = (DemandDetailBean) mActivity.getIntent().getSerializableExtra("demandDetailBean");
        if (demandDetailBean != null) {
            loadDemandDetailData();
        }
    }

    private void initView() {
        mActivityPublishDemandAddinfoBinding.svPublishDemandLabels.setVerticalScrollBarEnabled(false);
        mSallSkillLabels = mActivityPublishDemandAddinfoBinding.sallPublishDemandAddedSkilllabels;
        mSallSkillLabels.setActivity(mActivity);
        mSallSkillLabels.initSkillLabels();
    }

    /**
     * 修改需求时回填需求详情数据
     */
    private void loadDemandDetailData() {
        DemandDetailBean.Demand demand = demandDetailBean.data.demand;
        //回填技能标签
        String[] tags = demand.tag.split(",");
        ArrayList<String> reloadLabels = new ArrayList<String>();
        for (String tag : tags) {
            String[] tagInfo = tag.split("-");
            String tagName;
            if (tagInfo.length == 3) {
                tagName = tagInfo[2];
            } else {
                tagName = tag;
            }
            reloadLabels.add(tagName);
        }
        mSallSkillLabels.reloadSkillLabels(reloadLabels);
        //回填报价
        mActivityPublishDemandAddinfoBinding.etDemandQuote.setText(demand.quote + "");
        //回填分期开关
        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentHandle.getLayoutParams();
        if (demand.instalment == 0) {//分期关闭
            isInstalment = false;
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle_weijihuo);
        } else if (demand.instalment == 1) {//分期开启
            isInstalment = true;
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle);
        }
        mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentHandle.setLayoutParams(layoutParams);
        //回填线上、线下
        if (demand.pattern == 0) {//线上
            checkOnline(null);
        } else if (demand.pattern == 1) {//线下
            checkOffline(null);
        }
        //线下地址
        mActivityPublishDemandAddinfoBinding.etPublishDemandAddress.setText(demand.place);
        lng = demand.lng;
        lat = demand.lat;
        //纠纷处理方式
        checkedDisputeHandingTypeIndex = demand.bp - 1;
        mActivityPublishDemandAddinfoBinding.tvDisputeHandingType.setText(disputeHandingTypes[checkedDisputeHandingTypeIndex]);
        mActivityPublishDemandAddinfoBinding.tvDisputeHandingType.setTextColor(0xff333333);
    }

    public void gotoBack(View v) {
        mActivity.finish();
    }

    public void publish(View v) {

        Bundle bundleDemandData = mActivity.getIntent().getExtras();
        int anonymity = bundleDemandData.getInt("anonymity");
        String demandTitle = bundleDemandData.getString("demandTitle");
        String demandDesc = bundleDemandData.getString("demandDesc");
        long startTime = bundleDemandData.getLong("startTime");
        ArrayList<String> listPic = bundleDemandData.getStringArrayList("pic");


        ArrayList<String> addedSkillLabels = mSallSkillLabels.getAddedSkillLabels();
        double quote;
        int offer;
        try {
            quote = Double.parseDouble(mActivityPublishDemandAddinfoBinding.etDemandQuote.getText().toString());
            offer = 0;
        } catch (Exception ex) {
            quote = 0;
            offer = 1;
        }
        int instalment = isInstalment == true ? 1 : 0;//1开启，0关闭
        int pattern = isOnline == true ? 0 : 1;//1线下 0线上
        int bp = checkedDisputeHandingTypeIndex + 1;//1平台 2协商
        String place = getLocationAddress();

        if (demandDetailBean != null) {//修改需求
            DemandEngine.updateDemand(new BaseProtocol.IResultExecutor<CommonResultBean>() {
                @Override
                public void execute(CommonResultBean dataBean) {
                    Intent intentPublishDemandSuccessActivity = new Intent(CommonUtils.getContext(), PublishDemandSuccessActivity.class);
                    intentPublishDemandSuccessActivity.putExtra("demandId", demandDetailBean.data.demand.id);
                    mActivity.startActivity(intentPublishDemandSuccessActivity);
                    mActivity.finish();
                    if (PublishDemandBaseInfoActivity.mActivity != null) {
                        PublishDemandBaseInfoActivity.mActivity.finish();
                        PublishDemandBaseInfoActivity.mActivity = null;
                    }
                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("修改需求失败:" + result);
                }
            }, demandDetailBean.data.demand.id + "", demandTitle, addedSkillLabels, startTime + "", anonymity + "", demandDesc, listPic, instalment + "", bp + "", pattern + "", place, place, lng + "", lat + "", offer + "", quote + "");
        } else {//发布需求
            DemandEngine.publishDemand(new BaseProtocol.IResultExecutor<PublishDemandResultBean>() {
                @Override
                public void execute(PublishDemandResultBean dataBean) {
                    Intent intentPublishDemandSuccessActivity = new Intent(CommonUtils.getContext(), PublishDemandSuccessActivity.class);
                    intentPublishDemandSuccessActivity.putExtra("demandId", dataBean.data.id);
                    mActivity.startActivity(intentPublishDemandSuccessActivity);
                    mActivity.finish();
                    if (PublishDemandBaseInfoActivity.mActivity != null) {
                        PublishDemandBaseInfoActivity.mActivity.finish();
                        PublishDemandBaseInfoActivity.mActivity = null;
                    }
                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("发布需求失败：" + result);
                }
            }, demandTitle, addedSkillLabels, startTime + "", anonymity + "", demandDesc, listPic, instalment + "", bp + "", pattern + "", place, place, lng + "", lat + "", offer + "", quote + "");
        }


//        Intent intentPublishDemandSuccessActivity = new Intent(CommonUtils.getContext(), PublishDemandSuccessActivity.class);
//        mActivity.startActivity(intentPublishDemandSuccessActivity);
//        mActivity.finish();
//        if (PublishDemandBaseInfoActivity.mActivity != null) {
//            PublishDemandBaseInfoActivity.mActivity.finish();
//            PublishDemandBaseInfoActivity.mActivity = null;
//        }
    }

    public void checkOnline(View v) {
        isOnline = true;
        setOfflineItemVisibility(View.GONE);
        mActivityPublishDemandAddinfoBinding.ivPublishDemandOnlineIcon.setImageResource(R.mipmap.pitchon_btn);
        mActivityPublishDemandAddinfoBinding.ivPublishDemandOfflineIcon.setImageResource(R.mipmap.default_btn);
    }

    public void checkOffline(View v) {
        isOnline = false;
        setOfflineItemVisibility(View.VISIBLE);
        mActivityPublishDemandAddinfoBinding.ivPublishDemandOnlineIcon.setImageResource(R.mipmap.default_btn);
        mActivityPublishDemandAddinfoBinding.ivPublishDemandOfflineIcon.setImageResource(R.mipmap.pitchon_btn);
    }

    public void toggleInstalment(View v) {
        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentHandle.getLayoutParams();
        if (isInstalment) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle_weijihuo);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle);
        }
        mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentHandle.setLayoutParams(layoutParams);
        isInstalment = !isInstalment;
    }

    public void openLabelsActivity(View v) {
        Intent intentSubscribeActivity = new Intent(CommonUtils.getContext(), SubscribeActivity.class);
        ArrayList<String> addedSkillLabels = mSallSkillLabels.getAddedSkillLabels();
        intentSubscribeActivity.putStringArrayListExtra("addedSkillLabels", addedSkillLabels);
        mActivity.startActivityForResult(intentSubscribeActivity, 10);
    }

    public void openMapActivity(View v) {
        Intent intentMapActivity = new Intent(CommonUtils.getContext(), MapActivity.class);
//        intentMapActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivityForResult(intentMapActivity, 20);
    }

    public void chooseDisputeHandingType(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setSingleChoiceItems(disputeHandingTypes, checkedDisputeHandingTypeIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkedDisputeHandingTypeIndex = which;
                mActivityPublishDemandAddinfoBinding.tvDisputeHandingType.setText(disputeHandingTypes[which]);
                mActivityPublishDemandAddinfoBinding.tvDisputeHandingType.setTextColor(0xff333333);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private int offlineItemVisibility = View.GONE;

    @Bindable
    public int getOfflineItemVisibility() {
        return offlineItemVisibility;
    }

    public void setOfflineItemVisibility(int offlineItemVisibility) {
        this.offlineItemVisibility = offlineItemVisibility;
        notifyPropertyChanged(BR.offlineItemVisibility);
    }

    public void setLocationAddress(String address, double lng, double lat) {
//        ToastUtils.shortToast(address);
        mActivityPublishDemandAddinfoBinding.etPublishDemandAddress.setText(address);
        this.lng = lng;
        this.lat = lat;
    }

    public String getLocationAddress() {
        return mActivityPublishDemandAddinfoBinding.etPublishDemandAddress.getText().toString();
    }
}
