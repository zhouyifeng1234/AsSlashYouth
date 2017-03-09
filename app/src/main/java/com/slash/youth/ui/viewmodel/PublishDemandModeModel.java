package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import com.amap.api.maps2d.model.Marker;
import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandModeBinding;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MapActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/21.
 */
public class PublishDemandModeModel extends BaseObservable {

    public static final int OFFLINE_COST_TYPE_EMPLOYER = 2;//线下消费方式：雇主请客
    public static final int OFFLINE_COST_TYPE_AA = 1;//线下消费方式：AA制

    ActivityPublishDemandModeBinding mActivityPublishDemandModeBinding;
    Activity mActivity;
    Bundle publishDemandDataBundle;
    int chooseOfflineCostType = OFFLINE_COST_TYPE_EMPLOYER;///线下消费方式,默认雇主请客
    boolean isOpenSafeBox = true;//默认开启保险箱
    boolean isOnlineDemand = true;//发布线上需求还是线下需求，默认为true,线上需求

    int demandOfflineItemVisibility;
    int demandPayItemVisibility;

    Marker mMarker;//尝试解决地图中间定位大头针在页面切换时消失的问题
    int aroundKilometersLayerVisibility = View.INVISIBLE;//选择周边几公里的浮层是否显示，默认为不显示
    private NumberPicker mNpAroundKilometers;
    String[] aroundKilometersArr = new String[]{"1公里", "2公里", "3公里", "5公里", "10公里", "20公里", "50公里"};
    private String chooseAroundKilometersValue;
    private int mChoosePublishType;


    public PublishDemandModeModel(ActivityPublishDemandModeBinding activityPublishDemandModeBinding, Activity activity, Bundle publishDemandDataBundle, Marker marker) {
        this.mActivityPublishDemandModeBinding = activityPublishDemandModeBinding;
        this.mActivity = activity;
        this.publishDemandDataBundle = publishDemandDataBundle;
        this.mMarker = marker;
        initView();
    }

    private void initView() {
        mNpAroundKilometers = mActivityPublishDemandModeBinding.npPublishDemandAroundKilometers;
        displayDemandModeItem();
        initData();
    }

    private void initData() {
        mNpAroundKilometers.setDisplayedValues(aroundKilometersArr);
        mNpAroundKilometers.setMinValue(0);
        mNpAroundKilometers.setMaxValue(aroundKilometersArr.length - 1);
        mNpAroundKilometers.setValue(2);
    }

    @Bindable
    public int getDemandPayItemVisibility() {
        return demandPayItemVisibility;
    }

    public void setDemandPayItemVisibility(int demandPayItemVisibility) {
        this.demandPayItemVisibility = demandPayItemVisibility;
        notifyPropertyChanged(BR.demandPayItemVisibility);
    }

    @Bindable
    public int getDemandOfflineItemVisibility() {
        return demandOfflineItemVisibility;
    }

    public void setDemandOfflineItemVisibility(int demandOfflineItemVisibility) {
        this.demandOfflineItemVisibility = demandOfflineItemVisibility;
        notifyPropertyChanged(BR.demandOfflineItemVisibility);
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void publishDemand(View v) {
        boolean isRealNamePublish = publishDemandDataBundle.getBoolean("isRealNamePublish");
        int choosePublishType = publishDemandDataBundle.getInt("choosePublishType");
        ArrayList<String> listTotalAddedLabels = publishDemandDataBundle.getStringArrayList("listTotalAddedTagsNames");
        int mStartDisplayMonth = publishDemandDataBundle.getInt("mStartDisplayMonth");
        int mStartDisplayDay = publishDemandDataBundle.getInt("mStartDisplayDay");
        int mStartDisplayHour = publishDemandDataBundle.getInt("mStartDisplayHour");
        int mStartDisplayMinute = publishDemandDataBundle.getInt("mStartDisplayMinute");
        int mEndDisplayMonth = publishDemandDataBundle.getInt("mEndDisplayMonth");
        int mEndDisplayDay = publishDemandDataBundle.getInt("mEndDisplayDay");
        int mEndDisplayHour = publishDemandDataBundle.getInt("mEndDisplayHour");
        int mEndDisplayMinute = publishDemandDataBundle.getInt("mEndDisplayMinute");
        boolean isCheckAllDay = publishDemandDataBundle.getBoolean("isCheckAllDay");

        String demandTitle = publishDemandDataBundle.getString("demandTitle");
        String demandDesc = publishDemandDataBundle.getString("demandDesc");
        ArrayList<String> listAddedPicTempPath = publishDemandDataBundle.getStringArrayList("listAddedPicTempPath");


//        DemandEngine.publishDemand(new OnPublishDemandFinished(), demandTitle, "开发", System.currentTimeMillis() + 86400000 + "", 2 + "", (isRealNamePublish ? 1 : 0) + "", demandDesc, "", 1 + "", (isOpenSafeBox ? 1 : 0) + "", 1 + "", (isOnlineDemand ? 0 : 1) + "", "苏州", "苏州工业园区", chooseOfflineCostType + "", 0.000000D + "", 0.000000D + "", 0 + "", 20 + "");

//        ToastUtils.shortToast("发布需求成功");
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
        displayDemandModeItem();
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
        displayDemandModeItem();
    }

    /**
     * 根据 用户所选择的 “咨询”还是“交付”类的需求，以及“线上”还是“线下”，进行相应需求条目的展示和隐藏
     */
    public void displayDemandModeItem() {
        setDemandOfflineItemVisibility(isOnlineDemand ? View.GONE : View.VISIBLE);
        mChoosePublishType = publishDemandDataBundle.getInt("choosePublishType");
        setDemandPayItemVisibility(mChoosePublishType == PublishDemandModel.PUBLISH_DEMAND_PAY ? View.VISIBLE : View.GONE);
    }

    @Bindable
    public int getAroundKilometersLayerVisibility() {
        return aroundKilometersLayerVisibility;
    }

    public void setAroundKilometersLayerVisibility(int aroundKilometersLayerVisibility) {
        this.aroundKilometersLayerVisibility = aroundKilometersLayerVisibility;
        notifyPropertyChanged(BR.aroundKilometersLayerVisibility);
    }

    @Bindable
    public String getChooseAroundKilometersValue() {
        return chooseAroundKilometersValue;
    }

    public void setChooseAroundKilometersValue(String chooseAroundKilometersValue) {
        this.chooseAroundKilometersValue = chooseAroundKilometersValue;
        notifyPropertyChanged(BR.chooseAroundKilometersValue);
    }

    public void openAroundKilometersLayer(View v) {
        setAroundKilometersLayerVisibility(View.VISIBLE);
    }

    public void okChooseAroundKilometers(View v) {
        setAroundKilometersLayerVisibility(View.INVISIBLE);
        int value = mNpAroundKilometers.getValue();
        setChooseAroundKilometersValue(aroundKilometersArr[value]);
    }

    public class OnPublishDemandFinished implements BaseProtocol.IResultExecutor<String> {

        @Override
        public void execute(String dataBean) {
            ToastUtils.shortToast(dataBean);
        }

        @Override
        public void executeResultError(String result) {

        }
    }
}
