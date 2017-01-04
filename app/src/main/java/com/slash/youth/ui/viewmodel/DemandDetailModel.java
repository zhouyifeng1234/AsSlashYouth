package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityDemandDetailBinding;
import com.slash.youth.databinding.ItemDetailRecommendDemandBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.DemandDetailBean;
import com.slash.youth.domain.DetailRecommendDemandList;
import com.slash.youth.domain.UserInfoBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.DemandDetailLocationActivity;
import com.slash.youth.ui.activity.PublishDemandBaseInfoActivity;
import com.slash.youth.ui.activity.PublishDemandSuccessActivity;
import com.slash.youth.ui.view.SlashDateTimePicker;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhouyifeng on 2016/10/24.
 */
public class DemandDetailModel extends BaseObservable {

    ActivityDemandDetailBinding mActivityDemandDetailBinding;
    Activity mActivity;
    long demandId;
    SlashDateTimePicker sdtpBidDemandStarttime;
    private LinearLayout mLlDemandRecommend;

    public DemandDetailModel(ActivityDemandDetailBinding activityDemandDetailBinding, Activity activity) {
        this.mActivityDemandDetailBinding = activityDemandDetailBinding;
        this.mActivity = activity;
        initData();
        initView();
        initListener();
    }

    private void initData() {
        demandId = mActivity.getIntent().getLongExtra("demandId", -1);
        getDemandDetailDataFromServer();
    }


    private void initView() {
        mLlDemandRecommend = mActivityDemandDetailBinding.llDemandRecommend;
        sdtpBidDemandStarttime = mActivityDemandDetailBinding.sdtpBidDemandChooseDatetime;
        mActivityDemandDetailBinding.svDemandDetailContent.setVerticalScrollBarEnabled(false);
    }

