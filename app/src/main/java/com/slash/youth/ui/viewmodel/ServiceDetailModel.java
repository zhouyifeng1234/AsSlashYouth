package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amap.api.maps2d.model.LatLng;
import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityServiceDetailBinding;
import com.slash.youth.databinding.ItemServiceDetailRecommendServiceBinding;
import com.slash.youth.domain.AppointmentServiceResultBean;
import com.slash.youth.domain.ChatCmdShareTaskBean;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.DetailRecommendServiceList;
import com.slash.youth.domain.ServiceDetailBean;
import com.slash.youth.domain.UserInfoBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.engine.ServiceEngine;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.DemandDetailLocationActivity;
import com.slash.youth.ui.activity.MyFriendActivtiy;
import com.slash.youth.ui.activity.PublishServiceBaseInfoActivity;
import com.slash.youth.ui.activity.PublishServiceSucceddActivity;
import com.slash.youth.ui.activity.ServiceDetailActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ShareUtils;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

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
    boolean isFromDetail;
    String[] optionalPriceUnit;
    long serviceUserId;
    LatLng serviceLatLng;

    public ServiceDetailModel(ActivityServiceDetailBinding activityServiceDetailBinding, Activity activity) {
        this.mActivityServiceDetailBinding = activityServiceDetailBinding;
        this.mActivity = activity;
        mLlServiceRecommend =
                mActivityServiceDetailBinding.llSimilarServiceRecommend;
        initData();
        initView();
    }

    private void initData() {
        serviceId = mActivity.getIntent().getLongExtra("serviceId", -1);
        isFromDetail = mActivity.getIntent().getBooleanExtra("isFromDetail", false);//用来判断是否是从详情页中的推荐跳转过来的
        LogKit.v("serviceId:" + serviceId);
        optionalPriceUnit = new String[]{"次", "个", "幅", "份", "单", "小时", "分钟", "天", "其他"};
        getServiceDetailData();
    }

    private void initView() {

    }

    public void goBack(View v) {
        mActivity.finish();
    }


    //修改服务
    public void updateService(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SERVICE_DETAIL_REVISE);

        mActivity.finish();
        if (PublishServiceSucceddActivity.activity != null) {
            PublishServiceSucceddActivity.activity.finish();
            PublishServiceSucceddActivity.activity = null;
        }
        Intent intentPublishServiceBaseInfoActivity = new Intent(CommonUtils.getContext(), PublishServiceBaseInfoActivity.class);
        intentPublishServiceBaseInfoActivity.putExtra("serviceDetailBean", serviceDetailBean);
        mActivity.startActivity(intentPublishServiceBaseInfoActivity);
    }

    //下架服务
    public void offShelfService(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SERVICE_DETAIL_UNSHELVE);

        MyTaskEngine.upAndDownTask(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                setOffShelfLogoVisibility(View.VISIBLE);
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("下架服务失败:" + result);
            }
        }, serviceId + "", "2", "0");
    }

    //聊一聊
    public void haveAChat(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SERVICE_DETAIL_CHAT);
        Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
        intentChatActivity.putExtra("targetId", serviceId + "");
        mActivity.startActivity(intentChatActivity);
    }

    //收藏服务
    public void collectService(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SERVICE_DETAIL_COLLECT);
        if (isCollectionService) {//已经收藏过了，点击取消收藏
            MyTaskEngine.cancelCollection(new BaseProtocol.IResultExecutor<CommonResultBean>() {
                @Override
                public void execute(CommonResultBean dataBean) {
                    ToastUtils.shortToast("取消收藏成功");
                    isCollectionService = false;
                    setNoCollectionState();
                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("取消收藏失败:" + result);
                }
            }, "2", serviceId + "");
        } else {//还未收藏，点击收藏
            ServiceEngine.collectService(new BaseProtocol.IResultExecutor<CommonResultBean>() {
                @Override
                public void execute(CommonResultBean dataBean) {
                    ToastUtils.shortToast("收藏成功");
                    isCollectionService = true;
                    setCollectionState();
                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("收藏失败:" + result);
                }
            }, serviceId + "");
        }
    }

    //立即抢单（抢服务）
    public void grabService(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SERVICE_DETAIL_IMMEDIATELY_ORDER);
        ServiceEngine.appointmentService(new BaseProtocol.IResultExecutor<AppointmentServiceResultBean>() {
            @Override
            public void execute(AppointmentServiceResultBean dataBean) {
                ToastUtils.shortToast("预约成功");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("预约服务失败:" + result);
            }
        }, serviceId + "");


    }

    //分享服务（顶部需求者视角看到的分享按钮）
    public void shareService(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SERVICE_DETAIL_IMMEDIATELY_SHARE);
        openShareLayer();
    }

    //底部服务者视角看到的分享按钮
    public void shareServiceBottom(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SERVICE_DETAIL_IMMEDIATELY_SHARE);
        openShareLayer();
    }

    private void openShareLayer() {
        setShareLayerVisibility(View.VISIBLE);
    }

    /**
     * 取消分销按钮
     *
     * @param v
     */
    public void cancelShare(View v) {
        setShareLayerVisibility(View.GONE);
    }

    /**
     * 斜杠好友分享，分享给我们APP中的好友
     */
    public void shareToSlashFriend(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SERVICE_DETAIL_IMMEDIATELY_SHARE_FRIEND);

        Intent intentMyFriendActivity = new Intent(CommonUtils.getContext(), MyFriendActivtiy.class);
        ChatCmdShareTaskBean chatCmdShareTaskBean = new ChatCmdShareTaskBean();
        chatCmdShareTaskBean.uid = serviceUserId;
        chatCmdShareTaskBean.avatar = serviceUserAvatar;
        chatCmdShareTaskBean.title = getTitle();
        chatCmdShareTaskBean.quote = getQuote();
        chatCmdShareTaskBean.type = 2;
        chatCmdShareTaskBean.tid = serviceId;
        intentMyFriendActivity.putExtra("chatCmdShareTaskBean", chatCmdShareTaskBean);
        mActivity.startActivity(intentMyFriendActivity);
    }

    /**
     * 分享给微信好友
     *
     * @param v
     */
    public void shareToWeChat(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SERVICE_DETAIL_RECOMMEND_FRIEND);

        UMShareAPI mShareAPI = UMShareAPI.get(mActivity);
        if (mShareAPI.isInstall(mActivity, SHARE_MEDIA.WEIXIN)) {
            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN).withMedia(shareAvatar).withTitle(shareTitle).withText(shareContent).withTargetUrl(shareUrl).setCallback(umShareListener).share();
        }
    }

    /**
     * 分享到微信朋友圈
     *
     * @param v
     */
    public void shareToWeChatCircle(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SERVICE_DETAIL_IMMEDIATELY_SHARE_WECHAT);

        UMShareAPI mShareAPI = UMShareAPI.get(mActivity);
        if (mShareAPI.isInstall(mActivity, SHARE_MEDIA.WEIXIN_CIRCLE)) {
            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).withMedia(shareAvatar).withTitle(shareTitle).withText(shareContent).withTargetUrl(shareUrl).setCallback(umShareListener).share();
        }
    }

    /**
     * 分享到QQ
     *
     * @param v
     */
    public void shareToQQ(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SERVICE_DETAIL_IMMEDIATELY_SHARE_QQ);

        UMShareAPI mShareAPI = UMShareAPI.get(mActivity);
        if (mShareAPI.isInstall(mActivity, SHARE_MEDIA.QQ)) {
            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QQ).withMedia(shareAvatar).withTitle(shareTitle).withText(shareContent).withTargetUrl(shareUrl).setCallback(umShareListener).share();
        } else {
            ToastUtils.shortToast("请先安装qq客户端");
        }
    }

    /**
     * 分享到QQ空间
     *
     * @param v
     */
    public void shareToQZone(View v) {
        new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QZONE).withMedia(shareAvatar).withTitle(shareTitle).withText(shareContent).withTargetUrl(shareUrl).setCallback(umShareListener).share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogKit.v("platform" + platform);
            ToastUtils.shortToast(platform + " 分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.shortToast(platform + " 分享失败");
            if (t != null) {
                LogKit.v("throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.shortToast(platform + " 分享取消了");
        }
    };

    public void openServiceDetailLocation(View v) {
        Intent intentDemandDetailLocationActivity = new Intent(CommonUtils.getContext(), DemandDetailLocationActivity.class);
        intentDemandDetailLocationActivity.putExtra("demandLatLng", serviceLatLng);
        mActivity.startActivity(intentDemandDetailLocationActivity);
    }

    public void gotoUserInfo(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SERVICE_DETAIL_ENTER_PERSON_DETAIL);
        Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
        intentUserInfoActivity.putExtra("Uid", serviceUserId);
        mActivity.startActivity(intentUserInfoActivity);
    }

    private void displayTags(String tag1, String tag2, String tag3) {
        //加载第一个tag
        if (TextUtils.isEmpty(tag1)) {
            mActivityServiceDetailBinding.tvServiceDetailTag1.setVisibility(View.GONE);
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
            mActivityServiceDetailBinding.tvServiceDetailTag2.setVisibility(View.GONE);
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
            mActivityServiceDetailBinding.tvServiceDetailTag3.setVisibility(View.GONE);
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
            LogKit.v("pic1FileId:" + pic1FileId);
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

    ServiceDetailBean serviceDetailBean;

    /**
     * 获取服务详情信息
     */
    private void getServiceDetailData() {
        if (serviceId != -1) {
            ServiceEngine.getServiceDetail(new BaseProtocol.IResultExecutor<ServiceDetailBean>() {
                @Override
                public void execute(ServiceDetailBean dataBean) {
                    LogKit.v("service data:" + dataBean.data.service.title);
                    serviceDetailBean = dataBean;
                    ServiceDetailBean.Service service = dataBean.data.service;
                    serviceLatLng = new LatLng(service.lat, service.lng);
                    serviceUserId = service.uid;
                    if (service.uid == LoginManager.currentLoginUserId) {
                        //服务者视角
                        setServiceRecommendLabelVisibililty(View.GONE);

                        setTopServiceBtnVisibility(View.VISIBLE);
                        setTopShareBtnVisibility(View.GONE);

                        setBottomBtnServiceVisibility(View.VISIBLE);
                        setBottomBtnDemandVisibility(View.INVISIBLE);
                    } else {
                        //需求者视角
                        setServiceRecommendLabelVisibililty(View.VISIBLE);

                        setTopServiceBtnVisibility(View.GONE);
                        setTopShareBtnVisibility(View.VISIBLE);

                        setBottomBtnServiceVisibility(View.INVISIBLE);
                        setBottomBtnDemandVisibility(View.VISIBLE);
                    }
                    setTitle(service.title);
                    if (service.uid == LoginManager.currentLoginUserId) {
                        setTopTitle("服务详情");
                    } else {
                        setTopTitle(service.title);
                    }
                    if (service.quoteunit == 9) {
                        setQuote("¥" + (int) service.quote + "元");
                    } else if (service.quoteunit > 0 && service.quoteunit < 9) {
                        String quoteunitStr = optionalPriceUnit[service.quoteunit - 1];
                        setQuote("¥" + (int) service.quote + "元/" + quoteunitStr);
                    } else {//这种情况应该不存在
                        setQuote("¥" + (int) service.quote + "元");
                    }
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
                    //相关技能标签
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
//                        setOfflineItemVisibility(View.GONE);
                        setOfflineItemVisibility(View.VISIBLE);
                        mActivityServiceDetailBinding.tvOnlineOfflineLabel.setText("线上");
                        setServiceDetailLocationVisibility(View.GONE);
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
                    SimpleDateFormat publsihDatetimeSdf = new SimpleDateFormat("发布时间:yyyy年MM月dd日 HH:mm发布");
                    String publicDatetimeStr = publsihDatetimeSdf.format(service.cts);
                    setPublishDatetime(publicDatetimeStr);
                    //服务描述
                    setServiceDesc(service.desc);
                    //服务相关图片
                    String[] picFileIds = service.pic.split(",");
                    for (String fileId : picFileIds) {
                        listViewPicFileIds.add(fileId);
                    }
                    mActivityServiceDetailBinding.vpViewPic.setAdapter(new ViewPicPagerAdapter());
                    //如果service.pic为""空字符喘，picFileIds的length也是1
                    if (picFileIds.length <= 0 || TextUtils.isEmpty(service.pic)) {//这种情况应该不存在，因为至少传一张图片
                        mActivityServiceDetailBinding.llServiceDetailPicline1.setVisibility(View.GONE);
                        mActivityServiceDetailBinding.llServiceDetailPicline2.setVisibility(View.GONE);
                        mActivityServiceDetailBinding.vPicUnderline.setVisibility(View.GONE);
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

                    getServiceUserInfo(service.uid);//获取服务发布者的个人信息
                }

                @Override
                public void executeResultError(String result) {

                }
            }, serviceId + "");
        }
    }

    String avatarUrl;//三方分享中用到的头像地址，全路径
    String serviceUserAvatar;//服务者头像地址fileId，非全路径，只是头像地址的fileId

    /**
     * 获取服务发布者的个人信息
     *
     * @param uid
     */
    private void getServiceUserInfo(final long uid) {
        UserInfoEngine.getOtherUserInfo(new BaseProtocol.IResultExecutor<UserInfoBean>() {
            @Override
            public void execute(UserInfoBean dataBean) {
                UserInfoBean.UInfo uinfo = dataBean.data.uinfo;
                serviceUserAvatar = uinfo.avatar;
                avatarUrl = GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + uinfo.avatar;
                BitmapKit.bindImage(mActivityServiceDetailBinding.ivServiceUserAvatar, avatarUrl);
                if (uinfo.isauth == 0) {//未认证
                    setIsAuthVisibility(View.INVISIBLE);
                } else if (uinfo.isauth == 1) {//已认证
                    setIsAuthVisibility(View.VISIBLE);
                }
                setUsername(uinfo.name);
                setFanscount("粉丝数" + uinfo.fanscount);
                setTaskcount("顺利成交单数" + uinfo.achievetaskcount + "/" + uinfo.totoltaskcount);//顺利成交单数9/12
                String userPlace = "";
                if (uinfo.province.equals(uinfo.city)) {
                    userPlace = uinfo.province;
                } else {
                    userPlace = uinfo.province + uinfo.city;
                }
                setServiceUserPlace(userPlace);

                if (uid != LoginManager.currentLoginUserId) {
                    getRecommendServiceData(uid);//获取相似服务推荐
                }

                initShareInfo();
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取服务发布者信息失败:" + result);
            }
        }, uid + "", "0");
    }

    ArrayList<DetailRecommendServiceList.RecommendServiceInfo> listRecommendService;

    /**
     * 从接口获取相似服务推荐的数据，当需求者视角看服务的时候，需要显示推荐服务
     */
    public void getRecommendServiceData(final long uid) {
        ServiceEngine.getDetailRecommendService(new BaseProtocol.IResultExecutor<DetailRecommendServiceList>() {
            @Override
            public void execute(DetailRecommendServiceList dataBean) {
                listRecommendService = dataBean.data.list;
                setRecommendServiceItemData();
                if (uid != LoginManager.currentLoginUserId) {
                    getCollectionStatus();
                }
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取推荐服务列表失败");
            }
        }, serviceId + "", "5");
    }

    boolean isCollectionService;

    /**
     * 获取当前登录者收藏服务的状态
     */
    private void getCollectionStatus() {
        MyTaskEngine.getCollectionStatus(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                if (dataBean.data.status == 1) {//1表示收藏过
                    setCollectionState();
                    isCollectionService = true;
                } else {// 0表示未收藏过
                    setNoCollectionState();
                    isCollectionService = false;
                }
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取收藏服务状态失败:" + result);
            }
        }, "2", serviceId + "");
    }

    /**
     * 设置收藏状态
     */
    private void setCollectionState() {
        Drawable topDrawable = CommonUtils.getContext().getResources().getDrawable(R.mipmap.yi_heart);
        int intrinsicWidth = topDrawable.getIntrinsicWidth();
        int intrinsicHeight = topDrawable.getIntrinsicHeight();
        topDrawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        mActivityServiceDetailBinding.tvCollection.setCompoundDrawables(null, topDrawable, null, null);
//        mActivityServiceDetailBinding.tvCollection.setText("取消收藏");
        mActivityServiceDetailBinding.tvCollection.setText("收藏");
    }

    /**
     * 设置未收藏状态
     */
    private void setNoCollectionState() {
        Drawable topDrawable = CommonUtils.getContext().getResources().getDrawable(R.mipmap.collection_icon);
        int intrinsicWidth = topDrawable.getIntrinsicWidth();
        int intrinsicHeight = topDrawable.getIntrinsicHeight();
        topDrawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        mActivityServiceDetailBinding.tvCollection.setCompoundDrawables(null, topDrawable, null, null);
        mActivityServiceDetailBinding.tvCollection.setText("收藏");
    }

    String shareTitle;
    String shareContent;
    UMImage shareAvatar;
    String shareUrl;

    private void initShareInfo() {
        shareTitle = getUsername() + "发布了服务《" + getTitle() + "》";
        shareContent = "赶紧来预约吧";
        shareAvatar = new UMImage(CommonUtils.getContext(), avatarUrl);
        shareUrl = ShareUtils.DETAIL_SHARE + "?nav=1&param=2&oid=" + serviceId + "&favei=1&cid=" + LoginManager.currentLoginUserId + "&share=visit";
    }

    /**
     * 显示相似服务推荐的数据，只有需求者视角才需要
     */
    public void setRecommendServiceItemData() {
        for (int i = 0; i < listRecommendService.size(); i++) {
            final DetailRecommendServiceList.RecommendServiceInfo recommendServiceInfo = listRecommendService.get(i);
            ItemServiceDetailRecommendServiceBinding itemServiceDetailRecommendServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_service_detail_recommend_service, null, false);
            ItemServiceDetailRecommendServiceModel itemServiceDetailRecommendServiceModel = new ItemServiceDetailRecommendServiceModel(itemServiceDetailRecommendServiceBinding, mActivity, recommendServiceInfo);
            itemServiceDetailRecommendServiceBinding.setItemServiceDetailRecommendServiceModel(itemServiceDetailRecommendServiceModel);
            View itemView = itemServiceDetailRecommendServiceBinding.getRoot();
            if (!isFromDetail) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
                        intentServiceDetailActivity.putExtra("serviceId", recommendServiceInfo.id);
                        intentServiceDetailActivity.putExtra("isFromDetail", true);
                        mActivity.startActivity(intentServiceDetailActivity);
                    }
                });
            }
            mLlServiceRecommend.addView(itemView);
        }
    }

    /**
     * 点击图片查看大图
     *
     * @param v
     */
    public void openViewPic(View v) {
        int currentViewIndex;
        switch (v.getId()) {
            case R.id.fl_service_detail_picbox_1:
                currentViewIndex = 0;
                break;
            case R.id.fl_service_detail_picbox_2:
                currentViewIndex = 1;
                break;
            case R.id.fl_service_detail_picbox_3:
                currentViewIndex = 2;
                break;
            case R.id.fl_service_detail_picbox_4:
                currentViewIndex = 3;
                break;
            case R.id.fl_service_detail_picbox_5:
                currentViewIndex = 4;
                break;
            default:
                currentViewIndex = 5;
                break;
        }
        mActivityServiceDetailBinding.vpViewPic.setCurrentItem(currentViewIndex);
        setViewPicVisibility(View.VISIBLE);
    }

    ArrayList<String> listViewPicFileIds = new ArrayList<String>();

    private class ViewPicPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return listViewPicFileIds.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView ivViewPic = new ImageView(CommonUtils.getContext());
            ivViewPic.setScaleType(ImageView.ScaleType.CENTER);
            String fileId = listViewPicFileIds.get(position);
            BitmapKit.bindImage(ivViewPic, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + fileId);
            ivViewPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setViewPicVisibility(View.GONE);
                }
            });
            container.addView(ivViewPic);
            return ivViewPic;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    private int topShareBtnVisibility;
    private int topServiceBtnVisibility;
    private int bottomBtnDemandVisibility;//底部需求者视角可以看到的按钮
    private int bottomBtnServiceVisibility;//底部服务者视角可以看到的按钮
    private int offShelfLogoVisibility = View.GONE;

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
    private int isAuthVisibility;
    private String username;
    private String fanscount;
    private String taskcount;
    private String serviceUserPlace;

    private int shareLayerVisibility = View.GONE;

    private String topTitle;

    private int viewPicVisibility = View.GONE;

    private int serviceDetailLocationVisibility;

    private int serviceRecommendLabelVisibililty;

    @Bindable
    public int getServiceRecommendLabelVisibililty() {
        return serviceRecommendLabelVisibililty;
    }

    public void setServiceRecommendLabelVisibililty(int serviceRecommendLabelVisibililty) {
        this.serviceRecommendLabelVisibililty = serviceRecommendLabelVisibililty;
        notifyPropertyChanged(BR.serviceRecommendLabelVisibililty);
    }

    @Bindable
    public int getServiceDetailLocationVisibility() {
        return serviceDetailLocationVisibility;
    }

    public void setServiceDetailLocationVisibility(int serviceDetailLocationVisibility) {
        this.serviceDetailLocationVisibility = serviceDetailLocationVisibility;
        notifyPropertyChanged(BR.serviceDetailLocationVisibility);
    }

    @Bindable
    public int getViewPicVisibility() {
        return viewPicVisibility;
    }

    public void setViewPicVisibility(int viewPicVisibility) {
        this.viewPicVisibility = viewPicVisibility;
        notifyPropertyChanged(BR.viewPicVisibility);
    }

    @Bindable
    public String getTopTitle() {
        return topTitle;
    }

    public void setTopTitle(String topTitle) {
        this.topTitle = topTitle;
        notifyPropertyChanged(BR.topTitle);
    }

    @Bindable
    public int getShareLayerVisibility() {
        return shareLayerVisibility;
    }

    public void setShareLayerVisibility(int shareLayerVisibility) {
        this.shareLayerVisibility = shareLayerVisibility;
        notifyPropertyChanged(BR.shareLayerVisibility);
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

    @Bindable
    public int getIsAuthVisibility() {
        return isAuthVisibility;
    }

    public void setIsAuthVisibility(int isAuthVisibility) {
        this.isAuthVisibility = isAuthVisibility;
        notifyPropertyChanged(BR.isAuthVisibility);
    }

    @Bindable
    public String getFanscount() {
        return fanscount;
    }

    public void setFanscount(String fanscount) {
        this.fanscount = fanscount;
        notifyPropertyChanged(BR.fanscount);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getTaskcount() {
        return taskcount;
    }

    public void setTaskcount(String taskcount) {
        this.taskcount = taskcount;
        notifyPropertyChanged(BR.taskcount);
    }

    @Bindable
    public String getServiceUserPlace() {
        return serviceUserPlace;
    }

    public void setServiceUserPlace(String serviceUserPlace) {
        this.serviceUserPlace = serviceUserPlace;
        notifyPropertyChanged(BR.serviceUserPlace);
    }
}
