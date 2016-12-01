package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityServiceDetailBinding;
import com.slash.youth.databinding.ItemServiceDetailRecommendServiceBinding;
import com.slash.youth.domain.ServiceDetailBean;
import com.slash.youth.domain.SimilarServiceRecommendBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.ServiceEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.PublishServiceSucceddActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/11/9.
 */
public class ServiceDetailModel extends BaseObservable {

    ActivityServiceDetailBinding mActivityServiceDetailBinding;
    Activity mActivity;
    private LinearLayout mLlServiceRecommend;
    long serviceId;

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
        serviceId = mActivity.getIntent().getLongExtra("serviceId", -1);
        LogKit.v("serviceId:" + serviceId);
        getServiceDetailData();
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

        mActivity.finish();
        if (PublishServiceSucceddActivity.activity != null) {
            PublishServiceSucceddActivity.activity.finish();
            PublishServiceSucceddActivity.activity = null;
        }
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

    private void displayTags(String tag1, String tag2, String tag3) {
        //加载第一个tag
        if (TextUtils.isEmpty(tag1)) {
            mActivityServiceDetailBinding.tvServiceDetailTag1.setVisibility(View.INVISIBLE);
        } else {
            mActivityServiceDetailBinding.tvServiceDetailTag1.setVisibility(View.VISIBLE);
            String[] tagInfo = tag1.split("-");
            String tagName;
            if (tagInfo.length == 3) {
                tagName = tagInfo[2];
            } else {
                tagName = tag1;
            }
            mActivityServiceDetailBinding.tvServiceDetailTag1.setText(tagName);
        }
        //加载第二个tag
        if (TextUtils.isEmpty(tag2)) {
            mActivityServiceDetailBinding.tvServiceDetailTag2.setVisibility(View.INVISIBLE);
        } else {
            mActivityServiceDetailBinding.tvServiceDetailTag2.setVisibility(View.VISIBLE);
            String[] tagInfo = tag2.split("-");
            String tagName;
            if (tagInfo.length == 3) {
                tagName = tagInfo[2];
            } else {
                tagName = tag2;
            }
            mActivityServiceDetailBinding.tvServiceDetailTag2.setText(tagName);
        }//加载第三个tag
        if (TextUtils.isEmpty(tag3)) {
            mActivityServiceDetailBinding.tvServiceDetailTag3.setVisibility(View.INVISIBLE);
        } else {
            mActivityServiceDetailBinding.tvServiceDetailTag3.setVisibility(View.VISIBLE);
            String[] tagInfo = tag3.split("-");
            String tagName;
            if (tagInfo.length == 3) {
                tagName = tagInfo[2];
            } else {
                tagName = tag3;
            }
            mActivityServiceDetailBinding.tvServiceDetailTag3.setText(tagName);
        }
    }