    private void initListener() {
        mActivityDemandDetailBinding.etBidDemandInstalmentRatio1.addTextChangedListener(new InstalmentRatioTextChangeListener(1));
        mActivityDemandDetailBinding.etBidDemandInstalmentRatio2.addTextChangedListener(new InstalmentRatioTextChangeListener(2));
        mActivityDemandDetailBinding.etBidDemandInstalmentRatio3.addTextChangedListener(new InstalmentRatioTextChangeListener(3));
        mActivityDemandDetailBinding.etBidDemandInstalmentRatio4.addTextChangedListener(new InstalmentRatioTextChangeListener(4));
        mActivityDemandDetailBinding.etBidDemandQuote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String bidQuoteStr = s.toString();
                String ratioStr1 = mActivityDemandDetailBinding.etBidDemandInstalmentRatio1.getText().toString();
                String ratioStr2 = mActivityDemandDetailBinding.etBidDemandInstalmentRatio2.getText().toString();
                String ratioStr3 = mActivityDemandDetailBinding.etBidDemandInstalmentRatio3.getText().toString();
                String ratioStr4 = mActivityDemandDetailBinding.etBidDemandInstalmentRatio4.getText().toString();
                try {
                    double bidQuote = Double.parseDouble(bidQuoteStr);
                    double ratio1 = Double.parseDouble(ratioStr1);
//                    int ratioQuote1 = (int) (bidQuote * ratio1 / 100);
                    double ratioQuote1 = bidQuote * ratio1 / 100;
                    mActivityDemandDetailBinding.tvInstalment1Amount.setText("￥" + ratioQuote1);
                    double ratio2 = Double.parseDouble(ratioStr2);
//                    int ratioQuote2 = (int) (bidQuote * ratio2 / 100);
                    double ratioQuote2 = bidQuote * ratio2 / 100;
                    mActivityDemandDetailBinding.tvInstalment2Amount.setText("￥" + ratioQuote2);
                    double ratio3 = Double.parseDouble(ratioStr3);
//                    int ratioQuote3 = (int) (bidQuote * ratio3 / 100);
                    double ratioQuote3 = bidQuote * ratio3 / 100;
                    mActivityDemandDetailBinding.tvInstalment3Amount.setText("￥" + ratioQuote3);
                    double ratio4 = Double.parseDouble(ratioStr4);
//                    int ratioQuote4 = (int) (bidQuote * ratio4 / 100);
                    double ratioQuote4 = bidQuote * ratio4 / 100;
                    mActivityDemandDetailBinding.tvInstalment4Amount.setText("￥" + ratioQuote4);
                } catch (Exception ex) {
//                    ToastUtils.shortToast("填写数据有误");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private class InstalmentRatioTextChangeListener implements TextWatcher {

        private int instalmentNo;

        public InstalmentRatioTextChangeListener(int instalmentNo) {
            this.instalmentNo = instalmentNo;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String ratioStr = s.toString();
            String bidQuoteStr = mActivityDemandDetailBinding.etBidDemandQuote.getText().toString();
            String ratioQuoteStr = "";
            try {
                double ratio = Double.parseDouble(ratioStr);
                double bidQuote = Double.parseDouble(bidQuoteStr);
                double ratioQuote = bidQuote * ratio / 100;
                ratioQuoteStr = "￥" + ratioQuote;
            } catch (Exception ex) {

            }
            switch (instalmentNo) {
                case 1:
                    mActivityDemandDetailBinding.tvInstalment1Amount.setText(ratioQuoteStr);
                    break;
                case 2:
                    mActivityDemandDetailBinding.tvInstalment2Amount.setText(ratioQuoteStr);
                    break;
                case 3:
                    mActivityDemandDetailBinding.tvInstalment3Amount.setText(ratioQuoteStr);
                    break;
                case 4:
                    mActivityDemandDetailBinding.tvInstalment4Amount.setText(ratioQuoteStr);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void displayTags(String tag1, String tag2, String tag3) {
        //加载第一个tag
        if (TextUtils.isEmpty(tag1)) {
            mActivityDemandDetailBinding.tvDemandDetailTag1.setVisibility(View.GONE);
        } else {
            mActivityDemandDetailBinding.tvDemandDetailTag1.setVisibility(View.VISIBLE);
            String[] tagInfo = tag1.split("-");
            String tagName;
            if (tagInfo.length == 3) {
                tagName = tagInfo[2];
            } else {
                tagName = tag1;
            }
            mActivityDemandDetailBinding.tvDemandDetailTag1.setText(tagName);
        }
        //加载第二个tag
        if (TextUtils.isEmpty(tag2)) {
            mActivityDemandDetailBinding.tvDemandDetailTag2.setVisibility(View.GONE);
        } else {
            mActivityDemandDetailBinding.tvDemandDetailTag2.setVisibility(View.VISIBLE);
            String[] tagInfo = tag2.split("-");
            String tagName;
            if (tagInfo.length == 3) {
                tagName = tagInfo[2];
            } else {
                tagName = tag2;
            }
            mActivityDemandDetailBinding.tvDemandDetailTag2.setText(tagName);
        }//加载第三个tag
        if (TextUtils.isEmpty(tag3)) {
            mActivityDemandDetailBinding.tvDemandDetailTag3.setVisibility(View.GONE);
        } else {
            mActivityDemandDetailBinding.tvDemandDetailTag3.setVisibility(View.VISIBLE);
            String[] tagInfo = tag3.split("-");
            String tagName;
            if (tagInfo.length == 3) {
                tagName = tagInfo[2];
            } else {
                tagName = tag3;
            }
            mActivityDemandDetailBinding.tvDemandDetailTag3.setText(tagName);
        }
    }

    public void displayServicePic(String pic1FileId, String pic2FileId, String pic3FileId, String pic4FileId, String pic5FileId, String pic6FileId) {
        if (!TextUtils.isEmpty(pic1FileId)) {
            //加载第1张图片
            mActivityDemandDetailBinding.flDemandDetailPicbox1.setVisibility(View.VISIBLE);
            LogKit.v("pic1FileId:" + pic1FileId);
            BitmapKit.bindImage(mActivityDemandDetailBinding.ivDemandDetailPic1, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic1FileId);
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox1.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(pic2FileId)) {
            //加载第2张图片
            mActivityDemandDetailBinding.flDemandDetailPicbox2.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityDemandDetailBinding.ivDemandDetailPic2, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic2FileId);
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox2.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(pic3FileId)) {
            //加载第3张图片
            mActivityDemandDetailBinding.flDemandDetailPicbox3.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityDemandDetailBinding.ivDemandDetailPic3, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic3FileId);
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox3.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(pic4FileId)) {
            //加载第4张图片
            mActivityDemandDetailBinding.flDemandDetailPicbox4.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityDemandDetailBinding.ivDemandDetailPic4, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic4FileId);
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox4.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(pic5FileId)) {
            //加载第5张图片
            mActivityDemandDetailBinding.flDemandDetailPicbox5.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityDemandDetailBinding.ivDemandDetailPic5, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic5FileId);
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox5.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(pic6FileId)) {//这种情况应该不存在，因为最多只能上传5张
            //加载第6张图片
            mActivityDemandDetailBinding.flDemandDetailPicbox6.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityDemandDetailBinding.ivDemandDetailPic6, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic6FileId);
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox6.setVisibility(View.INVISIBLE);
        }

    }

    DemandDetailBean demandDetailBean;

    /**
     * 获取需求详情信息
     */
    private void getDemandDetailDataFromServer() {
        DemandEngine.getDemandDetail(new BaseProtocol.IResultExecutor<DemandDetailBean>() {
            @Override
            public void execute(DemandDetailBean dataBean) {
                demandDetailBean = dataBean;
                DemandDetailBean.Demand demand = dataBean.data.demand;
                if (LoginManager.currentLoginUserId == demand.uid) {//需求者视角
                    setTopShareBtnVisibility(View.GONE);
                    setTopDemandBtnVisibility(View.VISIBLE);
                    setBottomBtnServiceVisibility(View.GONE);
                    setBottomBtnDemandVisibility(View.VISIBLE);
                } else {//服务者视角
                    setTopShareBtnVisibility(View.VISIBLE);
                    setTopDemandBtnVisibility(View.GONE);
                    setBottomBtnServiceVisibility(View.VISIBLE);
                    setBottomBtnDemandVisibility(View.GONE);
                }
                setDemandTitle(demand.title);
                String starttimeStr = "";
                if (demand.starttime != 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 hh:mm");
                    starttimeStr = sdf.format(demand.starttime);
                    setDemandStartTime("开始:" + starttimeStr);
                } else {
                    setDemandStartTime("随时");
                }
                //回填抢单浮层中的开始时间
                mActivityDemandDetailBinding.tvBidDemandStarttime.setText(starttimeStr);
                bidDemandStarttime = demand.starttime;
                if (demand.quote == 0) {
                    setQuote("服务方报价");
                    //填写抢单浮层中的报价
                    mActivityDemandDetailBinding.etBidDemandQuote.setText("");
                } else {
                    setQuote("¥" + demand.quote + "元");
                    //填写抢单浮层中的报价
                    mActivityDemandDetailBinding.etBidDemandQuote.setText(demand.quote + "");
                }
                //浏览量暂时无法获取,接口中好像没有浏览量字段
                if (demand.pattern == 1) {//线下
                    setOfflineItemVisibility(View.VISIBLE);
                    setDemandPlace("约定地点" + demand.place);
                } else if (demand.pattern == 0) {//线上
//                    setOfflineItemVisibility(View.GONE);
                    setOfflineItemVisibility(View.VISIBLE);
                    mActivityDemandDetailBinding.tvOnlineOfflineLabel.setText("线上");
                    setDemandDetailLocationVisibility(View.GONE);
                }
                if (demand.instalment == 0) {//不开启
                    setInstalmentItemVisibility(View.GONE);
                    //填写抢单浮层中的分期
                    bidIsInstalment = true;//调用toggleInstalment方法后就变成false了
                    toggleInstalment(null);
                } else {//开启分期
                    setInstalmentItemVisibility(View.VISIBLE);
                    //填写抢单浮层中的分期
                    bidIsInstalment = false;//调用toggleInstalment方法后就变成true了
                    toggleInstalment(null);
                }
                //技能标签
                String[] tags = demand.tag.split(",");
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
                //发布时间
                SimpleDateFormat sdfPublishTime = new SimpleDateFormat("发布时间:yyyy年MM月dd日 HH:mm");//发布时间:9月18日 8:30
                String publishTimeStr = sdfPublishTime.format(demand.cts);
                setDemandPublishTime(publishTimeStr);
                //详情描述
                setDemandDesc(demand.desc);
                //详情图片
                String[] picFileIds = demand.pic.split(",");
                //如果demand.pic为""空字符喘，picFileIds的length也是1
                if (picFileIds.length <= 0 || TextUtils.isEmpty(demand.pic)) {//这种情况应该不存在，因为至少传一张图片
                    mActivityDemandDetailBinding.llDemandDetailPicLine1.setVisibility(View.GONE);
                    mActivityDemandDetailBinding.llDemandDetailPicLine2.setVisibility(View.GONE);
                    mActivityDemandDetailBinding.vPicUnderline.setVisibility(View.GONE);
                } else if (picFileIds.length > 0 && picFileIds.length <= 3) {
                    mActivityDemandDetailBinding.llDemandDetailPicLine1.setVisibility(View.VISIBLE);
                    mActivityDemandDetailBinding.llDemandDetailPicLine2.setVisibility(View.GONE);
                    if (picFileIds.length == 1) {
                        displayServicePic(picFileIds[0], null, null, null, null, null);
                    } else if (picFileIds.length == 2) {
                        displayServicePic(picFileIds[0], picFileIds[1], null, null, null, null);
                    } else if (picFileIds.length == 3) {
                        displayServicePic(picFileIds[0], picFileIds[1], picFileIds[2], null, null, null);
                    }
                } else {
                    mActivityDemandDetailBinding.llDemandDetailPicLine1.setVisibility(View.VISIBLE);
                    mActivityDemandDetailBinding.llDemandDetailPicLine2.setVisibility(View.VISIBLE);
                    if (picFileIds.length == 4) {
                        displayServicePic(picFileIds[0], picFileIds[1], picFileIds[2], picFileIds[3], null, null);
                    } else if (picFileIds.length == 5) {
                        displayServicePic(picFileIds[0], picFileIds[1], picFileIds[2], picFileIds[3], picFileIds[4], null);
                    } else if (picFileIds.length == 6) {//这种情况应该不存在，因为最多就只能传5张图片
                        displayServicePic(picFileIds[0], picFileIds[1], picFileIds[2], picFileIds[3], picFileIds[4], picFileIds[5]);
                    }
                }
                //纠纷处理方式
                if (demand.bp == 1) {
                    setDisputeHandingType("平台方式");
                    //填写抢单浮层中的纠纷处理方式
                    checkPlatformProcessing(null);
                } else if (demand.bp == 2) {
                    setDisputeHandingType("协商方式");
                    //填写抢单浮层中的纠纷处理方式
                    checkConsultProcessing(null);
                }
                //上架、下架显示 用isonline字段判断
//                    if(demand.isonline)

                getDemandUserInfo(demand.uid);//获取需求发布者的信息
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("查看需求详情失败:" + result);
            }
        }, demandId + "");
    }

    /**
     * 获取需求发布者的信息
     *
     * @param uid
     */
    private void getDemandUserInfo(final long uid) {
        UserInfoEngine.getOtherUserInfo(new BaseProtocol.IResultExecutor<UserInfoBean>() {
            @Override
            public void execute(UserInfoBean dataBean) {
                UserInfoBean.UInfo uinfo = dataBean.data.uinfo;
                BitmapKit.bindImage(mActivityDemandDetailBinding.ivDemandUserAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + uinfo.avatar);
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
                setDemandUserPlace(userPlace);

                if (uid != LoginManager.currentLoginUserId) {
                    getRecommendDemandData();//获取相似需求推荐
                }
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取需求发布者信息失败:" + result);
            }
        }, uid + "", "0");
    }

    ArrayList<DetailRecommendDemandList.RecommendDemandInfo> listRecommendDemand;

    /**
     * 从接口获取相似需求推荐的数据，当服务者视角看需求的时候，需要显示推荐需求
     */
    private void getRecommendDemandData() {
        DemandEngine.getDetailRecommendDemand(new BaseProtocol.IResultExecutor<DetailRecommendDemandList>() {
            @Override
            public void execute(DetailRecommendDemandList dataBean) {
                listRecommendDemand = dataBean.data.list;
                setRecommendDemandItemData();
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取需求服务列表失败");
            }
        }, demandId + "", "5");
    }

    private void setRecommendDemandItemData() {
        for (int i = 0; i < listRecommendDemand.size(); i++) {
            DetailRecommendDemandList.RecommendDemandInfo recommendDemandInfo = listRecommendDemand.get(i);
            ItemDetailRecommendDemandBinding itemDetailRecommendDemandBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_detail_recommend_demand, null, false);
            ItemDetailRecommendDemandModel itemDetailRecommendDemandModel = new ItemDetailRecommendDemandModel(itemDetailRecommendDemandBinding, mActivity, recommendDemandInfo);
            itemDetailRecommendDemandBinding.setItemDetailRecommendDemandModel(itemDetailRecommendDemandModel);
            mLlDemandRecommend.addView(itemDetailRecommendDemandBinding.getRoot());
        }
    }


    public void goBack(View v) {
        mActivity.finish();
    }

    //进行分享需求操作
    public void shareDemand(View v) {
//        ToastUtils.shortToast("Share Demand");
        openShareLayer();
    }

    //底部分享按钮的操作，需求者视角的时候从才会显示
    public void shareDemandBottom(View v) {
//        ToastUtils.shortToast("Share Demand Bottom");\
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
     * 分享给微信好友
     *
     * @param v
     */
    public void shareToWeChat(View v) {
        UMShareAPI mShareAPI = UMShareAPI.get(mActivity);
        if (mShareAPI.isInstall(mActivity, SHARE_MEDIA.WEIXIN)) {
            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN).withText("Good").withTargetUrl("https://www.baidu.com/").setCallback(umShareListener).share();
        }
    }

    /**
     * 分享到微信朋友圈
     *
     * @param v
     */
    public void shareToWeChatCircle(View v) {
        UMShareAPI mShareAPI = UMShareAPI.get(mActivity);
        if (mShareAPI.isInstall(mActivity, SHARE_MEDIA.WEIXIN_CIRCLE)) {
            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).withText("Good").withTargetUrl("https://www.baidu.com/").setCallback(umShareListener).share();
        }
    }

    /**
     * 分享到QQ
     *
     * @param v
     */
    public void shareToQQ(View v) {
        UMShareAPI mShareAPI = UMShareAPI.get(mActivity);
        if (mShareAPI.isInstall(mActivity, SHARE_MEDIA.QQ)) {
            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QQ).withText("Good").withTargetUrl("https://www.baidu.com/").setCallback(umShareListener).share();
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
        new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QZONE).withText("Good").withTargetUrl("https://www.baidu.com/").setCallback(umShareListener).share();
    }


    //修改需求内容，会跳转到发布需求的页面，并在发布需求页面自动填充已有的内容
    public void updateDemand(View v) {
        Intent intentPublishDemandBaseInfo = new Intent(CommonUtils.getContext(), PublishDemandBaseInfoActivity.class);
//        intentPublishDemandBaseInfo.putExtra("update", "update");
        intentPublishDemandBaseInfo.putExtra("demandDetailBean", demandDetailBean);
        mActivity.startActivity(intentPublishDemandBaseInfo);
        mActivity.finish();
        if (PublishDemandSuccessActivity.mActivity != null) {
            PublishDemandSuccessActivity.mActivity.finish();
            PublishDemandSuccessActivity.mActivity = null;
        }
    }

    //下架需求操作
    public void offShelfDemand(View v) {
//        DemandEngine.cancelDemand(new BaseProtocol.IResultExecutor<CommonResultBean>() {
//            @Override
//            public void execute(CommonResultBean dataBean) {
//                setOffShelfLogoVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void executeResultError(String result) {
//                ToastUtils.shortToast("下架需求失败:" + result);
//            }
//        }, demandId + "");

        MyTaskEngine.upAndDownTask(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                setOffShelfLogoVisibility(View.VISIBLE);
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("下架需求失败:" + result);
            }
        }, demandId + "", "1", "0");
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
        DemandEngine.collectDemand(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("收藏成功");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("收藏失败:" + result);
            }
        }, demandId + "");
    }


    //定位需求详情中的地址
    public void openDemandDetailLocation(View v) {
        Intent intentDemandDetailLocationActivity = new Intent(CommonUtils.getContext(), DemandDetailLocationActivity.class);
//        intentDemandDetailLocationActivity.putExtra("demandLatLng", demandLatLng);
        mActivity.startActivity(intentDemandDetailLocationActivity);
    }

    /**
     * 打开抢单信息浮层
     *
     * @param v
     */
    public void openBidDemandLayer(View v) {
        setBidInfoVisibility(View.VISIBLE);
    }

    /**
     * 关闭抢单信息浮层
     *
     * @param v
     */
    public void closeBidDemandLayer(View v) {
        setBidInfoVisibility(View.GONE);
    }

    /**
     * 打开修改抢单时间
     *
     * @param v
     */
    public void openTimeLayer(View v) {
        setChooseDateTimeLayerVisibility(View.VISIBLE);
    }

    /**
     * 取消选择时间
     *
     * @param v
     */
    public void cancelChooseTime(View v) {
        setChooseDateTimeLayerVisibility(View.GONE);
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

    private int mBidCurrentChooseYear;
    private int mBidCurrentChooseMonth;
    private int mBidCurrentChooseDay;
    private int mBidCurrentChooseHour;
    private int mBidCurrentChooseMinute;
    private long bidDemandStarttime;

    /**
     * 确定选择的时间
     *
     * @param v
     */
    public void okChooseTime(View v) {
        setChooseDateTimeLayerVisibility(View.GONE);
        mBidCurrentChooseYear = sdtpBidDemandStarttime.getCurrentChooseYear();
        mBidCurrentChooseMonth = sdtpBidDemandStarttime.getCurrentChooseMonth();
        mBidCurrentChooseDay = sdtpBidDemandStarttime.getCurrentChooseDay();
        mBidCurrentChooseHour = sdtpBidDemandStarttime.getCurrentChooseHour();
        mBidCurrentChooseMinute = sdtpBidDemandStarttime.getCurrentChooseMinute();
        String bidStarttimeStr = mBidCurrentChooseMonth + "月" + mBidCurrentChooseDay + "日" + "-" + mBidCurrentChooseHour + ":" + (mBidCurrentChooseMinute < 10 ? "0" + mBidCurrentChooseMinute : mBidCurrentChooseMinute);
        mActivityDemandDetailBinding.tvBidDemandStarttime.setText(bidStarttimeStr);
        convertTimeToMillis();
    }

    public void convertTimeToMillis() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, mBidCurrentChooseYear);
        calendar.set(Calendar.MONTH, mBidCurrentChooseMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, mBidCurrentChooseDay);
        calendar.set(Calendar.HOUR_OF_DAY, mBidCurrentChooseHour);
        calendar.set(Calendar.MINUTE, mBidCurrentChooseMinute);
        bidDemandStarttime = calendar.getTimeInMillis();
    }

    private int bidBp;

    /**
     * 抢单时 选择平台处理方式
     *
     * @param v
     */
    public void checkPlatformProcessing(View v) {
        mActivityDemandDetailBinding.ivPlatformProcessingIcon.setImageResource(R.mipmap.pitchon_btn);
        mActivityDemandDetailBinding.ivConsultProcessingIcon.setImageResource(R.mipmap.default_btn);
        bidBp = 1;
    }

    /**
     * 抢单时 选择写上方式
     *
     * @param v
     */
    public void checkConsultProcessing(View v) {
        mActivityDemandDetailBinding.ivPlatformProcessingIcon.setImageResource(R.mipmap.default_btn);
        mActivityDemandDetailBinding.ivConsultProcessingIcon.setImageResource(R.mipmap.pitchon_btn);
        bidBp = 2;
    }

    private boolean bidIsInstalment;

    /**
     * 抢单 开启 关闭 分期
     *
     * @param v
     */
    public void toggleInstalment(View v) {
        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) mActivityDemandDetailBinding.ivBidDemandInstalmentHandle.getLayoutParams();
        if (bidIsInstalment) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            mActivityDemandDetailBinding.ivBidDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle_weijihuo);
            //关闭分期 隐藏分期比率
            hideInstalmentRatio();
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            mActivityDemandDetailBinding.ivBidDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle);
            //开启分期 显示已输入的分期比率
            displayInstalmentRatio();
        }
        mActivityDemandDetailBinding.ivBidDemandInstalmentHandle.setLayoutParams(layoutParams);
        bidIsInstalment = !bidIsInstalment;
    }

    private int instalmentCount = 0;

    /**
     * 关闭分期时，隐藏分期比例
     */
    private void hideInstalmentRatio() {
        setInstalmentRatioVisibility(View.GONE);
    }

    /**
     * 开启分期时，显示已填写的分期比例
     */
    private void displayInstalmentRatio() {
        setInstalmentRatioVisibility(View.VISIBLE);
    }

    /**
     * 删除分期
     *
     * @param v
     */
    public void deleteInstalment(View v) {
        setAddInstalmentIconVisibility(View.VISIBLE);
        if (bidIsInstalment) {
            if (instalmentCount > 0) {
                instalmentCount--;
                if (instalmentCount == 0) {
                    setUpdateInstalmentLine1Visibility(View.GONE);
                } else if (instalmentCount == 1) {
                    setUpdateInstalmentLine2Visibility(View.GONE);
                } else if (instalmentCount == 2) {
                    setUpdateInstalmentLine3Visibility(View.GONE);
                } else {//instalmentCount=3
                    setUpdateInstalmentLine4Visibility(View.GONE);
                }
            }
        }
    }

    /**
     * 添加分期
     *
     * @param v
     */
    public void addInstalment(View v) {
        setAddInstalmentIconVisibility(View.VISIBLE);
        if (bidIsInstalment) {
            if (instalmentCount < 4) {
                boolean isAddable = checkIsAddedable();
                if (isAddable) {
                    instalmentCount++;
                    if (instalmentCount == 1) {
                        setUpdateInstalmentLine1Visibility(View.VISIBLE);
                    } else if (instalmentCount == 2) {
                        setUpdateInstalmentLine2Visibility(View.VISIBLE);
                    } else if (instalmentCount == 3) {
                        setUpdateInstalmentLine3Visibility(View.VISIBLE);
                    } else {//instalmentCount=4
                        setUpdateInstalmentLine4Visibility(View.VISIBLE);
                        setAddInstalmentIconVisibility(View.GONE);
                    }
                } else {
                    ToastUtils.shortToast("请正确填写分期比率");
                }
            }
        }
    }

    /**
     * 判断是否可以添加下一期，如果已经填写了分期比率，就可以添加，如果未填写，或者填写有误，就不能添加下一期
     */
    private boolean checkIsAddedable() {
        String ratioStr;
        if (instalmentCount == 0) {
            return true;
        } else if (instalmentCount == 1) {
            ratioStr = mActivityDemandDetailBinding.etBidDemandInstalmentRatio1.getText().toString();
        } else if (instalmentCount == 2) {
            ratioStr = mActivityDemandDetailBinding.etBidDemandInstalmentRatio2.getText().toString();
        } else if (instalmentCount == 3) {
            ratioStr = mActivityDemandDetailBinding.etBidDemandInstalmentRatio3.getText().toString();
        } else {
            return false;
        }
        if (ratioStr.endsWith("%")) {
            ratioStr = ratioStr.substring(0, ratioStr.length() - 1);
        }
        try {
            double ratio = Double.parseDouble(ratioStr);
            if (ratio <= 0) {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    ArrayList<Double> bidDemandInstalmentRatioList = new ArrayList<Double>();

    /**
     * 抢单信息填写完毕后，立即抢单
     *
     * @param v
     */
    public void bidDemand(View v) {
        double bidQuote;
        String bidQuoteStr = mActivityDemandDetailBinding.etBidDemandQuote.getText().toString();
        try {
            bidQuote = Double.parseDouble(bidQuoteStr);
            if (bidQuote <= 0) {
                ToastUtils.shortToast("报价必须大于0元");
                return;
            }
        } catch (Exception ex) {
            ToastUtils.shortToast("请正确填写报价");
            return;
        }

        if (bidDemandStarttime <= 0) {
            ToastUtils.shortToast("请先完善开始时间");
            return;
        }
        if (bidDemandStarttime <= System.currentTimeMillis() + 60 * 60 * 1000) {
            ToastUtils.shortToast("开始时间必须大于当前时间1个小时");
            return;
        }

        if (bidIsInstalment) {
            getInputInstalmentRatio();
        }
        if (bidIsInstalment == false) {
            bidDemandInstalmentRatioList.clear();
            bidDemandInstalmentRatioList.add(100d);
        } else if (bidDemandInstalmentRatioList.size() <= 0) {
            ToastUtils.shortToast("请先完善分期比例信息");
            return;
        } else {
            double totalRatio = 0;
            for (double ratio : bidDemandInstalmentRatioList) {
                totalRatio += ratio;
            }
            if (totalRatio != 100) {
                ToastUtils.shortToast("分期比例总和必须是100%");
                return;
            }
        }

        //调用抢需求接口
        DemandEngine.servicePartyBidDemand(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("抢单成功");
                setBidInfoVisibility(View.GONE);
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("抢单失败;" + result);
            }
        }, demandId + "", bidQuote + "", bidDemandInstalmentRatioList, bidBp + "", bidDemandStarttime + "");
    }

    private void getInputInstalmentRatio() {
        bidDemandInstalmentRatioList.clear();
        String ratioStr;
        if (instalmentCount >= 1) {
            ratioStr = mActivityDemandDetailBinding.etBidDemandInstalmentRatio1.getText().toString();
            double ratio = convertStrToRatio(ratioStr);
            if (ratio == -1) {
                ToastUtils.shortToast("请正确填写第一期分期比率");
                return;
            }
            bidDemandInstalmentRatioList.add(ratio);
        }
        if (instalmentCount >= 2) {
            ratioStr = mActivityDemandDetailBinding.etBidDemandInstalmentRatio2.getText().toString();
            double ratio = convertStrToRatio(ratioStr);
            if (ratio == -1) {
                ToastUtils.shortToast("请正确填写第二期分期比率");
                return;
            }
            bidDemandInstalmentRatioList.add(ratio);
        }
        if (instalmentCount >= 3) {
            ratioStr = mActivityDemandDetailBinding.etBidDemandInstalmentRatio3.getText().toString();
            double ratio = convertStrToRatio(ratioStr);
            if (ratio == -1) {
                ToastUtils.shortToast("请正确填写第三期分期比率");
                return;
            }
            bidDemandInstalmentRatioList.add(ratio);
        }
        if (instalmentCount >= 4) {
            ratioStr = mActivityDemandDetailBinding.etBidDemandInstalmentRatio4.getText().toString();
            double ratio = convertStrToRatio(ratioStr);
            if (ratio == -1) {
                ToastUtils.shortToast("请正确填写第四期分期比率");
                return;
            }
            bidDemandInstalmentRatioList.add(ratio);
        }
    }

    /**
     * @param ratioStr
     * @return
     */
    private double convertStrToRatio(String ratioStr) {
        double ratio;
        if (ratioStr.endsWith("%")) {
            ratioStr = ratioStr.substring(0, ratioStr.length() - 1);
        }
        try {
            ratio = Double.parseDouble(ratioStr);
            if (ratio <= 0) {
                return -1;
            }
        } catch (Exception ex) {
            return -1;
        }
        return ratio;
    }

    private int bottomBtnServiceVisibility;//服务者视角的底部按钮是否显示隐藏
    private int bottomBtnDemandVisibility;//需求者视角的底部按钮是否显示隐藏
    private int topShareBtnVisibility;//服务者视角的顶部分享按钮是否可见
    private int topDemandBtnVisibility;//需求者视角的顶部修改和下架按钮是否可见
    private int offShelfLogoVisibility = View.GONE;//已经下架的需求需要显示下架Logo

    private String demandTitle;//需求标题
    private String demandStartTime;//需求开始时间 开始:9月18日 8:30
    private String quote;//报价 ¥300元
    private String viewCount;//浏览量 300人浏览

    private int offlineItemVisibility;
    private String demandPlace;//"约定地点星湖街328号星湖广场"
    private int instalmentItemVisibility;

    private int isAuthVisibility;
    private String username;
    private String fanscount;
    private String taskcount;
    private String demandUserPlace;

    private String demandPublishTime;
    private String demandDesc;
    private String disputeHandingType;

    private int updateInstalmentLine1Visibility = View.GONE;
    private int updateInstalmentLine2Visibility = View.GONE;
    private int updateInstalmentLine3Visibility = View.GONE;
    private int updateInstalmentLine4Visibility = View.GONE;
    private int chooseDateTimeLayerVisibility = View.GONE;
    private int bidInfoVisibility = View.GONE;
    private int instalmentRatioVisibility;

    private int addInstalmentIconVisibility;
    private int shareLayerVisibility = View.GONE;

    private int demandDetailLocationVisibility;

    @Bindable
    public int getDemandDetailLocationVisibility() {
        return demandDetailLocationVisibility;
    }

    public void setDemandDetailLocationVisibility(int demandDetailLocationVisibility) {
        this.demandDetailLocationVisibility = demandDetailLocationVisibility;
        notifyPropertyChanged(BR.demandDetailLocationVisibility);
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
    public int getAddInstalmentIconVisibility() {
        return addInstalmentIconVisibility;
    }

    public void setAddInstalmentIconVisibility(int addInstalmentIconVisibility) {
        this.addInstalmentIconVisibility = addInstalmentIconVisibility;
        notifyPropertyChanged(BR.addInstalmentIconVisibility);
    }

    @Bindable
    public int getInstalmentRatioVisibility() {
        return instalmentRatioVisibility;
    }

    public void setInstalmentRatioVisibility(int instalmentRatioVisibility) {
        this.instalmentRatioVisibility = instalmentRatioVisibility;
        notifyPropertyChanged(BR.instalmentRatioVisibility);
    }

    @Bindable
    public int getBidInfoVisibility() {
        return bidInfoVisibility;
    }

    public void setBidInfoVisibility(int bidInfoVisibility) {
        this.bidInfoVisibility = bidInfoVisibility;
        notifyPropertyChanged(BR.bidInfoVisibility);
    }

    @Bindable
    public int getUpdateInstalmentLine4Visibility() {
        return updateInstalmentLine4Visibility;
    }

    public void setUpdateInstalmentLine4Visibility(int updateInstalmentLine4Visibility) {
        this.updateInstalmentLine4Visibility = updateInstalmentLine4Visibility;
        notifyPropertyChanged(BR.updateInstalmentLine4Visibility);
    }

    @Bindable
    public int getUpdateInstalmentLine3Visibility() {
        return updateInstalmentLine3Visibility;
    }

    public void setUpdateInstalmentLine3Visibility(int updateInstalmentLine3Visibility) {
        this.updateInstalmentLine3Visibility = updateInstalmentLine3Visibility;
        notifyPropertyChanged(BR.updateInstalmentLine3Visibility);
    }

    @Bindable
    public int getUpdateInstalmentLine2Visibility() {
        return updateInstalmentLine2Visibility;
    }

    public void setUpdateInstalmentLine2Visibility(int updateInstalmentLine2Visibility) {
        this.updateInstalmentLine2Visibility = updateInstalmentLine2Visibility;
        notifyPropertyChanged(BR.updateInstalmentLine2Visibility);
    }

    @Bindable
    public int getUpdateInstalmentLine1Visibility() {
        return updateInstalmentLine1Visibility;
    }

    public void setUpdateInstalmentLine1Visibility(int updateInstalmentLine1Visibility) {
        this.updateInstalmentLine1Visibility = updateInstalmentLine1Visibility;
        notifyPropertyChanged(BR.updateInstalmentLine1Visibility);
    }

    @Bindable
    public int getChooseDateTimeLayerVisibility() {
        return chooseDateTimeLayerVisibility;
    }

    public void setChooseDateTimeLayerVisibility(int chooseDateTimeLayerVisibility) {
        this.chooseDateTimeLayerVisibility = chooseDateTimeLayerVisibility;
        notifyPropertyChanged(BR.chooseDateTimeLayerVisibility);
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
    public String getDemandDesc() {
        return demandDesc;
    }

    public void setDemandDesc(String demandDesc) {
        this.demandDesc = demandDesc;
        notifyPropertyChanged(BR.demandDesc);
    }

    @Bindable
    public String getDemandPublishTime() {
        return demandPublishTime;
    }

    public void setDemandPublishTime(String demandPublishTime) {
        this.demandPublishTime = demandPublishTime;
        notifyPropertyChanged(BR.demandPublishTime);
    }

    @Bindable
    public String getDemandUserPlace() {
        return demandUserPlace;
    }

    public void setDemandUserPlace(String demandUserPlace) {
        this.demandUserPlace = demandUserPlace;
        notifyPropertyChanged(BR.demandUserPlace);
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
    public int getIsAuthVisibility() {
        return isAuthVisibility;
    }

    public void setIsAuthVisibility(int isAuthVisibility) {
        this.isAuthVisibility = isAuthVisibility;
        notifyPropertyChanged(BR.isAuthVisibility);
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
    public String getDemandPlace() {
        return demandPlace;
    }

    public void setDemandPlace(String demandPlace) {
        this.demandPlace = demandPlace;
        notifyPropertyChanged(BR.demandPlace);
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
}
