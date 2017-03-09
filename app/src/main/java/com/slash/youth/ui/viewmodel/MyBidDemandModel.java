package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyBidDemandBinding;
import com.slash.youth.databinding.ItemDemandFlowLogBinding;
import com.slash.youth.domain.CommonLogList;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.DemandBidInfoBean;
import com.slash.youth.domain.DemandDetailBean;
import com.slash.youth.domain.DemandInstalmentListBean;
import com.slash.youth.domain.InterventionBean;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.domain.MyTaskItemBean;
import com.slash.youth.domain.UserInfoBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.CommentActivity;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.view.RefreshScrollView;
import com.slash.youth.ui.view.SlashDateTimePicker;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class MyBidDemandModel extends BaseObservable {

    ActivityMyBidDemandBinding mActivityMyBidDemandBinding;
    Activity mActivity;
    private long tid;//需求ID
    private int type;//取值范围只能是: 1需求 2服务
    private int roleid;//表示是'我抢的单子' 还是 '我发布的任务' 1发布者 2抢单者
    private int fid;

    SlashDateTimePicker sdtpBidDemandStarttime;

    public MyBidDemandModel(ActivityMyBidDemandBinding activityMyBidDemandBinding, Activity activity) {
        this.mActivityMyBidDemandBinding = activityMyBidDemandBinding;
        this.mActivity = activity;
        displayLoadLayer();
        initData();
        initView();
        initListener();
    }

    //    ArrayList<DemandFlowLogList.LogInfo> listLogInfo = new ArrayList<DemandFlowLogList.LogInfo>();
    ArrayList<CommonLogList.CommonLogInfo> listLogInfo = new ArrayList<CommonLogList.CommonLogInfo>();

    private void initData() {
        Bundle taskInfo = mActivity.getIntent().getExtras();
        tid = taskInfo.getLong("tid");
        type = taskInfo.getInt("type");
        roleid = taskInfo.getInt("roleid");

        getDataFromServer();
    }

    private void getDataFromServer() {
        getTaskItemData();
        getDemandFlowLogData();
    }

    private void initView() {
        sdtpBidDemandStarttime = mActivityMyBidDemandBinding.sdtpBidDemandChooseDatetime;
    }

    private void initListener() {
        mActivityMyBidDemandBinding.scRefresh.setRefreshTask(new RefreshScrollView.IRefreshTask() {
            @Override
            public void refresh() {
                displayLoadLayer();
                getDataFromServer();
            }
        });
    }

    /**
     * 刚进入页面时，显示加载层
     */
    private void displayLoadLayer() {
        setLoadLayerVisibility(View.VISIBLE);
    }

    /**
     * 数据加载完毕后,隐藏加载层
     */
    private void hideLoadLayer() {
        setLoadLayerVisibility(View.GONE);
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void gotoUserInfoPage(View v) {
        Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
        switch (v.getId()) {
            case R.id.ll_demand_userinfo:
                //获取需求方uid
                intentUserInfoActivity.putExtra("Uid", innerDemandCardInfo.uid);
                break;
            case R.id.ll_service_userinfo:
                //我抢的需求，我就是服务方，所以这里不需要传uid
                break;
        }
        mActivity.startActivity(intentUserInfoActivity);
    }

    /**
     * 打开聊天界面聊一聊
     *
     * @param v
     */
    public void havaAChat(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_CHAT);
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_MISSONDETAIL_CLICK_MISSON_CHAT);

        Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
        intentChatActivity.putExtra("targetId", innerDemandCardInfo.uid + "");//对方的uid
        Bundle taskInfoBundle = new Bundle();
        taskInfoBundle.putLong("tid", tid);
        taskInfoBundle.putInt("type", 1);
        taskInfoBundle.putString("title", getDemandTitle());
        intentChatActivity.putExtra("taskInfo", taskInfoBundle);
        mActivity.startActivity(intentChatActivity);
    }


    /**
     * 抢需求者同意需求方
     *
     * @param v
     */
    public void accept(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_ACCEPT);
        DemandEngine.servicePartyConfirmServant(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("服务方确认成功");
                reloadData();
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("服务方确认失败:" + result);
            }
        }, tid + "");
    }

    /**
     * 抢需求者拒绝需求方
     *
     * @param v
     */
    public void noAccept(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_REJECT);

        DemandEngine.servicePartyReject(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("服务方拒绝成功");
                reloadData();
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("服务方拒绝失败:" + result);
            }
        }, tid + "");
    }

    /**
     * 服务方完成 任务
     *
     * @param v
     */
    public void complete(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_COMPLETE_MISSON);

        DemandEngine.servicePartyComplete(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("服务方完成成功");
                reloadData();
            }

            @Override
            public void executeResultError(String result) {
                Gson gson = new Gson();
                CommonResultBean commonResultBean = gson.fromJson(result, CommonResultBean.class);
                if (commonResultBean.data.status == 5 || commonResultBean.data.status == 6) {
                    ToastUtils.shortToast("请先设置交易密码");
                } else {
                    ToastUtils.shortToast("服务方完成失败:" + result);
                }
            }
        }, tid + "", fid + "");
    }

    /**
     * 同意退款
     *
     * @param v
     */
    public void agreeRefund(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_AGREEN_REFUND);
        DemandEngine.servicePartyAgreeRefund(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("服务方同意退款成功");
                reloadData();
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("服务方同意退款失败:" + result);
            }
        }, tid + "");
    }

    /**
     * 申诉，不同意退款
     *
     * @param v
     */
    public void complain(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_APPEAL);
        DemandEngine.servicePartyIntervention(new BaseProtocol.IResultExecutor<InterventionBean>() {
            @Override
            public void execute(InterventionBean dataBean) {
                ToastUtils.shortToast("服务方申诉成功");
                reloadData();
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("服务方申诉失败:" + result);
            }
        }, tid + "", "");
    }

    /**
     * 查看评价
     *
     * @param v
     */
    public void viewComment(View v) {

        Intent intentCommentActivity = new Intent(CommonUtils.getContext(), CommentActivity.class);

        Bundle commentInfo = new Bundle();
        commentInfo.putLong("tid", tid);
        commentInfo.putInt("type", type);
        commentInfo.putLong("suid", innerDemandCardInfo.suid);
        commentInfo.putLong("duid", innerDemandCardInfo.uid);
        commentInfo.putString("dAvatarUrl", dAvatarUrl);
        commentInfo.putString("sAvatarUrl", sAvatarUrl);
        commentInfo.putString("dname", dname);
        commentInfo.putString("sname", sname);
        intentCommentActivity.putExtras(commentInfo);

        mActivity.startActivity(intentCommentActivity);
    }

    private void getDemandFlowLogData() {
//        DemandEngine.getDemandFlowLog(new BaseProtocol.IResultExecutor<DemandFlowLogList>() {
//            @Override
//            public void execute(DemandFlowLogList dataBean) {
//                listLogInfo = dataBean.data.list;
//                if (listLogInfo == null || listLogInfo.size() <= 0) {
//                    DemandFlowLogList demandFlowLogList = new DemandFlowLogList();
//                    DemandFlowLogList.LogInfo logInfo2 = demandFlowLogList.new LogInfo();
//                    logInfo2.cts = System.currentTimeMillis();
//                    logInfo2.action = "需求方发布了需求";
//                    DemandFlowLogList.LogInfo logInfo = demandFlowLogList.new LogInfo();
//                    logInfo.action = "开始支付";
//                    logInfo.cts = System.currentTimeMillis() + 100;
//                    listLogInfo.add(logInfo2);
//                    listLogInfo.add(logInfo);
//                }
//                setDemandFlowLogItemData();
//            }
//
//            @Override
//            public void executeResultError(String result) {
//
//            }
//        }, tid + "");

        MyTaskEngine.getLog(new BaseProtocol.IResultExecutor<CommonLogList>() {
            @Override
            public void execute(CommonLogList dataBean) {
                listLogInfo = dataBean.data.list;
                setDemandFlowLogItemData();
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "", type + "", roleid + "");
    }

    public void setDemandFlowLogItemData() {
        mActivityMyBidDemandBinding.llDemandFlowLogs.removeAllViews();
        for (int i = listLogInfo.size() - 1; i >= 0; i--) {
            CommonLogList.CommonLogInfo logInfo = listLogInfo.get(i);
            View itemLogInfo = inflateItemLogInfo(logInfo);
            mActivityMyBidDemandBinding.llDemandFlowLogs.addView(itemLogInfo);
        }
    }

    public View inflateItemLogInfo(CommonLogList.CommonLogInfo logInfo) {
        ItemDemandFlowLogBinding itemDemandFlowLogBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_demand_flow_log, null, false);
        ItemDemandLogModel itemDemandLogModel = new ItemDemandLogModel(itemDemandFlowLogBinding, mActivity, logInfo);
        itemDemandFlowLogBinding.setItemDemanLogModel(itemDemandLogModel);
        return itemDemandFlowLogBinding.getRoot();
    }

    InnerDemandCardInfo innerDemandCardInfo = null;

    private void getTaskItemData() {
        MyTaskEngine.getMyTaskItem(new BaseProtocol.IResultExecutor<MyTaskItemBean>() {
            @Override
            public void execute(MyTaskItemBean myTaskItemBean) {
                MyTaskBean taskinfo = myTaskItemBean.data.taskinfo;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd HH:mm");
                String utsStr = sdf.format(taskinfo.uts);
                setTaskUts(utsStr);

                innerDemandCardInfo = new InnerDemandCardInfo();

                //innerDemandCardInfo.QQ_uid = taskinfo.QQ_uid;//这个任务列表中的uid暂时不准确，先不使用，使用需求详情中的uid
                innerDemandCardInfo.avatar = taskinfo.avatar;
                innerDemandCardInfo.username = taskinfo.name;
                innerDemandCardInfo.isAuth = taskinfo.isauth;
                innerDemandCardInfo.title = taskinfo.title;
                innerDemandCardInfo.starttime = taskinfo.starttime;
                innerDemandCardInfo.quote = taskinfo.quote;
                //这个字段使用分期列表接口中的
//                innerDemandCardInfo.instalment = taskinfo.instalment;//0 or 1 表示是否开启分期 0不允许分期 1允许分期
                innerDemandCardInfo.instalmentcurr = taskinfo.instalmentcurr;//表示当前处于第几个分期
                LogKit.v("taskinfo.instalmentcurr:" + taskinfo.instalmentcurr);
                innerDemandCardInfo.instalmentcurrfinish = taskinfo.instalmentcurrfinish;//表示当期是否服务方完成 0未完成 1已经完成
                //这个字段也使用分期列表接口来计算
//                innerDemandCardInfo.instalmentratio = taskinfo.instalmentratio;//表示分期情况，格式为30,20,10,40 (英文逗号分隔)
                innerDemandCardInfo.rectify = taskinfo.rectify;//是否使用过延期付款  1使用过，0未使用过
                innerDemandCardInfo.status = taskinfo.status;//使用我的任务中的status定义

                //通过调用需求详情接口补充一些信息，bp、suid、iscomment
                getDemandDetail();
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "", type + "", roleid + "");
    }

    int demandDetailStatus;//这个主要用来判断当需求方申请退款后，服务方是否已经同意退款或者申诉平台接入

    private void getDemandDetail() {
        DemandEngine.getDemandDetail(new BaseProtocol.IResultExecutor<DemandDetailBean>() {
            @Override
            public void execute(DemandDetailBean dataBean) {
                innerDemandCardInfo.uid = dataBean.data.demand.uid;//使用需求详情接口中的uid
                innerDemandCardInfo.suid = dataBean.data.demand.suid;
//                innerDemandCardInfo.bp = dataBean.data.demand.bp;//这个字段好像不对，用loadBid接口中的bp字段
                innerDemandCardInfo.isComment = dataBean.data.demand.iscomment;
                demandDetailStatus = dataBean.data.demand.status;

                loadBidInfo();
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "");
    }

    private void loadBidInfo() {
        LogKit.v("loadBidInfo LoginManager.currentLoginUserId:" + LoginManager.currentLoginUserId);
        LogKit.v("loadBidInfo innerDemandCardInfo.suid:" + innerDemandCardInfo.suid);
        LogKit.v("loadBidInfo tid:" + tid);
        DemandEngine.loadBidInfo(new BaseProtocol.IResultExecutor<DemandBidInfoBean>() {
            @Override
            public void execute(DemandBidInfoBean dataBean) {
                innerDemandCardInfo.bp = dataBean.data.bidinfo.bp;
                getInstalmentList();
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("加载抢单信息失败:" + result);
            }
        }, tid + "", LoginManager.currentLoginUserId + "");
        //这里不能使用innerDemandCardInfo.suid + ""，因为还没有形成1 V 1的关系，所以需求详情接口中的suid字段仍然是0
    }

    private void getInstalmentList() {
        //通过instalmentlist接口获取分期比例
        DemandEngine.getDemandInstalmentList(new BaseProtocol.IResultExecutor<DemandInstalmentListBean>() {
            @Override
            public void execute(DemandInstalmentListBean dataBean) {
                ArrayList<DemandInstalmentListBean.InstalmentInfo> instalmentList = dataBean.data.list;
                if (instalmentList.size() >= 2) {//分期至少分两期，如果只分一起，则视为不分期
                    innerDemandCardInfo.instalment = 1;
                } else {
                    innerDemandCardInfo.instalment = 0;
                }
                innerDemandCardInfo.instalmentratio = "";
                for (int i = 0; i < instalmentList.size(); i++) {
                    if (i < instalmentList.size() - 1) {
                        innerDemandCardInfo.instalmentratio += (instalmentList.get(i).percent + ",");
                    } else {
                        innerDemandCardInfo.instalmentratio += instalmentList.get(i).percent;
                    }
                }

                setMyPublishDemandInfo();
            }

            @Override
            public void executeResultError(String result) {
                LogKit.v("获取需求分期信息失败:" + result);
                ToastUtils.shortToast("获取需求分期信息失败:" + result);
            }
        }, tid + "");
    }

    private void setMyPublishDemandInfo() {
        //设置详细信息
        getDemandUserInfo();//获取需求方的个人信息
        getServiceUserInfo();//获取服务方的个人信息
        setDemandTitle(innerDemandCardInfo.title + "订单");
        SimpleDateFormat sdfStarttime = new SimpleDateFormat("开始时间:yyyy年MM月dd日 HH:mm");
        String starttimeStr = sdfStarttime.format(innerDemandCardInfo.starttime);
        setStarttime(starttimeStr);
        setQuote((int) innerDemandCardInfo.quote + "元");
        if (innerDemandCardInfo.instalment == 1) {//分期
            setInstalmentVisibility(View.VISIBLE);
            String[] ratios = innerDemandCardInfo.instalmentratio.split(",");
            String ratioStr = "";
            for (int i = 0; i < ratios.length; i++) {
                String ratio = ratios[i];
                if (TextUtils.isEmpty(ratio)) {
                    continue;
                }
                if (i == ratios.length - 1) {
                    ratioStr += (int) (Double.parseDouble(ratio) * 100) + "%";
                } else {
                    ratioStr += (int) (Double.parseDouble(ratio) * 100) + "%/";
                }
            }
            setInstalmentRatio(ratioStr);
        } else {//不分期
            setInstalmentVisibility(View.GONE);
        }
        if (innerDemandCardInfo.bp == 2) {//协商
//            setBpConsultVisibility(View.VISIBLE);
            mActivityMyBidDemandBinding.tvBpText.setText("协商处理纠纷");
            mActivityMyBidDemandBinding.ivBpIcon.setImageResource(R.mipmap.negotiation_icon);
        } else { //1 平台
//            setBpConsultVisibility(View.GONE);
            mActivityMyBidDemandBinding.tvBpText.setText("平台处理纠纷");
            mActivityMyBidDemandBinding.ivBpIcon.setImageResource(R.mipmap.platform_icon);
        }

        //回填抢单浮层中的开始时间
        mActivityMyBidDemandBinding.tvBidDemandStarttime.setText(starttimeStr);
        bidDemandStarttime = innerDemandCardInfo.starttime;
        //填写抢单浮层中的报价
        mActivityMyBidDemandBinding.etBidDemandQuote.setText((int) innerDemandCardInfo.quote + "");
        //填写抢单浮层中的分期
        if (innerDemandCardInfo.instalment == 0) {//不开启
            bidIsInstalment = true;//调用toggleInstalment方法后就变成false了
            toggleInstalment(null);
        } else {//开启分期
            bidIsInstalment = false;//调用toggleInstalment方法后就变成true了
            toggleInstalment(null);
        }
        //填写抢单浮层中的纠纷处理方式
        if (innerDemandCardInfo.bp == 1) {
            checkPlatformProcessing(null);
        } else if (innerDemandCardInfo.bp == 2) {
            checkConsultProcessing(null);
        }

        displayStatusButton();
        displayStatusProgressCycle();

        hideLoadLayer();
    }

    String dAvatarUrl;
    String dname;

    /**
     * 获取需求方方用户信息
     */
    private void getDemandUserInfo() {
        UserInfoEngine.getOtherUserInfo(new BaseProtocol.IResultExecutor<UserInfoBean>() {
            @Override
            public void execute(UserInfoBean dataBean) {
                UserInfoBean.UInfo uinfo = dataBean.data.uinfo;
                dAvatarUrl = GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + uinfo.avatar;
                BitmapKit.bindImage(mActivityMyBidDemandBinding.ivDemandUserAvatar, dAvatarUrl);
                if (uinfo.isauth == 0) {//未认证
                    setDemandUserIsAuthVisibility(View.GONE);
                } else {
                    setDemandUserIsAuthVisibility(View.VISIBLE);
                }
                dname = uinfo.name;
                setDemandUsername("需求方:" + uinfo.name);

                LogKit.v("需求方用户信息，dataBean.data.uinfo.id:" + dataBean.data.uinfo.id);
            }

            @Override
            public void executeResultError(String result) {
                LogKit.v("获取需求方用户信息失败");
            }
        }, innerDemandCardInfo.uid + "", "0");
    }

    String sAvatarUrl;
    String sname;

    /**
     * 获取服务方用户信息
     */
    private void getServiceUserInfo() {
        UserInfoEngine.getOtherUserInfo(new BaseProtocol.IResultExecutor<UserInfoBean>() {
            @Override
            public void execute(UserInfoBean dataBean) {
                UserInfoBean.UInfo uinfo = dataBean.data.uinfo;
                sAvatarUrl = GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + uinfo.avatar;
                BitmapKit.bindImage(mActivityMyBidDemandBinding.ivServiceUserAvatar, sAvatarUrl);
                if (uinfo.isauth == 0) {//未认证
                    setServiceUserIsAuthVisibility(View.GONE);
                } else {
                    setServiceUserIsAuthVisibility(View.VISIBLE);
                }
                sname = uinfo.name;
                setServiceUsername("服务方:" + uinfo.name);

                LogKit.v("服务方用户信息，dataBean.data.uinfo.id:" + dataBean.data.uinfo.id);
            }

            @Override
            public void executeResultError(String result) {
                LogKit.v("获取服务方用户信息失败");
            }
        }, innerDemandCardInfo.suid + "", "0");
        LogKit.v("getServiceUserInfo innerDemandCardInfo.suid:" + innerDemandCardInfo.suid);
    }


    private void displayStatusButton() {
        int status = innerDemandCardInfo.status;
        switch (status) {
            case 5:/*待确认*/
                setStatusButtonsVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
                LogKit.v("--------------待确认--------------------");
                break;
            case 1:/*已发布*/
            case 4:/*待选择*/
                setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE);
                break;
            case 7:/*进行中*/
                //通过instalmentlist接口获取分期比例
                DemandEngine.getDemandInstalmentList(new BaseProtocol.IResultExecutor<DemandInstalmentListBean>() {
                    @Override
                    public void execute(DemandInstalmentListBean dataBean) {
                        ArrayList<DemandInstalmentListBean.InstalmentInfo> instalmentInfoList = dataBean.data.list;
                        int totalInstalment = instalmentInfoList.size();//总的分期数
                        for (int i = 0; i < totalInstalment; i++) {
                            DemandInstalmentListBean.InstalmentInfo instalmentInfo = instalmentInfoList.get(i);
                            if (instalmentInfo.status != 2) {
                                fid = instalmentInfo.id;
                                innerDemandCardInfo.instalmentcurr = instalmentInfo.id;
                                //status  0表示未开始  1表示服务方完成  2表示需求方确认此分期完成
                                if (instalmentInfo.status == 0) {
                                    setStatusButtonsVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE);
                                    mActivityMyBidDemandBinding.tvCompleteText.setText("完成(" + fid + "/" + totalInstalment + ")");
                                } else {
                                    setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);
                                }
                                break;
                            }
                        }
                    }

                    @Override
                    public void executeResultError(String result) {
                        LogKit.v("获取需求分期信息失败:" + result);
                        ToastUtils.shortToast("获取需求分期信息失败:" + result);
                    }
                }, tid + "");


//                if (innerDemandCardInfo.instalmentcurr == 0) {
//                    fid = 1;
//                } else {
//                    fid = innerDemandCardInfo.instalmentcurr;
//                }
//                if (innerDemandCardInfo.instalmentcurrfinish == 0) {
//                    setStatusButtonsVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE);
//                } else {
//                    setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
//                }
                break;
            case 9:/*申请退款*/
                //这里要使用需求详情接口中的状态来判断，如果已经同意退款，或者申诉，按钮需要隐藏
                /*同意退款*/
                //public static final int DEMAND_STATUS_REFUNDED = 12;
                /*需求企业付款完成或者退款(彻底完成)*/
                //public static final int DEMAND_STATUS_FINISH = 13;
                /*需求出现纠纷进入申诉平台介入状态*/
                //public static final int DEMAND_STATUS_INTERVENTION = 14;
                if (demandDetailStatus == 12 || demandDetailStatus == 13 || demandDetailStatus == 14) {
                    setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);
                } else {
                    setStatusButtonsVisibility(View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
                }
                break;
            case 8:/*已完成*/
                //服务方完成了最后一期（需求方可能还没去确认完成，也可能确认了完成），当需求方确认完成以后，并且进行了评价，才显示"查看评价"按钮
                if (innerDemandCardInfo.isComment == 1) {
                    setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE);
                } else {
                    setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);
                }
                break;
            case 2:/*已取消*/
            case 3:/*被拒绝*/
            case 6:/*待支付*/
            case 10:/*已退款*/
            case 11:/*已淘汰*/
            default:
                setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);
                break;
        }
    }

    private void displayStatusProgressCycle() {
        int status = innerDemandCardInfo.status;
        switch (status) {
            case 1:/*已发布*/
            case 4:/*待选择*/
            case 5:/*待确认*/
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
            case 6://待支付
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
            case 7:/*进行中*/
            case 9:/*申请退款*/
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
            case 8:/*已完成*/
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4);
                break;
            case 2:/*已取消*/
            case 3:/*被拒绝*/
            case 10:/*已退款*/
            case 11:/*已淘汰*/

            default:
                //失效 过期 状态 四个圈全都是灰色
                setStatusProgress(R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
        }
    }

    private void setStatusButtonsVisibility(int acceptItemVisibility, int completeItemVisibility, int isAgreeRefundItemVisibility, int viewCommentItemVisibility, int viewUpdateBidDemandVisibility) {
        setAcceptItemVisibility(acceptItemVisibility);
        setCompleteItemVisibility(completeItemVisibility);
        setIsAgreeRefundItemVisibility(isAgreeRefundItemVisibility);
        setViewCommentItemVisibility(viewCommentItemVisibility);
        setUpdateBidDemandVisibility(viewUpdateBidDemandVisibility);
    }

    private void setStatusProgress(int bigStateReservationBg, int bigStateReservationTextColor, int bigStatePaymentBg, int bigStatePaymentTextColor, int bigStateServiceBg, int bigStateServiceTextColor, int bigStateCommentBg, int bigStateCommentTextColor) {
        mActivityMyBidDemandBinding.tvDemandReservationing.setBackgroundResource(bigStateReservationBg);
        mActivityMyBidDemandBinding.tvDemandReservationing.setTextColor(bigStateReservationTextColor);
        mActivityMyBidDemandBinding.tvDemandPayment.setBackgroundResource(bigStatePaymentBg);
        mActivityMyBidDemandBinding.tvDemandPayment.setTextColor(bigStatePaymentTextColor);
        mActivityMyBidDemandBinding.tvDemandServiceing.setBackgroundResource(bigStateServiceBg);
        mActivityMyBidDemandBinding.tvDemandServiceing.setTextColor(bigStateServiceTextColor);
        mActivityMyBidDemandBinding.tvDemandComment.setBackgroundResource(bigStateCommentBg);
        mActivityMyBidDemandBinding.tvDemandComment.setTextColor(bigStateCommentTextColor);
    }

    public class InnerDemandCardInfo {
        public long uid;
        public String avatar;
        public String username;
        public int isAuth;

        public long suid;

        public String title;
        public long starttime;
        public double quote;
        public int instalment;//0 or 1 表示是否开启分期 0不允许分期 1允许分期
        public int instalmentcurr;//表示当前处于第几个分期
        public int instalmentcurrfinish;//表示当期是否服务方完成 0未完成 1已经完成
        public String instalmentratio;//表示分期情况，格式为30,20,10,40 (英文逗号分隔)
        public int rectify;//是否使用过延期付款  1使用过，0未使用过
        public int bp;//1平台 2协商
        public int isComment;

        public int status;//使用我的任务中的status定义

    }

    /**
     * 修改抢单信息
     *
     * @param v
     */
    public void updateBidDemandInfo(View v) {

        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_MODIFY_CONDITION);
        setUpdateBidInfoVisibility(View.VISIBLE);
    }

    /**
     * 关闭抢单信息浮层
     *
     * @param v
     */
    public void closeBidDemandLayer(View v) {
        setUpdateBidInfoVisibility(View.GONE);
    }

    /**
     * 打开修改抢单时间
     *
     * @param v
     */
    public void openTimeLayer(View v) {
        setChooseDateTimeLayerVisibility(View.VISIBLE);
    }

    private int bidBp;

    /**
     * 抢单时 选择平台处理方式
     *
     * @param v
     */
    public void checkPlatformProcessing(View v) {
        mActivityMyBidDemandBinding.ivPlatformProcessingIcon.setImageResource(R.mipmap.pitchon_btn);
        mActivityMyBidDemandBinding.ivConsultProcessingIcon.setImageResource(R.mipmap.default_btn);
        bidBp = 1;
    }

    /**
     * 抢单时 选择写上方式
     *
     * @param v
     */
    public void checkConsultProcessing(View v) {
        mActivityMyBidDemandBinding.ivPlatformProcessingIcon.setImageResource(R.mipmap.default_btn);
        mActivityMyBidDemandBinding.ivConsultProcessingIcon.setImageResource(R.mipmap.pitchon_btn);
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
                = (RelativeLayout.LayoutParams) mActivityMyBidDemandBinding.ivBidDemandInstalmentHandle.getLayoutParams();
        if (bidIsInstalment) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            mActivityMyBidDemandBinding.ivBidDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle_weijihuo);
            //关闭分期 隐藏分期比率
            hideInstalmentRatio();
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            mActivityMyBidDemandBinding.ivBidDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle);
            //开启分期 显示已输入的分期比率
            displayInstalmentRatio();
        }
        mActivityMyBidDemandBinding.ivBidDemandInstalmentHandle.setLayoutParams(layoutParams);
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
            ratioStr = mActivityMyBidDemandBinding.etBidDemandInstalmentRatio1.getText().toString();
        } else if (instalmentCount == 2) {
            ratioStr = mActivityMyBidDemandBinding.etBidDemandInstalmentRatio2.getText().toString();
        } else if (instalmentCount == 3) {
            ratioStr = mActivityMyBidDemandBinding.etBidDemandInstalmentRatio3.getText().toString();
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
    public void updateBidInfo(View v) {
        double bidQuote;
        String bidQuoteStr = mActivityMyBidDemandBinding.etBidDemandQuote.getText().toString();
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
//            ToastUtils.shortToast("开始时间必须大于当前时间2个小时");
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
        //调用修改抢单信息的接口
        DemandEngine.updateBid(new BaseProtocol.IResultExecutor() {
            @Override
            public void execute(Object dataBean) {
                ToastUtils.shortToast("修改成功");
                setUpdateBidInfoVisibility(View.GONE);
                reloadData();
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("修改失败;" + result);
            }
        }, tid + "", bidQuote + "", bidDemandInstalmentRatioList, bidBp + "", bidDemandStarttime + "");

    }

    private void getInputInstalmentRatio() {
        bidDemandInstalmentRatioList.clear();
        String ratioStr;
        if (instalmentCount >= 1) {
            ratioStr = mActivityMyBidDemandBinding.etBidDemandInstalmentRatio1.getText().toString();
            double ratio = convertStrToRatio(ratioStr);
            if (ratio == -1) {
                ToastUtils.shortToast("请正确填写第一期分期比率");
                return;
            }
            bidDemandInstalmentRatioList.add(ratio);
        }
        if (instalmentCount >= 2) {
            ratioStr = mActivityMyBidDemandBinding.etBidDemandInstalmentRatio2.getText().toString();
            double ratio = convertStrToRatio(ratioStr);
            if (ratio == -1) {
                ToastUtils.shortToast("请正确填写第二期分期比率");
                return;
            }
            bidDemandInstalmentRatioList.add(ratio);
        }
        if (instalmentCount >= 3) {
            ratioStr = mActivityMyBidDemandBinding.etBidDemandInstalmentRatio3.getText().toString();
            double ratio = convertStrToRatio(ratioStr);
            if (ratio == -1) {
                ToastUtils.shortToast("请正确填写第三期分期比率");
                return;
            }
            bidDemandInstalmentRatioList.add(ratio);
        }
        if (instalmentCount >= 4) {
            ratioStr = mActivityMyBidDemandBinding.etBidDemandInstalmentRatio4.getText().toString();
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
     * 取消选择时间
     *
     * @param v
     */
    public void cancelChooseTime(View v) {
        setChooseDateTimeLayerVisibility(View.GONE);
    }

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
        mActivityMyBidDemandBinding.tvBidDemandStarttime.setText(bidStarttimeStr);
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

    public void reloadData() {
        reloadData(true);
    }

    public void reloadData(boolean isNeedDelay) {
        if (isNeedDelay) {//延迟两秒重新加载
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDataFromServer();
                }
            }, 2000);
        } else {//不延迟，直接重新加载
            getDataFromServer();
        }
    }

    /**
     * 点击任务标题，跳转到详情页
     *
     * @param v
     */
    public void gotoDemandDetail(View v) {
        Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
        intentDemandDetailActivity.putExtra("demandId", tid);
        mActivity.startActivity(intentDemandDetailActivity);
    }

    private int acceptItemVisibility = View.GONE;
    private int completeItemVisibility = View.GONE;
    private int isAgreeRefundItemVisibility = View.GONE;
    private int viewCommentItemVisibility = View.GONE;
    private int updateBidDemandVisibility = View.GONE;

    private String demandTitle;
    private String starttime; //开始时间:2016年9月18日 8:00
    private String quote;   //300元
    private int instalmentVisibility;
    private String instalmentRatio;//30%/40%/40%/50%
    private int bpConsultVisibility;

    private int serviceUserIsAuthVisibility = View.GONE;
    private int demandUserIsAuthVisibility = View.GONE;
    private String demandUsername;
    private String serviceUsername;

    private int loadLayerVisibility = View.GONE;
    private int updateBidInfoVisibility = View.GONE;
    private int instalmentRatioVisibility;
    private int addInstalmentIconVisibility;

    private int chooseDateTimeLayerVisibility = View.GONE;
    private int updateInstalmentLine1Visibility = View.GONE;
    private int updateInstalmentLine2Visibility = View.GONE;
    private int updateInstalmentLine3Visibility = View.GONE;
    private int updateInstalmentLine4Visibility = View.GONE;

    private String taskUts;

    @Bindable
    public String getTaskUts() {
        return taskUts;
    }

    public void setTaskUts(String taskUts) {
        this.taskUts = taskUts;
        notifyPropertyChanged(BR.taskUts);
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
    public int getUpdateBidInfoVisibility() {
        return updateBidInfoVisibility;
    }

    public void setUpdateBidInfoVisibility(int updateBidInfoVisibility) {
        this.updateBidInfoVisibility = updateBidInfoVisibility;
        notifyPropertyChanged(BR.updateBidInfoVisibility);
    }

    @Bindable
    public int getUpdateBidDemandVisibility() {
        return updateBidDemandVisibility;
    }

    public void setUpdateBidDemandVisibility(int updateBidDemandVisibility) {
        this.updateBidDemandVisibility = updateBidDemandVisibility;
        notifyPropertyChanged(BR.updateBidDemandVisibility);
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
    public String getServiceUsername() {
        return serviceUsername;
    }

    public void setServiceUsername(String serviceUsername) {
        this.serviceUsername = serviceUsername;
        notifyPropertyChanged(BR.serviceUsername);
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
    public int getServiceUserIsAuthVisibility() {
        return serviceUserIsAuthVisibility;
    }

    public void setServiceUserIsAuthVisibility(int serviceUserIsAuthVisibility) {
        this.serviceUserIsAuthVisibility = serviceUserIsAuthVisibility;
        notifyPropertyChanged(BR.serviceUserIsAuthVisibility);
    }

    @Bindable
    public int getDemandUserIsAuthVisibility() {
        return demandUserIsAuthVisibility;
    }

    public void setDemandUserIsAuthVisibility(int demandUserIsAuthVisibility) {
        this.demandUserIsAuthVisibility = demandUserIsAuthVisibility;
        notifyPropertyChanged(BR.demandUserIsAuthVisibility);
    }

    @Bindable
    public int getBpConsultVisibility() {
        return bpConsultVisibility;
    }

    public void setBpConsultVisibility(int bpConsultVisibility) {
        this.bpConsultVisibility = bpConsultVisibility;
        notifyPropertyChanged(BR.bpConsultVisibility);
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
    public int getInstalmentVisibility() {
        return instalmentVisibility;
    }

    public void setInstalmentVisibility(int instalmentVisibility) {
        this.instalmentVisibility = instalmentVisibility;
        notifyPropertyChanged(BR.instalmentVisibility);
    }

    @Bindable
    public String getInstalmentRatio() {
        return instalmentRatio;
    }

    public void setInstalmentRatio(String instalmentRatio) {
        this.instalmentRatio = instalmentRatio;
        notifyPropertyChanged(BR.instalmentRatio);
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
    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
        notifyPropertyChanged(BR.starttime);
    }

    @Bindable
    public int getAcceptItemVisibility() {
        return acceptItemVisibility;
    }

    public void setAcceptItemVisibility(int acceptItemVisibility) {
        this.acceptItemVisibility = acceptItemVisibility;
        notifyPropertyChanged(BR.acceptItemVisibility);
    }

    @Bindable
    public int getCompleteItemVisibility() {
        return completeItemVisibility;
    }

    public void setCompleteItemVisibility(int completeItemVisibility) {
        this.completeItemVisibility = completeItemVisibility;
        notifyPropertyChanged(BR.completeItemVisibility);
    }

    @Bindable
    public int getIsAgreeRefundItemVisibility() {
        return isAgreeRefundItemVisibility;
    }

    public void setIsAgreeRefundItemVisibility(int isAgreeRefundItemVisibility) {
        this.isAgreeRefundItemVisibility = isAgreeRefundItemVisibility;
        notifyPropertyChanged(BR.isAgreeRefundItemVisibility);
    }

    @Bindable
    public int getViewCommentItemVisibility() {
        return viewCommentItemVisibility;
    }

    public void setViewCommentItemVisibility(int viewCommentItemVisibility) {
        this.viewCommentItemVisibility = viewCommentItemVisibility;
        notifyPropertyChanged(BR.viewCommentItemVisibility);
    }
}
