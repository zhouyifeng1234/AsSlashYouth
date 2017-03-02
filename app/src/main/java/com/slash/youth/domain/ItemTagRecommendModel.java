package com.slash.youth.domain;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ItemTagRecommendBinding;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.viewmodel.DemandDetailModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.DistanceUtils;

import java.text.SimpleDateFormat;

/**
 * Created by zhouyifeng on 2017/3/2.
 */
public class ItemTagRecommendModel extends BaseObservable {

    ItemTagRecommendBinding mItemTagRecommendBinding;

    public ItemTagRecommendModel(ItemTagRecommendBinding itemTagRecommendBinding) {
        this.mItemTagRecommendBinding = itemTagRecommendBinding;
    }

    TagRecommendList.TagRecommendInfo tagRecommendInfo;
    String[] optionalPriceUnit = new String[]{"次", "个", "幅", "份", "单", "小时", "分钟", "天", "其他"};

    public void setData(TagRecommendList.TagRecommendInfo data) {
        this.tagRecommendInfo = data;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        //头像和名字需要进行匿名判断
        if (tagRecommendInfo.anonymity == 1) {//实名
            BitmapKit.bindImage(mItemTagRecommendBinding.ivServiceUserAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + tagRecommendInfo.avatar);
            setServiceUsername(tagRecommendInfo.name);
        } else {//匿名
            mItemTagRecommendBinding.ivServiceUserAvatar.setImageResource(R.mipmap.anonymity_avater);
            String anonymityName;
            if (TextUtils.isEmpty(tagRecommendInfo.name)) {
                anonymityName = "XXX";
            } else {
                anonymityName = tagRecommendInfo.name.substring(0, 1) + "XX";
            }
            setServiceUsername(anonymityName);
        }

        if (tagRecommendInfo.isauth == 0) {
            //未认证
            setAuthVisibility(View.GONE);
        } else {
            //已认证
            setAuthVisibility(View.VISIBLE);
        }
        setServiceTitle(tagRecommendInfo.title);

        //pattern  1线下 0线上
        if (tagRecommendInfo.pattern == 0) {//线上
            setPatternText("线上");
        } else {//线下
            setPatternText("线下");
        }

        if (tagRecommendInfo.pattern == 0) {//线上
            setServicePlace("不限城市");
            mItemTagRecommendBinding.tvDistance.setVisibility(View.GONE);
        } else {//线下
            if (TextUtils.isEmpty(tagRecommendInfo.place)) {
                setServicePlace("火星村");
                mItemTagRecommendBinding.tvDistance.setVisibility(View.GONE);
            } else {
                setServicePlace(tagRecommendInfo.place);
            }
        }

        double distance = DistanceUtils.getDistance(SlashApplication.getCurrentLatitude(), SlashApplication.getCurrentLongitude(), tagRecommendInfo.lat, tagRecommendInfo.lng);
        setDistanceStr("距您 " + distance + "KM");

        //data.type为2是服务，其它的为需求
        if (tagRecommendInfo.type == 2) {//服务
            if (tagRecommendInfo.quoteunit == 9) {
                setQuote("报价:" + (int) tagRecommendInfo.quote + "元");
            } else if (tagRecommendInfo.quoteunit < 9 && tagRecommendInfo.quoteunit > 0) {
                setQuote("报价:" + (int) tagRecommendInfo.quote + "元/" + optionalPriceUnit[tagRecommendInfo.quoteunit - 1]);
            } else {//如果数据正确，这种情况应该不存在
                setQuote("报价:" + (int) tagRecommendInfo.quote + "元");
            }
            //因为发布服务时，去掉了具体的开始时间和结束时间，所以现在只有 周末、下班后这种模糊时间
            int timetype = tagRecommendInfo.timetype;
            if (timetype == 0) {//这种情况应该不存在了
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                String starttimeStr = sdf.format(tagRecommendInfo.starttime);
                String endtimeStr = sdf.format(tagRecommendInfo.endtime);
                setIdleTime(starttimeStr + "-" + endtimeStr);
            } else if (timetype == 1) {//下班后
                setIdleTime("下班后");
            } else if (timetype == 2) {//周末
                setIdleTime("周末");
            } else if (timetype == 3) {//下班后及周末
                setIdleTime("下班后及周末");
            } else if (timetype == 4) {//随时
                setIdleTime("随时");
            }

            //instalment 1开启，0关闭
            if (tagRecommendInfo.instalment == 1) {
                setInstalmentVisibility(View.VISIBLE);
                mItemTagRecommendBinding.tvInstalmentText.setText("分期到账");
            } else {
                setInstalmentVisibility(View.GONE);
            }

        } else {//需求，这里希求的type可能是0
            if (tagRecommendInfo.quote <= 0) {
                setQuote("服务方报价");
            } else {
                setQuote("报价:¥" + (int) tagRecommendInfo.quote);
            }

            //需求不需要时间和时间icon
            mItemTagRecommendBinding.ivTimeIcon.setVisibility(View.GONE);
            mItemTagRecommendBinding.tvIdleTime.setVisibility(View.GONE);

            if (tagRecommendInfo.instalment == 1) {//开启分期
                setInstalmentVisibility(View.GONE);
            } else {//未开启
                setInstalmentVisibility(View.VISIBLE);
                mItemTagRecommendBinding.tvInstalmentText.setText("一次性到账");
            }
        }
    }

    private int authVisibility;
    private String serviceUsername;
    private String serviceTitle;
    private String quote;//报价:¥300
    private String idleTime;//闲置时间:2016年9月18日 8:00
    private String patternText;//1线下 0线上
    private int instalmentVisibility;
    private String servicePlace;//苏州市圆融星座苏州市圆融星座苏州市圆融星座.
    private String distanceStr;//&lt; 4.5KM

    @Bindable
    public String getDistanceStr() {
        return distanceStr;
    }

    public void setDistanceStr(String distanceStr) {
        this.distanceStr = distanceStr;
        notifyPropertyChanged(BR.distanceStr);
    }

    @Bindable
    public String getServicePlace() {
        return servicePlace;
    }

    public void setServicePlace(String servicePlace) {
        this.servicePlace = servicePlace;
        notifyPropertyChanged(BR.servicePlace);
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
    public int getAuthVisibility() {
        return authVisibility;
    }

    public void setAuthVisibility(int authVisibility) {
        this.authVisibility = authVisibility;
        notifyPropertyChanged(BR.authVisibility);
    }

    @Bindable
    public String getServiceUsername() {
        return serviceUsername;
    }

    public void setServiceUsername(String serviceUsername) {
        this.serviceUsername = serviceUsername;
        notifyPropertyChanged(BR.serviceUsername);
    }

    @Bindable
    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
        notifyPropertyChanged(BR.serviceTitle);
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
    public String getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(String idleTime) {
        this.idleTime = idleTime;
        notifyPropertyChanged(BR.idleTime);
    }
}
