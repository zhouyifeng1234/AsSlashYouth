package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.amap.api.maps2d.model.LatLng;
import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityDemandDetailBinding;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/10/24.
 */
public class DemandDetailModel extends BaseObservable {

    public static final int VISUAL_ANGLE_SERVICE = 10000;//服务者视角
    public static final int VISUAL_ANGLE_DAMAND = 10001;//需求者视角

    ActivityDemandDetailBinding mActivityDemandDetailBinding;
    Activity mActivity;
    private int current_visual_angle;//当前视角
    private boolean isDemandOffShelf;//需求是否下架
    private LatLng mCurrentLatlng;//发布需求时获取的经纬度，由服务端返回

    public DemandDetailModel(ActivityDemandDetailBinding activityDemandDetailBinding, Activity activity) {
        this.mActivityDemandDetailBinding = activityDemandDetailBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {
        current_visual_angle = VISUAL_ANGLE_SERVICE;//获取当前者视角，应该根据服务端数据来判断，如果是自己发的需求，就是需求者视角，如果不是自己发的，就是服务者视角
        isDemandOffShelf = false;//由服务端数据获取当前浏览的需求是否已经下架
    }


    private void initView() {
        if (current_visual_angle == VISUAL_ANGLE_SERVICE) {
            setTopShareBtnVisibility(View.VISIBLE);
            setTopDemandBtnVisibility(View.GONE);
            setBottomBtnServiceVisibility(View.VISIBLE);
            setBottomBtnDemandVisibility(View.GONE);
        } else if (current_visual_angle == VISUAL_ANGLE_DAMAND) {
            setTopShareBtnVisibility(View.GONE);
            setTopDemandBtnVisibility(View.VISIBLE);
            setBottomBtnServiceVisibility(View.GONE);
            setBottomBtnDemandVisibility(View.VISIBLE);
        }

        if (isDemandOffShelf) {
            setOffShelfLogoVisibility(View.VISIBLE);
        } else {
            setOffShelfLogoVisibility(View.GONE);
        }
    }


    public void goBack(View v) {
        mActivity.finish();
    }

    //进行分享需求操作
    public void shareDemand(View v) {
        ToastUtils.shortToast("Share Demand");
    }

    //底部分享按钮的操作，需求者视角的时候从才会显示
    public void shareDemandBottom(View v) {
        ToastUtils.shortToast("Share Demand Bottom");
    }

    private int bottomBtnServiceVisibility;//服务者视角的底部按钮是否显示隐藏
    private int bottomBtnDemandVisibility;//需求者视角的底部按钮是否显示隐藏
    private int topShareBtnVisibility;//服务者视角的顶部分享按钮是否可见
    private int topDemandBtnVisibility;//需求者视角的顶部修改和下架按钮是否可见
    private int offShelfLogoVisibility;//已经下架的需求需要显示下架Logo

    @Bindable
    public int getBottomBtnServiceVisibility() {
        return bottomBtnServiceVisibility;
    }

    public void setBottomBtnServiceVisibility(int bottomBtnServiceVisibility) {
        this.bottomBtnServiceVisibility = bottomBtnServiceVisibility;
        notifyPropertyChanged(BR.bottomBtnServiceVisibility);
    }

    @Bindable
    public int getBottomBtnDemandVisibility() {
        return bottomBtnDemandVisibility;
    }

    public void setBottomBtnDemandVisibility(int bottomBtnDemandVisibility) {
        this.bottomBtnDemandVisibility = bottomBtnDemandVisibility;
        notifyPropertyChanged(BR.bottomBtnDemandVisibility);
    }

    @Bindable
    public int getTopShareBtnVisibility() {
        return topShareBtnVisibility;
    }

    public void setTopShareBtnVisibility(int topShareBtnVisibility) {
        this.topShareBtnVisibility = topShareBtnVisibility;
        notifyPropertyChanged(BR.topShareBtnVisibility);
    }

    @Bindable
    public int getTopDemandBtnVisibility() {
        return topDemandBtnVisibility;
    }

    public void setTopDemandBtnVisibility(int topDemandBtnVisibility) {
        this.topDemandBtnVisibility = topDemandBtnVisibility;
        notifyPropertyChanged(BR.topDemandBtnVisibility);
    }

    @Bindable
    public int getOffShelfLogoVisibility() {
        return offShelfLogoVisibility;
    }

    public void setOffShelfLogoVisibility(int offShelfLogoVisibility) {
        this.offShelfLogoVisibility = offShelfLogoVisibility;
        notifyPropertyChanged(BR.offShelfLogoVisibility);
    }
}
