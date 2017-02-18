package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ItemDetailRecommendDemandBinding;
import com.slash.youth.domain.DetailRecommendDemandList;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.DistanceUtils;

import java.text.SimpleDateFormat;

/**
 * Created by zhouyifeng on 2017/1/4.
 */
public class ItemDetailRecommendDemandModel extends BaseObservable {
    ItemDetailRecommendDemandBinding mItemDetailRecommendDemandBinding;
    Activity mActivity;
    DetailRecommendDemandList.RecommendDemandInfo mRecommendDemandInfo;

    public ItemDetailRecommendDemandModel(ItemDetailRecommendDemandBinding itemDetailRecommendDemandBinding, Activity activity, DetailRecommendDemandList.RecommendDemandInfo recommendDemandInfo) {
        this.mItemDetailRecommendDemandBinding = itemDetailRecommendDemandBinding;
        this.mActivity = activity;
        this.mRecommendDemandInfo = recommendDemandInfo;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        //头像和名字需要进行匿名判断
        if (mRecommendDemandInfo.anonymity == 1) {//实名
            BitmapKit.bindImage(mItemDetailRecommendDemandBinding.ivDemandUserAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + mRecommendDemandInfo.avatar);
            setDemandUsername(mRecommendDemandInfo.name);
        } else {
            mItemDetailRecommendDemandBinding.ivDemandUserAvatar.setImageResource(R.mipmap.anonymity_avater);
            String anonymityName;
            if (TextUtils.isEmpty(mRecommendDemandInfo.name)) {
                anonymityName = "XXX";
            } else {
                anonymityName = mRecommendDemandInfo.name.substring(0, 1) + "XX";
            }
            setDemandUsername(anonymityName);
        }

        if (mRecommendDemandInfo.isauth == 0) {//未认证
            setAuthVisibility(View.GONE);
        } else {//已认证
            setAuthVisibility(View.VISIBLE);
        }
        if (mRecommendDemandInfo.quote <= 0) {
            setQuote("服务方报价");
        } else {
            setQuote("报价:¥" + (int) mRecommendDemandInfo.quote);
        }
        setDemandTitle(mRecommendDemandInfo.title);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String starttimeStr = sdf.format(mRecommendDemandInfo.starttime);
        setDemandStartTime(starttimeStr);
        if (mRecommendDemandInfo.pattern == 0) {//线上
            setPatternText("线上");
        } else {//线下
            setPatternText("线下");
        }
        if (mRecommendDemandInfo.instalment == 1) {//开启分期
            setInstalmentVisibility(View.GONE);
        } else {//未开启
            setInstalmentVisibility(View.VISIBLE);
        }


        if (mRecommendDemandInfo.pattern == 0) {//线上
            setDemandPlace("不限城市");
            mItemDetailRecommendDemandBinding.tvDistance.setVisibility(View.GONE);
        } else {//线下
            if (TextUtils.isEmpty(mRecommendDemandInfo.place)) {
                setDemandPlace("火星村");
                mItemDetailRecommendDemandBinding.tvDistance.setVisibility(View.GONE);
            } else {
                setDemandPlace(mRecommendDemandInfo.place);
            }
        }

        double distance = DistanceUtils.getDistance(SlashApplication.getCurrentLatitude(), SlashApplication.getCurrentLongitude(), mRecommendDemandInfo.lat, mRecommendDemandInfo.lng);
        setDistanceStr("距您" + distance + "KM");
    }

    private int authVisibility;
    private String demandUsername;
    private String quote;//报价:¥300
    private String demandTitle;
    private String demandStartTime;//开始时间:2016年9月18日 8:00

    private String patternText;
    private int instalmentVisibility;

    private String demandPlace;
    private String distanceStr;//&lt; 4.5KM

    @Bindable
    public String getDemandPlace() {
        return demandPlace;
    }

    public void setDemandPlace(String demandPlace) {
        this.demandPlace = demandPlace;
        notifyPropertyChanged(BR.demandPlace);
    }

    @Bindable
    public String getDistanceStr() {
        return distanceStr;
    }

    public void setDistanceStr(String distanceStr) {
        this.distanceStr = distanceStr;
        notifyPropertyChanged(BR.distanceStr);
    }

    @Bindable
    public int getInstalmentVisibility() {
        return instalmentVisibility;
    }

    public void setInstalmentVisibility(int instalmentVisibility) {
        this.instalmentVisibility = instalmentVisibility;
        notifyPropertyChanged(BR.instalmentVisibility);
    }

    @Bindable
    public String getPatternText() {
        return patternText;
    }

    public void setPatternText(String patternText) {
        this.patternText = patternText;
        notifyPropertyChanged(BR.patternText);
    }

    @Bindable
    public String getDemandStartTime() {
        return demandStartTime;
    }

    public void setDemandStartTime(String demandStartTime) {
        this.demandStartTime = demandStartTime;
        notifyPropertyChanged(BR.demandStartTime);
    }

    @Bindable
    public String getDemandTitle() {
        return demandTitle;
    }

    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
        notifyPropertyChanged(BR.demandTitle);
    }

    @Bindable
    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
        notifyPropertyChanged(BR.quote);
    }

    @Bindable
    public String getDemandUsername() {
        return demandUsername;
    }

    public void setDemandUsername(String demandUsername) {
        this.demandUsername = demandUsername;
        notifyPropertyChanged(BR.demandUsername);
    }

    @Bindable
    public int getAuthVisibility() {
        return authVisibility;
    }

    public void setAuthVisibility(int authVisibility) {
        this.authVisibility = authVisibility;
        notifyPropertyChanged(BR.authVisibility);
    }
}
