package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amap.api.maps2d.model.LatLng;
import com.google.gson.Gson;
import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityDemandDetailBinding;
import com.slash.youth.databinding.ItemDetailRecommendDemandBinding;
import com.slash.youth.domain.ChatCmdShareTaskBean;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.DemandDetailBean;
import com.slash.youth.domain.DetailRecommendDemandList;
import com.slash.youth.domain.UserInfoBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.DemandDetailLocationActivity;
import com.slash.youth.ui.activity.MyFriendActivtiy;
import com.slash.youth.ui.activity.PublishDemandBaseInfoActivity;
import com.slash.youth.ui.activity.PublishDemandSuccessActivity;
import com.slash.youth.ui.activity.ReportTaskActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.view.SlashDateTimePicker;
import com.slash.youth.ui.view.TouchImageView;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.DialogUtils;
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
    boolean isFromDetail;
    long demandUserId;
    LatLng demandLatLng;
    int pattern;
    int isonline;
    int anonymity;

    public DemandDetailModel(ActivityDemandDetailBinding activityDemandDetailBinding, Activity activity) {
        this.mActivityDemandDetailBinding = activityDemandDetailBinding;
        this.mActivity = activity;
        initData();
        initView();
        initListener();
    }

    private void initData() {
        demandId = mActivity.getIntent().getLongExtra("demandId", -1);
        isFromDetail = mActivity.getIntent().getBooleanExtra("isFromDetail", false);//用来判断是否是从详情页中的推荐跳转过来的
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
        //显示加载层
        setLoadLayerVisibility(View.VISIBLE);
        DemandEngine.getDemandDetail(new BaseProtocol.IResultExecutor<DemandDetailBean>() {
            @Override
            public void execute(DemandDetailBean dataBean) {
                //隐藏加载层
                setLoadLayerVisibility(View.GONE);

                demandDetailBean = dataBean;
                DemandDetailBean.Demand demand = dataBean.data.demand;
                anonymity = demand.anonymity;
                demandLatLng = new LatLng(demand.lat, demand.lng);
                demandUserId = demand.uid;
                if (LoginManager.currentLoginUserId == demand.uid) {//需求者视角
                    setDemandRecommendLabelVisibility(View.GONE);

                    setTopDemandBtnVisibility(View.VISIBLE);

                    int status = demand.status;
                    if (status == 0 || status == 1) {
                        setUpdateBtnVisibility(View.VISIBLE);
                        setOffShelfBtnVisibility(View.VISIBLE);
                        setRemarkBtnVisibility(View.GONE);
                        //这里使用isonline字段来判断是否已经上架
                        isonline = demand.isonline;
                        mActivityDemandDetailBinding.tvOnshelfOffshelf.setVisibility(View.VISIBLE);
                        if (isonline == 0) {//未上架，需要显示上架
                            mActivityDemandDetailBinding.tvOnshelfOffshelf.setText("上架");
                        } else {//已上架，需要显示下架
                            mActivityDemandDetailBinding.tvOnshelfOffshelf.setText("下架");
                        }
                    } else if (status == 2) {
                        setUpdateBtnVisibility(View.GONE);
                        setOffShelfBtnVisibility(View.GONE);
                        setRemarkBtnVisibility(View.VISIBLE);
                    } else {
                        setUpdateBtnVisibility(View.GONE);
                        setOffShelfBtnVisibility(View.GONE);
                        setRemarkBtnVisibility(View.GONE);
                    }

                    setBottomBtnServiceVisibility(View.GONE);
                    setBottomBtnDemandVisibility(View.VISIBLE);
                } else {//服务者视角
                    setDemandRecommendLabelVisibility(View.VISIBLE);

                    setTopDemandBtnVisibility(View.GONE);
                    setBottomBtnServiceVisibility(View.VISIBLE);
                    setBottomBtnDemandVisibility(View.GONE);

                    getBidDemandStatus();
                    getCollectionStatus();
                }
                setDemandTitle(demand.title);

//                if (demand.uid == LoginManager.currentLoginUserId) {
//                    setTopTitle("需求详情");
//                } else {
//                    setTopTitle(demand.title);
//                }
                setTopTitle(demand.title);

                String starttimeStr = "";
                if (demand.starttime != 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("开始时间:MM月dd日 HH:mm");
                    starttimeStr = sdf.format(demand.starttime);
                    setDemandStartTime(starttimeStr);
                } else {
                    setDemandStartTime("开始时间:随时");
                }
                //回填抢单浮层中的开始时间
                mActivityDemandDetailBinding.tvBidDemandStarttime.setText(starttimeStr);
                bidDemandStarttime = demand.starttime;
                if (demand.quote == 0) {
                    setQuote("服务方报价");
                    //填写抢单浮层中的报价
                    mActivityDemandDetailBinding.etBidDemandQuote.setText("");
                } else {
                    setQuote((int) demand.quote + "元");
                    //填写抢单浮层中的报价
                    mActivityDemandDetailBinding.etBidDemandQuote.setText((int) demand.quote + "");
                }
                //浏览量暂时无法获取,接口中好像没有浏览量字段
                pattern = demand.pattern;
                if (demand.pattern == 1) {//线下
                    setOfflineItemVisibility(View.VISIBLE);
//                    setDemandPlace("约定地点:" + demand.place != null ? demand.place : "");
                    setDemandPlace(demand.place != null ? demand.place : "");
                } else if (demand.pattern == 0) {//线上
//                    setOfflineItemVisibility(View.GONE);
                    setOfflineItemVisibility(View.VISIBLE);
                    mActivityDemandDetailBinding.tvOnlineOfflineLabel.setText("线上");
//                    setDemandDetailLocationVisibility(View.GONE);
                    setDemandPlace("线上解决，不受地域限制");
                    mActivityDemandDetailBinding.ivOfflinePlaceIcon.setVisibility(View.GONE);
                }
                if (demand.instalment == 0) {//不开启
//                    setInstalmentItemVisibility(View.GONE);//这里不是直接隐藏，而是显示成“一次性到账”
                    setInstalmentItemVisibility(View.VISIBLE);
                    mActivityDemandDetailBinding.tvInstalmentLabel.setText("一次性到账");
                    mActivityDemandDetailBinding.tvInstalmentContent.setText("服务完成后，资金一次性打入服务方账户");

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
//                SimpleDateFormat sdfPublishTime = new SimpleDateFormat("yyyy年MM月dd日 HH:mm发布");//9月18日 8:30
//                String publishTimeStr = sdfPublishTime.format(demand.cts);
//                setDemandPublishTime(publishTimeStr);
                //如果是今天发的，就显示时分;如果是之前发的，就显示月日;如果是去年发的，就显示年月日
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(demand.cts);
                int ctsYear = calendar.get(Calendar.YEAR);
                int ctsMonth = calendar.get(Calendar.MONTH);
                int ctsDay = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.setTime(new Date());
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                SimpleDateFormat publsihDatetimeSdf;
                String publicDatetimeStr;
                if (ctsYear != currentYear) {//去年
                    publsihDatetimeSdf = new SimpleDateFormat("yyyy年MM月dd日发布");
                    publicDatetimeStr = publsihDatetimeSdf.format(demand.cts);
                    setDemandPublishTime(publicDatetimeStr);
                } else {
                    if (currentMonth == ctsMonth && currentDay == ctsDay) {//今天
                        publsihDatetimeSdf = new SimpleDateFormat("HH:mm发布");
                        publicDatetimeStr = publsihDatetimeSdf.format(demand.cts);
                        setDemandPublishTime(publicDatetimeStr);
                    } else {//之前
                        publsihDatetimeSdf = new SimpleDateFormat("MM月dd日发布");
                        publicDatetimeStr = publsihDatetimeSdf.format(demand.cts);
                        setDemandPublishTime(publicDatetimeStr);
                    }
                }

                //详情描述
                setDemandDesc(demand.desc);
                //设置备注
                if (TextUtils.isEmpty(demand.remark)) {
                    setRemarksVisibility(View.GONE);
                } else {
                    setRemarksVisibility(View.VISIBLE);
//                    setDemandRemark("\\u3000\\u3000\\u3000\\u3000" + demand.remark);
                    SpannableStringBuilder span = new SpannableStringBuilder("备注:" + demand.remark);
                    span.setSpan(new ForegroundColorSpan(0xff31C5E4), 0, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    span.setSpan(new AbsoluteSizeSpan(CommonUtils.dip2px(15)), 0, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    mActivityDemandDetailBinding.tvDemandRemark.setText(span);
                }
                //详情图片
                String[] picFileIds = demand.pic.split(",");
                for (String fileId : picFileIds) {
                    listViewPicFileIds.add(fileId);
                }
                mActivityDemandDetailBinding.vpViewPic.setAdapter(new ViewPicPagerAdapter());
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
                if (demand.isonline == 0) {//下架状态
                    setOffShelfLogoVisibility(View.VISIBLE);
                } else {//上架状态
                    setOffShelfLogoVisibility(View.GONE);
                }

                getDemandUserInfo(demand.uid);//获取需求发布者的信息
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("查看需求详情失败:" + result);
            }
        }, demandId + "");
    }

    String avatarUrl;//第三方分享中用到的头像地址，全路径
    String demandUserAvatar;//需求者头像地址fileId，非全路径，只是头像地址的fileId

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
                demandUserAvatar = uinfo.avatar;
                avatarUrl = GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + uinfo.avatar;
                if (anonymity == 1) {//实名
                    BitmapKit.bindImage(mActivityDemandDetailBinding.ivDemandUserAvatar, avatarUrl);
                    setUsername(uinfo.name);
                } else {//匿名
                    mActivityDemandDetailBinding.ivDemandUserAvatar.setImageResource(R.mipmap.anonymity_avater);
                    String anonymityName;
                    if (TextUtils.isEmpty(uinfo.name)) {
                        anonymityName = "XXX";
                    } else {
                        anonymityName = uinfo.name.substring(0, 1) + "XX";
                    }
                    setUsername(anonymityName);
                }
                if (uinfo.isauth == 0) {//未认证
                    setIsAuthVisibility(View.INVISIBLE);
                } else if (uinfo.isauth == 1) {//已认证
                    setIsAuthVisibility(View.VISIBLE);
                }
//                setFanscount("粉丝数" + uinfo.fanscount);
                setFanscount("人脉数" + uinfo.fanscount);
                setTaskcount("顺利成交单数" + uinfo.achievetaskcount + "/" + uinfo.totoltaskcount);//顺利成交单数9/12
                String userPlace = "";
                if (uinfo.province.equals(uinfo.city)) {
                    userPlace = uinfo.province;
                } else {
                    userPlace = uinfo.province + uinfo.city;
                }
                if (TextUtils.isEmpty(userPlace)) {
                    setDemandUserPlace("暂未填写");
                } else {
                    setDemandUserPlace(userPlace);
                }

                if (uid != LoginManager.currentLoginUserId) {
                    getRecommendDemandData(uid);//获取相似需求推荐
                }

                initShareInfo();
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
    private void getRecommendDemandData(final long uid) {
        if (!isFromDetail) {
            DemandEngine.getDetailRecommendDemand(new BaseProtocol.IResultExecutor<DetailRecommendDemandList>() {
                @Override
                public void execute(DetailRecommendDemandList dataBean) {
                    listRecommendDemand = dataBean.data.list;
                    setRecommendDemandItemData();
//                if (uid != LoginManager.currentLoginUserId) {
//                    getBidDemandStatus(uid);
//                }
                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("获取需求服务列表失败");
                }
            }, demandId + "", "5");
        } else {
            setDemandRecommendLabelVisibility(View.GONE);
            setDemandRecommendItemVisibility(View.GONE);
        }
    }

    private void setRecommendDemandItemData() {
        for (int i = 0; i < listRecommendDemand.size(); i++) {
            final DetailRecommendDemandList.RecommendDemandInfo recommendDemandInfo = listRecommendDemand.get(i);
            ItemDetailRecommendDemandBinding itemDetailRecommendDemandBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_detail_recommend_demand, null, false);
            ItemDetailRecommendDemandModel itemDetailRecommendDemandModel = new ItemDetailRecommendDemandModel(itemDetailRecommendDemandBinding, mActivity, recommendDemandInfo);
            itemDetailRecommendDemandBinding.setItemDetailRecommendDemandModel(itemDetailRecommendDemandModel);
            View itemView = itemDetailRecommendDemandBinding.getRoot();
            if (!isFromDetail) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
                        intentDemandDetailActivity.putExtra("demandId", recommendDemandInfo.id);
                        intentDemandDetailActivity.putExtra("isFromDetail", true);
                        mActivity.startActivity(intentDemandDetailActivity);
                    }
                });
            }
            mLlDemandRecommend.addView(itemView);
        }
    }

    boolean isBidDemand = false;//表示是否抢单过某需求

    /**
     * 获取当前登录用户是否抢过这个需求，服务者视角才需要
     */
    private void getBidDemandStatus() {
        MyTaskEngine.getBidTaskStatus(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                if (dataBean.data.status == 1) {//1预约过某服务或者抢单过某需求
                    setBidDemandSuccessStatus();
                } else {//0未预约过某服务或者未抢单过某需求
                    isBidDemand = false;
                }
//                if (uid != LoginManager.currentLoginUserId) {
//                    getCollectionStatus();
//                }
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取抢单状态失败:" + result);
            }
        }, "1", demandId + "");
    }

    private void setBidDemandSuccessStatus() {
        mActivityDemandDetailBinding.tvBidDemand.setText("抢单成功");
        mActivityDemandDetailBinding.tvBidDemand.setBackgroundColor(0xffcccccc);
        mActivityDemandDetailBinding.tvBidDemand.setClickable(false);
        isBidDemand = true;
    }

    boolean isCollectionDemand;

    /**
     * 获取当前登录者收藏需求的状态
     */
    private void getCollectionStatus() {
        MyTaskEngine.getCollectionStatus(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                if (dataBean.data.status == 1) {//1表示收藏过
                    setCollectionState();
                    isCollectionDemand = true;
                } else {// 0表示未收藏过
                    setNoCollectionState();
                    isCollectionDemand = false;
                }
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取收藏需求状态失败:" + result);
            }
        }, "1", demandId + "");
    }

    /**
     * 设置收藏状态
     */
    private void setCollectionState() {
        Drawable topDrawable = CommonUtils.getContext().getResources().getDrawable(R.mipmap.yi_heart);
        int intrinsicWidth = topDrawable.getIntrinsicWidth();
        int intrinsicHeight = topDrawable.getIntrinsicHeight();
        topDrawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        mActivityDemandDetailBinding.tvCollection.setCompoundDrawables(null, topDrawable, null, null);
//        mActivityDemandDetailBinding.tvCollection.setText("取消收藏");
        mActivityDemandDetailBinding.tvCollection.setText("收藏");
    }

    /**
     * 设置未收藏状态
     */
    private void setNoCollectionState() {
        Drawable topDrawable = CommonUtils.getContext().getResources().getDrawable(R.mipmap.collection_icon);
        int intrinsicWidth = topDrawable.getIntrinsicWidth();
        int intrinsicHeight = topDrawable.getIntrinsicHeight();
        topDrawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        mActivityDemandDetailBinding.tvCollection.setCompoundDrawables(null, topDrawable, null, null);
        mActivityDemandDetailBinding.tvCollection.setText("收藏");
    }

    String shareTitle;
    String shareContent;
    UMImage shareAvatar;
    String shareUrl;

    private void initShareInfo() {
        shareTitle = getUsername() + "发布了需求《" + getDemandTitle() + "》";
        shareContent = "赶紧来抢单吧";
        shareAvatar = new UMImage(CommonUtils.getContext(), avatarUrl);
        shareUrl = ShareUtils.DETAIL_SHARE + "?nav=1&param=1&oid=" + demandId + "&favei=1&cid=" + LoginManager.currentLoginUserId + "&share=visit";
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    //进行分享需求操作
    public void shareDemand(View v) {
//        ToastUtils.shortToast("Share Demand");
        openShareLayer();
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_IMMEDIATELY_SHARE);
    }

    //底部分享按钮的操作，需求者视角的时候从才会显示
    public void shareDemandBottom(View v) {
//        ToastUtils.shortToast("Share Demand Bottom");\
        openShareLayer();
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_IMMEDIATELY_SHARE);
    }

    private void openShareLayer() {
        setShareLayerVisibility(View.VISIBLE);
    }

    /**
     * 取消分享按钮
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
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_IMMEDIATELY_SHARE_FRIEND);

        Intent intentMyFriendActivity = new Intent(CommonUtils.getContext(), MyFriendActivtiy.class);
        ChatCmdShareTaskBean chatCmdShareTaskBean = new ChatCmdShareTaskBean();
        chatCmdShareTaskBean.uid = demandUserId;
        chatCmdShareTaskBean.avatar = demandUserAvatar;
        chatCmdShareTaskBean.title = getDemandTitle();
        chatCmdShareTaskBean.quote = getQuote();
        chatCmdShareTaskBean.type = 1;
        chatCmdShareTaskBean.tid = demandId;
        intentMyFriendActivity.putExtra("chatCmdShareTaskBean", chatCmdShareTaskBean);
        mActivity.startActivity(intentMyFriendActivity);

        setShareLayerVisibility(View.GONE);
    }

    /**
     * 分享给微信好友
     *
     * @param v
     */
    public void shareToWeChat(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_RECOMMEND_FRIEND);

        UMShareAPI mShareAPI = UMShareAPI.get(mActivity);
        if (mShareAPI.isInstall(mActivity, SHARE_MEDIA.WEIXIN)) {
            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN).withMedia(shareAvatar).withTitle(shareTitle).withText(shareContent).withTargetUrl(shareUrl).setCallback(umShareListener).share();
        }

        setShareLayerVisibility(View.GONE);
    }

    /**
     * 分享到微信朋友圈
     *
     * @param v
     */
    public void shareToWeChatCircle(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_IMMEDIATELY_SHARE_WECHAT);

        UMShareAPI mShareAPI = UMShareAPI.get(mActivity);
        if (mShareAPI.isInstall(mActivity, SHARE_MEDIA.WEIXIN_CIRCLE)) {
            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).withMedia(shareAvatar).withTitle(shareTitle).withText(shareContent).withTargetUrl(shareUrl).setCallback(umShareListener).share();
        }

        setShareLayerVisibility(View.GONE);
    }

    /**
     * 分享到QQ
     *
     * @param v
     */
    public void shareToQQ(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_IMMEDIATELY_SHARE_QQ);

        UMShareAPI mShareAPI = UMShareAPI.get(mActivity);
        if (mShareAPI.isInstall(mActivity, SHARE_MEDIA.QQ)) {
            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QQ).withMedia(shareAvatar).withTitle(shareTitle).withText(shareContent).withTargetUrl(shareUrl).setCallback(umShareListener).share();
        } else {
            ToastUtils.shortToast("请先安装qq客户端");
        }

        setShareLayerVisibility(View.GONE);
    }

    /**
     * 分享到QQ空间
     *
     * @param v
     */
    public void shareToQZone(View v) {
        new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QZONE).withMedia(shareAvatar).withTitle(shareTitle).withText(shareContent).withTargetUrl(shareUrl).setCallback(umShareListener).share();

        setShareLayerVisibility(View.GONE);
    }


    //修改需求内容，会跳转到发布需求的页面，并在发布需求页面自动填充已有的内容
    public void updateDemand(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_MODIFY);
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
        if (isonline == 0) {//上架埋点
            MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_SHELVE);
        } else {//下架埋点
            MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_UNSHELVE);
        }

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
                String cmdStr;
                if (isonline == 0) {
                    cmdStr = "上架成功";
                    isonline = 1;
                    mActivityDemandDetailBinding.tvOnshelfOffshelf.setText("下架");
                    setOffShelfLogoVisibility(View.GONE);
                } else {
                    cmdStr = "下架成功";
                    isonline = 0;
                    mActivityDemandDetailBinding.tvOnshelfOffshelf.setText("上架");
                    setOffShelfLogoVisibility(View.VISIBLE);
                }
                ToastUtils.shortToast(cmdStr);
            }

            @Override
            public void executeResultError(String result) {
                //这里需要根据返回的状态做判断
                Gson gson = new Gson();
                CommonResultBean commonResultBean = gson.fromJson(result, CommonResultBean.class);
                int status = commonResultBean.data.status;
                if (status == 2) {
                    ToastUtils.shortToast("未认证，请先认证");
                } else if (status == 6) {
                    ToastUtils.shortToast("未绑定手机号，请先绑定手机号");
                } else if (status == 7) {
                    ToastUtils.shortToast("您还未实名认证，请先进行实名人认证");
                } else {
                    if (isonline == 0) {
                        ToastUtils.shortToast("上架失败:" + result);
                    } else {
                        ToastUtils.shortToast("下架失败:" + result);
                    }
                }
            }
        }, demandId + "", "1", (isonline == 0 ? 1 : 0) + "");
    }

    /**
     * 添加备注
     *
     * @param v
     */
    public void addRemarks(View v) {
//        ToastUtils.shortToast("添加备注");
        setAddRemarksLayerVisibility(View.VISIBLE);
    }

    /**
     * 关闭添加备注浮层
     *
     * @param v
     */
    public void closeAddRemarksLayer(View v) {
        setAddRemarksLayerVisibility(View.GONE);
    }

    /**
     * 输入完备注内容以后，确定添加备注
     *
     * @param v
     */
    public void sureAddRemarks(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_REMARK);
        final String remarks = mActivityDemandDetailBinding.etDemandRemarks.getText().toString();
        if (TextUtils.isEmpty(remarks)) {
            ToastUtils.shortToast("请输入备注信息");
            return;
        }
        DemandEngine.setDemandRemark(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                setAddRemarksLayerVisibility(View.GONE);
                SpannableStringBuilder span = new SpannableStringBuilder("备注:" + remarks);
                span.setSpan(new ForegroundColorSpan(0xff31C5E4), 0, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                span.setSpan(new AbsoluteSizeSpan(CommonUtils.dip2px(15)), 0, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mActivityDemandDetailBinding.tvDemandRemark.setText(span);
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("设置需求备注失败:" + result);
            }
        }, demandId + "", remarks);
    }

    //跳转到个人信息界面
    public void gotoUserInfo(View v) {
//        ToastUtils.shortToast("跳转至个人信息界面");
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_ENTER_PERSON_MESSAGE);
        Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
        intentUserInfoActivity.putExtra("Uid", demandUserId);
        intentUserInfoActivity.putExtra("anonymity", anonymity);
        mActivity.startActivity(intentUserInfoActivity);
    }

    //打开聊天功能
    public void haveAChat(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_CHAT);
        Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
        intentChatActivity.putExtra("targetId", demandUserId + "");
        mActivity.startActivity(intentChatActivity);

    }

    //收藏需求
    public void collectDemand(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_COLLECT);
        if (isCollectionDemand) {//已经收藏过了，点击取消收藏
            MyTaskEngine.cancelCollection(new BaseProtocol.IResultExecutor<CommonResultBean>() {
                @Override
                public void execute(CommonResultBean dataBean) {
                    ToastUtils.shortToast("取消收藏成功");
                    isCollectionDemand = false;
                    setNoCollectionState();
                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("取消收藏失败:" + result);
                }
            }, "1", demandId + "");
        } else {//还未收藏，点击收藏
            DemandEngine.collectDemand(new BaseProtocol.IResultExecutor<CommonResultBean>() {
                @Override
                public void execute(CommonResultBean dataBean) {
                    ToastUtils.shortToast("收藏成功");
                    isCollectionDemand = true;
                    setCollectionState();
                }

                @Override
                public void executeResultError(String result) {
                    ToastUtils.shortToast("收藏失败:" + result);
                }
            }, demandId + "");
        }
    }

    //定位需求详情中的地址
    public void openDemandDetailLocation(View v) {
        if (pattern == 1) {
            Intent intentDemandDetailLocationActivity = new Intent(CommonUtils.getContext(), DemandDetailLocationActivity.class);
            intentDemandDetailLocationActivity.putExtra("demandLatLng", demandLatLng);
            mActivity.startActivity(intentDemandDetailLocationActivity);
        }
    }

    /**
     * 打开抢单信息浮层
     *
     * @param v
     */
    public void openBidDemandLayer(View v) {
        if (!isBidDemand) {
            setBidInfoVisibility(View.VISIBLE);
        }
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
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_IMMEDIATELY_GRAB_SINGLE);
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
        if (bidDemandStarttime <= System.currentTimeMillis() + 2 * 60 * 60 * 1000) {
//            ToastUtils.shortToast("开始时间必须大于当前时间1个小时");
            ToastUtils.shortToast("开始时间必须是2小时以后");
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
        } else if (bidDemandInstalmentRatioList.size() == 1) {
            ToastUtils.shortToast("开启分期至少分两期");
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
                setBidDemandSuccessStatus();
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

    /**
     * 点击图片查看大图
     *
     * @param v
     */
    public void openViewPic(View v) {
        int currentViewIndex;
        switch (v.getId()) {
            case R.id.fl_demand_detail_picbox_1:
                currentViewIndex = 0;
                break;
            case R.id.fl_demand_detail_picbox_2:
                currentViewIndex = 1;
                break;
            case R.id.fl_demand_detail_picbox_3:
                currentViewIndex = 2;
                break;
            case R.id.fl_demand_detail_picbox_4:
                currentViewIndex = 3;
                break;
            case R.id.fl_demand_detail_picbox_5:
                currentViewIndex = 4;
                break;
            default:
                currentViewIndex = 5;
                break;
        }
        mActivityDemandDetailBinding.vpViewPic.setCurrentItem(currentViewIndex);
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
//            ImageView ivViewPic = new ImageView(CommonUtils.getContext());
            TouchImageView ivViewPic = new TouchImageView(CommonUtils.getContext());
            ivViewPic.setScaleType(ImageView.ScaleType.CENTER);
            String fileId = listViewPicFileIds.get(position);
//            BitmapKit.bindImage(ivViewPic, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + fileId);
//            BitmapKit.bindImage(ivViewPic, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + fileId,ImageView.ScaleType.CENTER_INSIDE);
            BitmapKit.bindImage(ivViewPic, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + fileId, ImageView.ScaleType.FIT_CENTER, 0);
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

    /**
     * 点击斜杠客服，与小助手聊天
     *
     * @param v
     */
    public void chatToSlashHelper(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_SLASH_SERVICE);
        Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
        intentChatActivity.putExtra("targetId", MsgManager.customerServiceUid);
        mActivity.startActivity(intentChatActivity);
    }

    String bpContent = "针对交易过程中出现的争议、纠纷等情况，本平台提供平台处理规则和双方协商规则的两种方式。\n" +
            "\n" +
            "平台处理规则：\n" +
            "若任务开启了分期到账，\n" +
            "1）未开始的分期阶段对应的资金，全额退给需求方\n" +
            "2）已开始未完成的或已完成未被需求方认可的分期阶段对应的资金，扣除顺利成交保证金（5%）后退款给需求方\n" +
            "3）已完成并被需求方认可的分期阶段对应的资金，扣除顺利成交保证金（5%）后划转给服务方。\n" +
            "若任务未开启分期到账，任务没有顺利完成的（需求方支付后，并没有认可对方的服务结果），扣除顺利成交保证金（5%）后退款给需求方。\n" +
            "\n" +
            "上述“开始”是指需求方支付后开始第一期服务或需求方确认某期服务后开始下期服务。\n" +
            "\n" +
            "双方协商规则：除平台处理方规则外，交易双方还可以选择“双方协商规则”方式处理纠纷。纠纷出现时，平台将依据双方提供的本客户端聊天截图、协议等资料来判断退款金额。对于处理结果双方不满意的，双方可以通过专业鉴定机构等第三方进行裁决，客服根据双方认可的裁决进行退款。\n" +
            "附则：本客户端之外的其他第三方聊天、通讯记录不具备法律效用。\n";
    String bpTitle = "纠纷处理";

    /**
     * 点击纠纷处理方式问号
     *
     * @param v
     */
    public void viewBpExplain(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_REQUIREMENT_DETAIL_DISPUTE_CONDUCT_BEHIND_QUESTION);
        DialogUtils.showDialogOne(mActivity, new DialogUtils.DialogCallUnderStandBack() {
            @Override
            public void OkDown() {
                LogKit.d("close viewBpExplain");
            }
        }, bpContent, bpTitle);
    }

    private static final String securityRulesTitle = "斜杠青年顺利成交保障规则";
    private static final String securityRulesContent = "斜杠青年通过顺利成交保证金、预支付、分期到账等一系列规则来保障双方的顺利成交。\n" +
            "\n" +
            "1、顺利成交保证金规则\n" +
            "\n" +
            "本服务平台将实际交易金额的5%计提为“顺利成交保证金”，任务顺利完成并且服务、需求双方评价分享后，平台将以交易金额的2.5%奖励形式返还给双方。\n" +
            "\n" +
            "如果任务并未顺利成交，已经开始的“服务阶段”对应的“顺利成交保证金”将，不予退还，存放奖金池 用于活动基金；未开始“服务阶段”对应的“顺利成交保证金”将退还给需求方。上述“服务阶段”是指双方用户达成的“分期到账”后各期对应的服务阶段。\n" +
            "\n" +
            "\n" +
            "2、预支付与分期到账规则\n" +
            "\n" +
            "预支付：为了保障交易双方的权益，双方确认交易意向后，应支付的全部款项将会一次性预支付且托管在斜杠平台。\n" +
            "\n" +
            "分期到帐：基于任务的阶段性特征，若双方将任务划分成若干个阶段，预支付的资金也将会被划分成对应的若干个部分，在需求方确认服务方提交的某阶段的服务时，该阶段的资金将被划转给服务方。\n" +
            "\n" +
            "3、到账与冻结规则\n" +
            "到账：\n" +
            "在没有开启分期付功能的情况下，任务顺利完成后，需求方确认服务后款项立即到账，即可提现；\n" +
            "在开启分期付功能后，非尾期账款在需求方确认服务后款项立即到账，暂时处于冻结状态；尾期账款在需求方确认服务后款项立即到账，全部资金可提现。\n" +
            "\n" +
            "冻结：\n" +
            "为了保障交易双方的权益，服务期间，涉及到当前任务的已到账资金处于冻结状态。冻结是暂时的，在全部任务顺利完成后，资金将解冻，服务方可以用于提现或支付。如果没有顺利完成，将按照双方选择的平台处理规则或者双方协商的规则来处理，具体请见“帮助-常见问题”。";


    /**
     * 打开《斜杠青年顺利成交保障规则》
     *
     * @param v
     */
    public void openSecurityRules(View v) {
        DialogUtils.showDialogOne(mActivity, new DialogUtils.DialogCallUnderStandBack() {
            @Override
            public void OkDown() {
                LogKit.d("close SecurityRules");
            }
        }, securityRulesContent, securityRulesTitle);
    }


    /**
     * 点击右上角3个点的，弹出举报按钮
     *
     * @param v
     */
    public void report(View v) {
        setReportBtnLayerVisibility(View.VISIBLE);
    }

    public void hideReportBtnLayer(View v) {
        setReportBtnLayerVisibility(View.GONE);
    }

    public void openReportActivity(View v) {
        setReportBtnLayerVisibility(View.GONE);
        Intent intentReportTaskActivity = new Intent(CommonUtils.getContext(), ReportTaskActivity.class);
        intentReportTaskActivity.putExtra("tid", demandId);
        intentReportTaskActivity.putExtra("type", 1);
        mActivity.startActivity(intentReportTaskActivity);
    }


    private int bottomBtnServiceVisibility;//服务者视角的底部按钮是否显示隐藏
    private int bottomBtnDemandVisibility;//需求者视角的底部按钮是否显示隐藏
    private int topDemandBtnVisibility;//需求者视角的顶部修改和下架按钮是否可见
    private int offShelfLogoVisibility = View.GONE;//已经下架的需求需要显示下架Logo

    private String demandTitle;//需求标题
    private String demandStartTime;//需求开始时间 开始:9月18日 8:30
    private String quote;//报价 300元
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

    private String topTitle;
    private int addRemarksLayerVisibility = View.GONE;
    private String demandRemark;
    private int remarksVisibility;

    private int updateBtnVisibility;
    private int remarkBtnVisibility;

    private int viewPicVisibility = View.GONE;

    private int demandRecommendLabelVisibility = View.GONE;

    private int demandRecommendItemVisibility;

    private int loadLayerVisibility = View.GONE;
    private int offShelfBtnVisibility;

    private int reportBtnLayerVisibility = View.GONE;

    @Bindable
    public int getReportBtnLayerVisibility() {
        return reportBtnLayerVisibility;
    }

    public void setReportBtnLayerVisibility(int reportBtnLayerVisibility) {
        this.reportBtnLayerVisibility = reportBtnLayerVisibility;
        notifyPropertyChanged(BR.reportBtnLayerVisibility);
    }

    @Bindable
    public int getOffShelfBtnVisibility() {
        return offShelfBtnVisibility;
    }

    public void setOffShelfBtnVisibility(int offShelfBtnVisibility) {
        this.offShelfBtnVisibility = offShelfBtnVisibility;
        notifyPropertyChanged(BR.offShelfBtnVisibility);
    }

    @Bindable
    public int getLoadLayerVisibility() {
        return loadLayerVisibility;
    }

    public void setLoadLayerVisibility(int loadLayerVisibility) {
        this.loadLayerVisibility = loadLayerVisibility;
        notifyPropertyChanged(BR.loadLayerVisibility);
    }

    @Bindable
    public int getDemandRecommendItemVisibility() {
        return demandRecommendItemVisibility;
    }

    public void setDemandRecommendItemVisibility(int demandRecommendItemVisibility) {
        this.demandRecommendItemVisibility = demandRecommendItemVisibility;
        notifyPropertyChanged(BR.demandRecommendItemVisibility);
    }

    @Bindable
    public int getDemandRecommendLabelVisibility() {
        return demandRecommendLabelVisibility;
    }

    public void setDemandRecommendLabelVisibility(int demandRecommendLabelVisibility) {
        this.demandRecommendLabelVisibility = demandRecommendLabelVisibility;
        notifyPropertyChanged(BR.demandRecommendLabelVisibility);
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
    public int getUpdateBtnVisibility() {
        return updateBtnVisibility;
    }

    public void setUpdateBtnVisibility(int updateBtnVisibility) {
        this.updateBtnVisibility = updateBtnVisibility;
        notifyPropertyChanged(BR.updateBtnVisibility);
    }

    @Bindable
    public int getRemarkBtnVisibility() {
        return remarkBtnVisibility;
    }

    public void setRemarkBtnVisibility(int remarkBtnVisibility) {
        this.remarkBtnVisibility = remarkBtnVisibility;
        notifyPropertyChanged(BR.remarkBtnVisibility);
    }

    @Bindable
    public int getRemarksVisibility() {
        return remarksVisibility;
    }

    public void setRemarksVisibility(int remarksVisibility) {
        this.remarksVisibility = remarksVisibility;
        notifyPropertyChanged(BR.remarksVisibility);
    }

    @Bindable
    public String getDemandRemark() {
        return demandRemark;
    }

    public void setDemandRemark(String demandRemark) {
        this.demandRemark = demandRemark;
        notifyPropertyChanged(BR.demandRemark);
    }

    @Bindable
    public int getAddRemarksLayerVisibility() {
        return addRemarksLayerVisibility;
    }

    public void setAddRemarksLayerVisibility(int addRemarksLayerVisibility) {
        this.addRemarksLayerVisibility = addRemarksLayerVisibility;
        notifyPropertyChanged(BR.addRemarksLayerVisibility);
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
