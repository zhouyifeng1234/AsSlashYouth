package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityServiceDetailBinding;
import com.slash.youth.databinding.ItemServiceDetailRecommendServiceBinding;
import com.slash.youth.domain.SimilarServiceRecommendBean;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/11/9.
 */
public class ServiceDetailModel extends BaseObservable {

    ActivityServiceDetailBinding mActivityServiceDetailBinding;
    Activity mActivity;
    private LinearLayout mLlServiceRecommend;

    public ServiceDetailModel(ActivityServiceDetailBinding activityServiceDetailBinding, Activity activity) {
        this.mActivityServiceDetailBinding = activityServiceDetailBinding;
        this.mActivity = activity;
        mLlServiceRecommend =
                mActivityServiceDetailBinding.llSimilarServiceRecommend;
        initData();
        initView();
    }

    ArrayList<SimilarServiceRecommendBean> listRecommendService = new ArrayList<SimilarServiceRecommendBean>();

    private void initData() {
        getRecommendServiceData();
    }

    private void initView() {

    }

    public void goBack(View v) {
        mActivity.finish();
    }

    //分享服务（顶部需求者视角看到的分享按钮）
    public void shareService(View v) {

    }

    //修改服务
    public void updateService(View v) {

    }

    //下架服务
    public void offShelfService(View v) {

    }

    //聊一聊
    public void haveAChat(View v) {

    }

    //收藏服务
    public void collectService(View v) {

    }

    //立即抢单（抢服务）
    public void grabService(View v) {

    }

    //底部服务者视角看到的分享按钮
    public void shareServiceBottom(View v) {

    }

    public void openServiceDetailLocation(View v) {

    }

    public void gotoUserInfo(View v) {

    }

    public void getRecommendServiceData() {
        //模拟数据
        listRecommendService.add(new SimilarServiceRecommendBean());
        listRecommendService.add(new SimilarServiceRecommendBean());
        listRecommendService.add(new SimilarServiceRecommendBean());
        listRecommendService.add(new SimilarServiceRecommendBean());
        listRecommendService.add(new SimilarServiceRecommendBean());

        //实际应该网络加载数据完毕后的异步回调中调用
        setRecommendServiceItemData();
    }

    public void setRecommendServiceItemData() {
        for (int i = 0; i < listRecommendService.size(); i++) {
            SimilarServiceRecommendBean similarServiceRecommendBean = listRecommendService.get(i);
            ItemServiceDetailRecommendServiceBinding itemServiceDetailRecommendServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_service_detail_recommend_service, null, false);
            ItemServiceDetailRecommendServiceModel itemServiceDetailRecommendServiceModel = new ItemServiceDetailRecommendServiceModel(itemServiceDetailRecommendServiceBinding, mActivity, similarServiceRecommendBean);
            itemServiceDetailRecommendServiceBinding.setItemServiceDetailRecommendServiceModel(itemServiceDetailRecommendServiceModel);
            mLlServiceRecommend.addView(itemServiceDetailRecommendServiceBinding.getRoot());
        }
    }

    private int topShareBtnVisibility;
    private int topServiceBtnVisibility;
    private int bottomBtnDemandVisibility;//底部需求者视角可以看到的按钮
    private int bottomBtnServiceVisibility;//底部服务者视角可以看到的按钮
    private int offShelfLogoVisibility;

    @Bindable
    public int getTopShareBtnVisibility() {
        return topShareBtnVisibility;
    }

    public void setTopShareBtnVisibility(int topShareBtnVisibility) {
        this.topShareBtnVisibility = topShareBtnVisibility;
        notifyPropertyChanged(BR.topShareBtnVisibility);
    }

    @Bindable
    public int getTopServiceBtnVisibility() {
        return topServiceBtnVisibility;
    }

    public void setTopServiceBtnVisibility(int topServiceBtnVisibility) {
        this.topServiceBtnVisibility = topServiceBtnVisibility;
        notifyPropertyChanged(BR.topServiceBtnVisibility);
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
    public int getBottomBtnServiceVisibility() {
        return bottomBtnServiceVisibility;
    }

    public void setBottomBtnServiceVisibility(int bottomBtnServiceVisibility) {
        this.bottomBtnServiceVisibility = bottomBtnServiceVisibility;
        notifyPropertyChanged(BR.bottomBtnServiceVisibility);
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
