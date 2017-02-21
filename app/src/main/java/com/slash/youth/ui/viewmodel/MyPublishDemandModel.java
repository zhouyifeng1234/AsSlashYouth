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

import com.google.gson.Gson;
import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyPublishDemandBinding;
import com.slash.youth.databinding.ItemDemandFlowLogBinding;
import com.slash.youth.domain.CommonLogList;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.DelayPayBean;
import com.slash.youth.domain.DemandDetailBean;
import com.slash.youth.domain.DemandInstalmentListBean;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.domain.MyTaskItemBean;
import com.slash.youth.domain.UserInfoBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.CommentActivity;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.MyPublishDemandActivity;
import com.slash.youth.ui.activity.PaymentActivity;
import com.slash.youth.ui.activity.RefundActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.view.RefreshScrollView;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/27.
 */
public class MyPublishDemandModel extends BaseObservable {

    ActivityMyPublishDemandBinding mActivityMyPublishDemandBinding;
    Activity mActivity;
    long tid;//需求ID
    int type;//取值范围只能是: 1需求 2服务
    int roleid;//表示是'我抢的单子' 还是 '我发布的任务' 1发布者 2抢单者

    public MyPublishDemandModel(ActivityMyPublishDemandBinding activityMyPublishDemandBinding, Activity activity) {
        this.mActivityMyPublishDemandBinding = activityMyPublishDemandBinding;
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

    }

    private void initListener() {
        mActivityMyPublishDemandBinding.scRefresh.setRefreshTask(new RefreshScrollView.IRefreshTask() {
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
                //我发的需求，我就是需求方，所以这里不需要传uid
                break;
            case R.id.ll_service_userinfo:
                //获取服务方uid
                intentUserInfoActivity.putExtra("Uid", innerDemandCardInfo.suid);
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
        intentChatActivity.putExtra("targetId", innerDemandCardInfo.suid + "");//对方的uid
        Bundle taskInfoBundle = new Bundle();
        taskInfoBundle.putLong("tid", tid);
        taskInfoBundle.putInt("type", 1);
        taskInfoBundle.putString("title", getDemandTitle());
        intentChatActivity.putExtra("taskInfo", taskInfoBundle);
        mActivity.startActivity(intentChatActivity);
    }

    //需求方评论
    public void comment(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_EVALUATION);

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

//        commentInfo.putDouble("quote", innerDemandCardInfo.quote);
//        commentInfo.putString("quote", quote);
        intentCommentActivity.putExtras(commentInfo);

        mActivity.startActivityForResult(intentCommentActivity, MyPublishDemandActivity.activityRequestCode);
    }

    //申请退款
    public void refund(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_APPLY_REFUND);

        Intent intentRefundActivity = new Intent(CommonUtils.getContext(), RefundActivity.class);
        intentRefundActivity.putExtra("tid", tid);
        intentRefundActivity.putExtra("type", type);
        mActivity.startActivityForResult(intentRefundActivity, MyPublishDemandActivity.activityRequestCode);
    }