    public void displayServicePic(String pic1FileId, String pic2FileId, String pic3FileId, String pic4FileId, String pic5FileId, String pic6FileId) {
        if (!TextUtils.isEmpty(pic1FileId)) {
            //加载第1张图片
            mActivityServiceDetailBinding.flServiceDetailPicbox1.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityServiceDetailBinding.ivServiceDetailPic1, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic1FileId);
        } else {
            mActivityServiceDetailBinding.flServiceDetailPicbox1.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(pic2FileId)) {
            //加载第2张图片
            mActivityServiceDetailBinding.flServiceDetailPicbox2.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityServiceDetailBinding.ivServiceDetailPic2, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic2FileId);
        } else {
            mActivityServiceDetailBinding.flServiceDetailPicbox2.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(pic3FileId)) {
            //加载第3张图片
            mActivityServiceDetailBinding.flServiceDetailPicbox3.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityServiceDetailBinding.ivServiceDetailPic3, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic3FileId);
        } else {
            mActivityServiceDetailBinding.flServiceDetailPicbox3.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(pic4FileId)) {
            //加载第4张图片
            mActivityServiceDetailBinding.flServiceDetailPicbox4.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityServiceDetailBinding.ivServiceDetailPic4, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic4FileId);
        } else {
            mActivityServiceDetailBinding.flServiceDetailPicbox4.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(pic5FileId)) {
            //加载第5张图片
            mActivityServiceDetailBinding.flServiceDetailPicbox5.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityServiceDetailBinding.ivServiceDetailPic5, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic5FileId);
        } else {
            mActivityServiceDetailBinding.flServiceDetailPicbox5.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(pic6FileId)) {//这种情况应该不存在，因为最多只能上传5张
            //加载第6张图片
            mActivityServiceDetailBinding.flServiceDetailPicbox6.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityServiceDetailBinding.ivServiceDetailPic6, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic6FileId);
        } else {
            mActivityServiceDetailBinding.flServiceDetailPicbox6.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 获取服务详情信息
     */
    private void getServiceDetailData() {
        if (serviceId != -1) {
            ServiceEngine.getServiceDetail(new BaseProtocol.IResultExecutor<ServiceDetailBean>() {
                @Override
                public void execute(ServiceDetailBean dataBean) {
                    LogKit.v("service data:" + dataBean.data.service.title);

                    ServiceDetailBean.Service service = dataBean.data.service;
                    if (service.uid == LoginManager.currentLoginUserId) {
                        //服务者视角
                        setTopServiceBtnVisibility(View.VISIBLE);
                        setTopShareBtnVisibility(View.GONE);

                        setBottomBtnServiceVisibility(View.VISIBLE);
                        setBottomBtnDemandVisibility(View.INVISIBLE);
                    } else {
                        //需求者视角
                        setTopServiceBtnVisibility(View.GONE);
                        setTopShareBtnVisibility(View.VISIBLE);

                        setBottomBtnServiceVisibility(View.INVISIBLE);
                        setBottomBtnDemandVisibility(View.VISIBLE);
                    }
                    setTitle(service.title);
                    setQuote("¥" + service.quote + "元");
                    if (service.timetype == ServiceEngine.SERVICE_TIMETYPE_USER_DEFINED) {//自定义时间
                        //时间:9月18日 8:30-9月19日 8:30
                        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 hh:mm");
                        String starttimeStr = sdf.format(service.starttime);
                        String endtimeStr = sdf.format(service.endtime);
                        setServiceTime("时间:" + starttimeStr + "-" + endtimeStr);
                    } else if (service.timetype == ServiceEngine.SERVICE_TIMETYPE_AFTER_WORK) {//下班后
                        setServiceTime("时间:下班后");
                    } else if (service.timetype == ServiceEngine.SERVICE_TIMETYPE_WEEKEND) {//周末
                        setServiceTime("时间:周末");
                    } else if (service.timetype == ServiceEngine.SERVICE_TIMETYPE_AFTER_WORK_AND_WEEKEND) {//下班后及周末
                        setServiceTime("时间:下班后及周末");
                    } else if (service.timetype == ServiceEngine.SERVICE_TIMETYPE_ANYTIME) {//随时
                        setServiceTime("时间:随时");
                    }
                    //接口中好像没有浏览量字段
                    String[] tags = service.tag.split(",");
                    if (tags.length == 0) {//这种情况应该不存在
                        displayTags("", "", "");
                    } else if (tags.length == 1) {
                        displayTags(tags[0], "", "");
                    } else if (tags.length == 2) {
                        displayTags(tags[0], tags[1], "");
                    } else if (tags.length == 3) {
                        displayTags(tags[0], tags[1], tags[2]);
                    } else {//这种情况应该不存在
                        displayTags(tags[0], tags[1], tags[2]);
                    }
                    if (service.pattern == 0) {//线上
                        setOfflineItemVisibility(View.GONE);
                    } else if (service.pattern == 1) {//线下
                        setOffShelfLogoVisibility(View.VISIBLE);
                        setOfflinePlace("约定地点:" + service.place != null ? service.place : "");//约定地点:星湖街328号星湖广场
                    }
                    if (service.instalment == 0) {//关闭分期
                        setInstalmentItemVisibility(View.GONE);
                    } else if (service.instalment == 1) {//开启分期
                        setInstalmentItemVisibility(View.VISIBLE);
                    }
                    //发布时间:9月18日 8:30
                    SimpleDateFormat publsihDatetimeSdf = new SimpleDateFormat("发布时间:MM月dd日 hh:mm");
                    String publicDatetimeStr = publsihDatetimeSdf.format(service.cts);
                    setPublishDatetime(publicDatetimeStr);
                    //服务描述
                    setServiceDesc(service.desc);
                    //服务相关图片
                    String[] picFileIds = service.pic.split(",");
                    if (picFileIds.length <= 0) {//这种情况应该不存在，因为至少传一张图片
                        mActivityServiceDetailBinding.llServiceDetailPicline1.setVisibility(View.GONE);
                        mActivityServiceDetailBinding.llServiceDetailPicline2.setVisibility(View.GONE);
                    } else if (picFileIds.length > 0 && picFileIds.length <= 3) {
                        mActivityServiceDetailBinding.llServiceDetailPicline1.setVisibility(View.VISIBLE);
                        mActivityServiceDetailBinding.llServiceDetailPicline2.setVisibility(View.GONE);
                        if (picFileIds.length == 1) {
                            displayServicePic(picFileIds[0], null, null, null, null, null);
                        } else if (picFileIds.length == 2) {
                            displayServicePic(picFileIds[0], picFileIds[1], null, null, null, null);
                        } else if (picFileIds.length == 3) {
                            displayServicePic(picFileIds[0], picFileIds[1], picFileIds[2], null, null, null);
                        }
                    } else {
                        mActivityServiceDetailBinding.llServiceDetailPicline1.setVisibility(View.VISIBLE);
                        mActivityServiceDetailBinding.llServiceDetailPicline2.setVisibility(View.VISIBLE);
                        if (picFileIds.length == 4) {
                            displayServicePic(picFileIds[0], picFileIds[1], picFileIds[2], picFileIds[3], null, null);
                        } else if (picFileIds.length == 5) {
                            displayServicePic(picFileIds[0], picFileIds[1], picFileIds[2], picFileIds[3], picFileIds[4], null);
                        } else if (picFileIds.length == 6) {//这种情况应该不存在，因为最多就只能传5张图片
                            displayServicePic(picFileIds[0], picFileIds[1], picFileIds[2], picFileIds[3], picFileIds[4], picFileIds[5]);
                        }
                    }
                    //纠纷处理方式
                    if (service.bp == 1) {
                        setDisputeHandingType("平台方式");
                    } else if (service.bp == 2) {
                        setDisputeHandingType("协商方式");
                    }
                    //上架、下架显示 用isonline字段判断
//                    if(service.isonline)
                }

                @Override
                public void executeResultError(String result) {

                }
            }, serviceId + "");
        }
    }

    /**
     * 从接口获取相似服务推荐的数据，当需求者视角看服务的时候，需要显示推荐服务
     */
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

    /**
     * 显示相似服务推荐的数据，只有需求者视角才需要
     */
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

    private String title;
    private String serviceTime;//时间:9月18日 8:30-9月19日 8:30
    private String quote;//¥300元
    private String viewCount;//300人浏览
    //技能标签最多三个，暂时写死，控制显示和隐藏
    private int offlineItemVisibility = View.INVISIBLE;
    private String offlinePlace;//约定地点:星湖街328号星湖广场
    private int instalmentItemVisibility = View.INVISIBLE;
    private String publishDatetime;//发布时间:9月18日 8:30
    private String serviceDesc;
    private String disputeHandingType;//纠纷处理方式，平台、协商

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

    @Bindable
    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
        notifyPropertyChanged(BR.viewCount);
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
    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
        notifyPropertyChanged(BR.serviceTime);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public int getOfflineItemVisibility() {
        return offlineItemVisibility;
    }

    public void setOfflineItemVisibility(int offlineItemVisibility) {
        this.offlineItemVisibility = offlineItemVisibility;
        notifyPropertyChanged(BR.offlineItemVisibility);
    }

    @Bindable
    public String getOfflinePlace() {
        return offlinePlace;
    }

    public void setOfflinePlace(String offlinePlace) {
        this.offlinePlace = offlinePlace;
        notifyPropertyChanged(BR.offlinePlace);
    }

    @Bindable
    public int getInstalmentItemVisibility() {
        return instalmentItemVisibility;
    }

    public void setInstalmentItemVisibility(int instalmentItemVisibility) {
        this.instalmentItemVisibility = instalmentItemVisibility;
        notifyPropertyChanged(BR.instalmentItemVisibility);
    }

    @Bindable
    public String getPublishDatetime() {
        return publishDatetime;
    }


    public void setPublishDatetime(String publishDatetime) {
        this.publishDatetime = publishDatetime;
        notifyPropertyChanged(BR.publishDatetime);
    }

    @Bindable
    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
        notifyPropertyChanged(BR.serviceDesc);
    }

    @Bindable
    public String getDisputeHandingType() {
        return disputeHandingType;
    }

    public void setDisputeHandingType(String disputeHandingType) {
        this.disputeHandingType = disputeHandingType;
        notifyPropertyChanged(BR.disputeHandingType);
    }
}
