package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.amap.api.maps2d.model.LatLng;
import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityDemandDetailBinding;
import com.slash.youth.ui.activity.DemandDetailLocationActivity;
import com.slash.youth.ui.activity.PublishDemandBaseInfoActivity;
import com.slash.youth.ui.activity.PublishDemandSuccessActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;

import org.xutils.x;

import java.util.ArrayList;

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
    private LatLng demandLatLng;//发布需求时获取的经纬度，由服务端返回

    public DemandDetailModel(ActivityDemandDetailBinding activityDemandDetailBinding, Activity activity) {
        this.mActivityDemandDetailBinding = activityDemandDetailBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {
        current_visual_angle = VISUAL_ANGLE_DAMAND;//获取当前者视角，应该根据服务端数据来判断，如果是自己发的需求，就是需求者视角，如果不是自己发的，就是服务者视角
        isDemandOffShelf = false;//由服务端数据获取当前浏览的需求是否已经下架
        demandLatLng = new LatLng(31.317866, 120.71596);//模拟经纬度，实际由服务端返回
    }


    private void initView() {
        mActivityDemandDetailBinding.svDemandDetailContent.setVerticalScrollBarEnabled(false);
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

        initDemandPics();
    }

    private void initDemandPics() {
        //模拟的图片URL数据
        ArrayList<String> listPicUrl = new ArrayList<String>();
        listPicUrl.add("http://www.pptjia.com/pptbeijing/tupian/4000s/ppts2582.jpg");
        listPicUrl.add("http://img3.3lian.com/2013/c2/43/93.jpg");
        listPicUrl.add("http://img3.3lian.com/2013/v11/95/87.jpg");
        listPicUrl.add("http://img3.3lian.com/2013/v9/87/104.jpg");
//        listPicUrl.add("http://img.sc115.com/uploads1/sc/vector/c160126/1601261115324.jpg");

        if (listPicUrl.size() >= 1) {
            mActivityDemandDetailBinding.flDemandDetailPicbox1.setVisibility(View.VISIBLE);
            x.image().bind(mActivityDemandDetailBinding.ivDemandDetailPic1, listPicUrl.get(0));
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox1.setVisibility(View.INVISIBLE);
        }

        if (listPicUrl.size() >= 2) {
            mActivityDemandDetailBinding.flDemandDetailPicbox2.setVisibility(View.VISIBLE);
            x.image().bind(mActivityDemandDetailBinding.ivDemandDetailPic2, listPicUrl.get(1));
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox2.setVisibility(View.INVISIBLE);
        }

        if (listPicUrl.size() >= 3) {
            mActivityDemandDetailBinding.flDemandDetailPicbox3.setVisibility(View.VISIBLE);
            x.image().bind(mActivityDemandDetailBinding.ivDemandDetailPic3, listPicUrl.get(2));
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox3.setVisibility(View.INVISIBLE);
        }

        if (listPicUrl.size() >= 4) {
            mActivityDemandDetailBinding.flDemandDetailPicbox4.setVisibility(View.VISIBLE);
            x.image().bind(mActivityDemandDetailBinding.ivDemandDetailPic4, listPicUrl.get(3));
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox4.setVisibility(View.INVISIBLE);
        }

        if (listPicUrl.size() >= 5) {
            mActivityDemandDetailBinding.flDemandDetailPicbox5.setVisibility(View.VISIBLE);
            x.image().bind(mActivityDemandDetailBinding.ivDemandDetailPic5, listPicUrl.get(4));
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox5.setVisibility(View.INVISIBLE);
        }

        if (listPicUrl.size() >= 6) {
            mActivityDemandDetailBinding.flDemandDetailPicbox6.setVisibility(View.VISIBLE);
            x.image().bind(mActivityDemandDetailBinding.ivDemandDetailPic6, listPicUrl.get(4));
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox6.setVisibility(View.INVISIBLE);
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

    //修改需求内容，会跳转到发布需求的页面，并在发布需求页面自动填充已有的内容
    public void updateDemand(View v) {
        if (PublishDemandSuccessActivity.mActivity != null) {
            PublishDemandSuccessActivity.mActivity.finish();
            PublishDemandSuccessActivity.mActivity = null;
        }
        Intent intentPublishDemandBaseInfo = new Intent(CommonUtils.getContext(), PublishDemandBaseInfoActivity.class);
        intentPublishDemandBaseInfo.putExtra("update", "update");
        mActivity.startActivity(intentPublishDemandBaseInfo);
        mActivity.finish();
    }

    //下架需求操作
    public void offShelfDemand(View v) {
        //调用服务端下架接口，下架成功后显示下架logo
        setOffShelfLogoVisibility(View.VISIBLE);
    }

    //跳转到个人信息界面
    public void gotoUserInfo(View v) {
        ToastUtils.shortToast("跳转至个人信息界面");
    }

    //打开聊天功能
    public void haveAChat(View v) {
        ToastUtils.shortToast("聊一聊");
    }

    //收藏需求
    public void collectDemand(View v) {
        //需要调用服务端收藏接口
        ToastUtils.shortToast("收藏需求");
    }

    //立即抢单
    public void grabDemand(View v) {
        //调用服务端接口进行抢单操作
        ToastUtils.shortToast("立即抢单");
    }

    //定位需求详情中的地址
    public void openDemandDetailLocation(View v) {
        Intent intentDemandDetailLocationActivity = new Intent(CommonUtils.getContext(), DemandDetailLocationActivity.class);
        intentDemandDetailLocationActivity.putExtra("demandLatLng", demandLatLng);
        mActivity.startActivity(intentDemandDetailLocationActivity);
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