    //延期支付,出现他弹框
    public void rectifyPayment(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_DELAY_PAY);
        setRectifyLayerVisibility(View.VISIBLE);
    }

    //取消延期支付，关闭弹框
    public void cancelRectifyPayment(View v) {
        setRectifyLayerVisibility(View.GONE);
    }

    //确认延期支付
    public void okRectifyPayment(View v) {
        DemandEngine.delayPay(new BaseProtocol.IResultExecutor<DelayPayBean>() {
            @Override
            public void execute(DelayPayBean dataBean) {
                //延期支付成功
                ToastUtils.shortToast("延期支付成功");
                setRectifyLayerVisibility(View.GONE);
                reloadData();
            }

            @Override
            public void executeResultError(String result) {
                //延期支付失败
                ToastUtils.shortToast("延期支付失败");

            }
        }, tid + "", innerDemandCardInfo.instalmentcurr + "");
    }

    //延期支付右上角按钮，关闭弹框
    public void closeRectifyLayer(View v) {
        setRectifyLayerVisibility(View.GONE);
    }

    //需求方确认完成
    public void confirmFinish(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_CONFIRM_MISSON);
        int fid = innerDemandCardInfo.instalmentcurr;
        DemandEngine.demandPartyConfirmComplete(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                //确认完成成功
                ToastUtils.shortToast("确认完成成功");
                reloadData();
            }

            @Override
            public void executeResultError(String result) {
                //确认完成失败
                Gson gson = new Gson();
                CommonResultBean commonResultBean = gson.fromJson(result, CommonResultBean.class);
                if (commonResultBean.data.status == 5 || commonResultBean.data.status == 6) {
                    ToastUtils.shortToast("请先设置交易密码");
                } else {
                    ToastUtils.shortToast("确认完成失败:" + result);
                }
            }
        }, tid + "", fid + "");
    }

    //打开支付界面
    public void openPaymentActivity(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_PAY);

        Intent intentPaymentActivity = new Intent(CommonUtils.getContext(), PaymentActivity.class);

        Bundle payInfo = new Bundle();
        payInfo.putLong("tid", tid);
        payInfo.putDouble("quote", innerDemandCardInfo.quote);
        payInfo.putInt("type", 1);
        payInfo.putString("title", innerDemandCardInfo.title);
        intentPaymentActivity.putExtras(payInfo);

        mActivity.startActivityForResult(intentPaymentActivity, MyPublishDemandActivity.activityRequestCode);

        //暂时先用余额支付测试
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
        mActivityMyPublishDemandBinding.llDemandFlowLogs.removeAllViews();
        for (int i = listLogInfo.size() - 1; i >= 0; i--) {
            CommonLogList.CommonLogInfo logInfo = listLogInfo.get(i);
            View itemLogInfo = inflateItemLogInfo(logInfo);
            mActivityMyPublishDemandBinding.llDemandFlowLogs.addView(itemLogInfo);
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
                //这个字段使用分期列表中的
//                innerDemandCardInfo.instalment = taskinfo.instalment;//0 or 1 表示是否开启分期 0不允许分期 1允许分期
                innerDemandCardInfo.instalmentcurr = taskinfo.instalmentcurr;//表示当前处于第几个分期
                innerDemandCardInfo.instalmentcurrfinish = taskinfo.instalmentcurrfinish;//表示当期是否服务方完成 0未完成 1已经完成
                //这个字段也是用分期列表中的
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

    private void getDemandDetail() {
        DemandEngine.getDemandDetail(new BaseProtocol.IResultExecutor<DemandDetailBean>() {
            @Override
            public void execute(DemandDetailBean dataBean) {
                innerDemandCardInfo.uid = dataBean.data.demand.uid;//使用需求详情接口中的uid
                innerDemandCardInfo.suid = dataBean.data.demand.suid;
                innerDemandCardInfo.bp = dataBean.data.demand.bp;
                innerDemandCardInfo.isComment = dataBean.data.demand.iscomment;

                getInstalmentList();
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "");
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
        setStarttime(sdfStarttime.format(innerDemandCardInfo.starttime));
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
            mActivityMyPublishDemandBinding.tvBpText.setText("协商处理纠纷");
            mActivityMyPublishDemandBinding.ivBpIcon.setImageResource(R.mipmap.negotiation_icon);
        } else { //1 平台
//            setBpConsultVisibility(View.GONE);
            mActivityMyPublishDemandBinding.tvBpText.setText("平台处理纠纷");
            mActivityMyPublishDemandBinding.ivBpIcon.setImageResource(R.mipmap.platform_icon);
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
                BitmapKit.bindImage(mActivityMyPublishDemandBinding.ivDemandUserAvatar, dAvatarUrl);
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
                BitmapKit.bindImage(mActivityMyPublishDemandBinding.ivServiceUserAvatar, sAvatarUrl);
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
    }

    private void displayStatusButton() {
        int status = innerDemandCardInfo.status;
        switch (status) {
            case 1:
            case 4:
            case 5:
                setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
                break;
            case 6://待支付
                setStatusButtonsVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE);
                break;
            case 7:
                //通过instalmentlist接口获取分期比例
                DemandEngine.getDemandInstalmentList(new BaseProtocol.IResultExecutor<DemandInstalmentListBean>() {
                    @Override
                    public void execute(DemandInstalmentListBean dataBean) {
                        ArrayList<DemandInstalmentListBean.InstalmentInfo> instalmentInfoList = dataBean.data.list;
                        final int totalInstalment = instalmentInfoList.size();//总的分期数
                        for (int i = 0; i < totalInstalment; i++) {
                            DemandInstalmentListBean.InstalmentInfo instalmentInfo = instalmentInfoList.get(i);
                            if (instalmentInfo.status != 2) {
                                innerDemandCardInfo.instalmentcurr = instalmentInfo.id;
                                final int instalmentcurr = instalmentInfo.id;

                                if (instalmentcurr < totalInstalment) {//不是最后一期
                                    if (instalmentInfo.status == 1) {
                                        setStatusButtonsVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE);
                                        mActivityMyPublishDemandBinding.tvConfirmText.setText("确认(" + instalmentcurr + "/" + totalInstalment + ")");
                                    } else {
                                        setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
                                    }
                                } else {//最后一期
                                    if (instalmentInfo.status == 1) {
                                        DemandEngine.getRectifyStatus(new BaseProtocol.IResultExecutor<CommonResultBean>() {
                                            @Override
                                            public void execute(CommonResultBean dataBean) {
                                                if (dataBean.data.status == 1) {
                                                    setStatusButtonsVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE);
                                                } else {
                                                    setStatusButtonsVisibility(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
                                                }
                                                mActivityMyPublishDemandBinding.tvConfirmText.setText("确认(" + instalmentcurr + "/" + totalInstalment + ")");
                                            }

                                            @Override
                                            public void executeResultError(String result) {
                                                LogKit.v("获取延期支付状态失败:" + result);
                                                ToastUtils.shortToast("获取延期支付状态失败:" + result);
                                            }
                                        }, tid + "");


//                                        if (innerDemandCardInfo.rectify == 1) {
//                                            setStatusButtonsVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE);
//                                        } else {
//                                            setStatusButtonsVisibility(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
//                                        }
                                    } else {
                                        setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
                                    }
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


//                int totalInstalmentCount = innerDemandCardInfo.instalmentratio.split(",").length;//总的分期数
//                int instalmentcurr = innerDemandCardInfo.instalmentcurr;
//                if (instalmentcurr == 0) {
//                    instalmentcurr = 1;
//                }
//                if (instalmentcurr < totalInstalmentCount) {//不是最后一期
//                    if (innerDemandCardInfo.instalmentcurrfinish == 1) {
//                        setStatusButtonsVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE);
//                    } else {
//                        setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
//                    }
//                } else {//最后一期
//                    if (innerDemandCardInfo.instalmentcurrfinish == 1) {
//                        if (innerDemandCardInfo.rectify == 1) {
//                            setStatusButtonsVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE);
//                        } else {
//                            setStatusButtonsVisibility(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
//                        }
//                    } else {
//                        setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
//                    }
//                }
                break;
            case 8:
                if (innerDemandCardInfo.isComment == 0) {
                    setStatusButtonsVisibility(View.GONE, View.GONE, View.VISIBLE, View.GONE);//显示去评价
                    mActivityMyPublishDemandBinding.tvBtnComment.setText("去评价");
                } else {
                    setStatusButtonsVisibility(View.GONE, View.GONE, View.VISIBLE, View.GONE);//显示查看评价
                    mActivityMyPublishDemandBinding.tvBtnComment.setText("查看评价");
                }
                break;
            case 9:
                setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
                break;
            default:
                setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
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


    private void setStatusButtonsVisibility(int paymentVisibility, int confirmFinishVisibility, int commentVisibility, int rectifyVisibility) {
        setPaymentVisibility(paymentVisibility);
        setConfirmFinishVisibility(confirmFinishVisibility);
        setCommentVisibility(commentVisibility);
        setRectifyVisibility(rectifyVisibility);
    }

    private void setStatusProgress(int bigStateReservationBg, int bigStateReservationTextColor, int bigStatePaymentBg, int bigStatePaymentTextColor, int bigStateServiceBg, int bigStateServiceTextColor, int bigStateCommentBg, int bigStateCommentTextColor) {
        mActivityMyPublishDemandBinding.tvDemandReservationing.setBackgroundResource(bigStateReservationBg);
        mActivityMyPublishDemandBinding.tvDemandReservationing.setTextColor(bigStateReservationTextColor);
        mActivityMyPublishDemandBinding.tvDemandPayment.setBackgroundResource(bigStatePaymentBg);
        mActivityMyPublishDemandBinding.tvDemandPayment.setTextColor(bigStatePaymentTextColor);
        mActivityMyPublishDemandBinding.tvDemandServiceing.setBackgroundResource(bigStateServiceBg);
        mActivityMyPublishDemandBinding.tvDemandServiceing.setTextColor(bigStateServiceTextColor);
        mActivityMyPublishDemandBinding.tvDemandComment.setBackgroundResource(bigStateCommentBg);
        mActivityMyPublishDemandBinding.tvDemandComment.setTextColor(bigStateCommentTextColor);
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

    private int paymentVisibility = View.GONE;
    private int confirmFinishVisibility = View.GONE;
    private int commentVisibility = View.GONE;
    private int rectifyVisibility = View.GONE;
    private int rectifyLayerVisibility = View.GONE;

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
    public int getCommentVisibility() {
        return commentVisibility;
    }

    public void setCommentVisibility(int commentVisibility) {
        this.commentVisibility = commentVisibility;
        notifyPropertyChanged(BR.commentVisibility);
    }

    @Bindable
    public int getConfirmFinishVisibility() {
        return confirmFinishVisibility;
    }

    public void setConfirmFinishVisibility(int confirmFinishVisibility) {
        this.confirmFinishVisibility = confirmFinishVisibility;
        notifyPropertyChanged(BR.confirmFinishVisibility);
    }

    @Bindable
    public int getPaymentVisibility() {
        return paymentVisibility;
    }

    public void setPaymentVisibility(int paymentVisibility) {
        this.paymentVisibility = paymentVisibility;
        notifyPropertyChanged(BR.paymentVisibility);
    }

    @Bindable
    public int getRectifyVisibility() {
        return rectifyVisibility;
    }

    public void setRectifyVisibility(int rectifyVisibility) {
        this.rectifyVisibility = rectifyVisibility;
        notifyPropertyChanged(BR.rectifyVisibility);
    }

    @Bindable
    public int getRectifyLayerVisibility() {
        return rectifyLayerVisibility;
    }


    public void setRectifyLayerVisibility(int rectifyLayerVisibility) {
        this.rectifyLayerVisibility = rectifyLayerVisibility;
        notifyPropertyChanged(BR.rectifyLayerVisibility);
    }
}
