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
import com.slash.youth.databinding.ActivityMyBidServiceBinding;
import com.slash.youth.databinding.ItemServiceFlowLogBinding;
import com.slash.youth.domain.CommonLogList;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.domain.MyTaskItemBean;
import com.slash.youth.domain.ServiceDetailBean;
import com.slash.youth.domain.ServiceInstalmentListBean;
import com.slash.youth.domain.ServiceOrderInfoBean;
import com.slash.youth.domain.UserInfoBean;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.engine.ServiceEngine;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.CommentActivity;
import com.slash.youth.ui.activity.MyBidServiceActivity;
import com.slash.youth.ui.activity.PaymentActivity;
import com.slash.youth.ui.activity.RefundActivity;
import com.slash.youth.ui.activity.ServiceDetailActivity;
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
 * Created by zhouyifeng on 2016/12/6.
 */
public class MyBidServiceModel extends BaseObservable {

    ActivityMyBidServiceBinding mActivityMyBidServiceBinding;
    Activity mActivity;

    private long tid;
    private long soid;
    private long suid;
    private int fid;//当前是第几期
    private double orderQuote = -1;//必须是服务订单信息接口返回的报价才是准确的
    private int quoteunit = -1;
    private long duid;//服务订单中的需求方ID
    String[] optionalPriceUnit = new String[]{"次", "个", "幅", "份", "单", "小时", "分钟", "天", "其他"};
    private long serviceId;

    public MyBidServiceModel(ActivityMyBidServiceBinding activityMyBidServiceBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityMyBidServiceBinding = activityMyBidServiceBinding;
        displayLoadLayer();
        initData();
        initView();
        initListener();
    }

    MyTaskBean myTaskBean;

    private void initData() {
//        tid = mActivity.getIntent().getLongExtra("tid", -1);
        myTaskBean = (MyTaskBean) mActivity.getIntent().getSerializableExtra("myTaskBean");
        tid = myTaskBean.tid;//tid就是soid
        soid = tid;//tid（任务id）就是soid(服务订单id)
        fid = myTaskBean.instalmentcurr;//通过调试接口发现，这个字段当type=2为服务的时候，好像不准，一直都是0

        getDataFromServer();
    }

    private int loadDataTimes = 0;//getTaskItemData、getServiceDetailFromServer、getServiceOrderInfoData、getDemandUserInfo、getServiceUserInfo五次都加载完毕，则数据加载完毕

    private void getDataFromServer() {
        getTaskItemData();
        getServiceDetailFromServer();//通过tid获取服务详情信息
        getServiceOrderInfoData();//根据soid(即tid)获取服务订单状态信息
        getInstalmentList();//通过分期信息接口获取分期，我的任务中的分期信息不可靠
        getServiceFlowLogData();//获取服务流程的日志
    }

    private void initView() {

    }

    private void initListener() {
        mActivityMyBidServiceBinding.scRefresh.setRefreshTask(new RefreshScrollView.IRefreshTask() {
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
                //我抢的服务，我就是需求方，所以这里不需要uid
                break;
            case R.id.ll_service_userinfo:
                //获取服务方uid
                intentUserInfoActivity.putExtra("Uid", suid);
                break;
        }
        mActivity.startActivity(intentUserInfoActivity);
    }

    /**
     * 打开聊天界面
     *
     * @param v
     */
    public void havaAChat(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_CHAT);
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_MISSONDETAIL_CLICK_MISSON_CHAT);

        Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
        intentChatActivity.putExtra("targetId", suid + "");//对方的uid
        Bundle taskInfoBundle = new Bundle();
        taskInfoBundle.putLong("tid", tid);
        taskInfoBundle.putInt("type", 2);
        taskInfoBundle.putString("title", getServiceTitle());
        intentChatActivity.putExtra("taskInfo", taskInfoBundle);
        mActivity.startActivity(intentChatActivity);
    }

    /**
     * 评价按钮操作
     *
     * @param v
     */
    public void comment(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_EVALUATION);

        Intent intentCommentActivity = new Intent(CommonUtils.getContext(), CommentActivity.class);

        Bundle commentInfo = new Bundle();
        commentInfo.putLong("tid", tid);
        commentInfo.putInt("type", 2);
        commentInfo.putLong("suid", suid);
        commentInfo.putLong("duid", duid);
        commentInfo.putString("dAvatarUrl", dAvatarUrl);
        commentInfo.putString("sAvatarUrl", sAvatarUrl);
        commentInfo.putString("dname", dname);
        commentInfo.putString("sname", sname);

//        commentInfo.putDouble("quote", Double.parseDouble(quote));
//        commentInfo.putString("quote", quote);
        intentCommentActivity.putExtras(commentInfo);

        mActivity.startActivityForResult(intentCommentActivity, MyBidServiceActivity.activityRequestCode);
    }

    /**
     * 申请退款按钮
     *
     * @param v
     */
    public void refund(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_APPLY_REFUND);

        Intent intentRefundActivity = new Intent(CommonUtils.getContext(), RefundActivity.class);

        intentRefundActivity.putExtra("tid", tid);
        intentRefundActivity.putExtra("type", 2);//1需求 2服务

        mActivity.startActivityForResult(intentRefundActivity, MyBidServiceActivity.activityRequestCode);
    }

    /**
     * 延期支付按钮
     *
     * @param v
     */
    public void rectifyPayment(View v) {

        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_DELAY_PAY);
        setRectifyLayerVisibility(View.VISIBLE);
    }

    /**
     * 确认完成按钮
     *
     * @param v
     */
    public void confirmFinish(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_CONFIRM_MISSON);
//        LogKit.v("soid:" + soid + "  fid:" + myTaskBean.instalmentcurr + "");
        ServiceEngine.confirmComplete(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("确认完成成功");
                reloadData();
            }

            @Override
            public void executeResultError(String result) {
                Gson gson = new Gson();
                CommonResultBean commonResultBean = gson.fromJson(result, CommonResultBean.class);
                if (commonResultBean.data.status == 5 || commonResultBean.data.status == 6) {
                    ToastUtils.shortToast("请先设置交易密码");
                } else {
                    ToastUtils.shortToast("确认完成失败:" + result);
                }
            }
        }, soid + "", fid + "");
    }

    /**
     * 支付按钮，打开支付界面
     *
     * @param v
     */
    public void openPaymentActivity(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON_PAY);
//        Intent intentServicePaymentActivity = new Intent(CommonUtils.getContext(), ServicePaymentActivity.class);
//
//        intentServicePaymentActivity.putExtra("soid", soid);
//        intentServicePaymentActivity.putExtra("amount", myTaskBean.quote);
//
//        mActivity.startActivity(intentServicePaymentActivity);

        Intent intentPaymentActivity = new Intent(CommonUtils.getContext(), PaymentActivity.class);

        Bundle payInfo = new Bundle();
        payInfo.putLong("tid", tid);
        payInfo.putDouble("quote", orderQuote);
        payInfo.putInt("type", 2);//1需求 2服务
        payInfo.putString("title", getServiceTitle());
        intentPaymentActivity.putExtras(payInfo);

        mActivity.startActivityForResult(intentPaymentActivity, MyBidServiceActivity.activityRequestCode);
    }

    /**
     * 右上角 关闭延期支付浮层
     *
     * @param v
     */
    public void closeRectifyLayer(View v) {
        setRectifyLayerVisibility(View.GONE);
    }

    /**
     * 取消延期支付
     *
     * @param v
     */
    public void cancelRectifyPayment(View v) {
        setRectifyLayerVisibility(View.GONE);
    }

    /**
     * 确定延期支付
     *
     * @param v
     */
    public void okRectifyPayment(View v) {
        ServiceEngine.delayPay(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("延期支付成功");
                setRectifyLayerVisibility(View.GONE);
                reloadData();
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("延期支付失败:" + result);
            }
        }, soid + "", fid + "");
    }

    /**
     * 获取对应的单个任务条目信息（从任务列表穿过的myTaskBean可能不是最新的数据）
     */
    private void getTaskItemData() {
        MyTaskEngine.getMyTaskItem(new BaseProtocol.IResultExecutor<MyTaskItemBean>() {
            @Override
            public void execute(MyTaskItemBean dataBean) {
                myTaskBean = dataBean.data.taskinfo;
                tid = myTaskBean.tid;//tid就是soid
                soid = tid;//tid（任务id）就是soid(服务订单id)
                fid = myTaskBean.instalmentcurr;//通过调试接口发现，这个字段当type=2为服务的时候，好像不准，一直都是0

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd HH:mm");
                String utsStr = sdf.format(myTaskBean.uts);
                setTaskUts(utsStr);

                loadDataTimes++;
                if (loadDataTimes >= 5) {
                    hideLoadLayer();
                }
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取服务订单条目信息失败:" + result);

                loadDataTimes++;
                if (loadDataTimes >= 5) {
                    hideLoadLayer();
                }
            }
        }, tid + "", "2", "2");
    }


    /**
     * 通过tid获取服务详情信息
     */
    private void getServiceDetailFromServer() {
        MyTaskEngine.getServiceDetailByTid(new BaseProtocol.IResultExecutor<ServiceDetailBean>() {
            @Override
            public void execute(ServiceDetailBean dataBean) {
                ServiceDetailBean.Service service = dataBean.data.service;
                serviceId = service.id;
                //服务标题，布局文件中有两个地方需要设置
                setServiceTitle(service.title + "订单");
                //闲置时间
                if (!isSetOrderTime) {
                    int timetype = service.timetype;
                    if (timetype == 0) {
                        SimpleDateFormat sdfIdleTime = new SimpleDateFormat("MM月dd日 HH:mm");
                        String starttimeStr = sdfIdleTime.format(service.starttime);
                        String endtimeStr = sdfIdleTime.format(service.endtime);
//                        setIdleTime("闲置时间:" + starttimeStr + "-" + endtimeStr);
                        setIdleTime(starttimeStr + "-" + endtimeStr);
                    } else if (timetype == 1) {
//                        setIdleTime("闲置时间:下班后");
                        setIdleTime("下班后");
                    } else if (timetype == 2) {
//                        setIdleTime("闲置时间:周末");
                        setIdleTime("周末");
                    } else if (timetype == 3) {
//                        setIdleTime("闲置时间:下班后及周末");
                        setIdleTime("下班后及周末");
                    } else if (timetype == 4) {
//                        setIdleTime("闲置时间:随时");
                        setIdleTime("随时");
                    }
                }
                //报价 这里不能使用服务详情接口返回的报价
                quoteunit = service.quoteunit;
//                CommonUtils.getHandler().post(new Runnable() {
//                    @Override
//                    public void run() {
                if (orderQuote != -1) {
                    if (quoteunit == 9) {
                        setQuote((int) orderQuote + "元");
                    } else if (quoteunit > 0 && quoteunit < 9) {
                        setQuote((int) orderQuote + "元/" + optionalPriceUnit[quoteunit - 1]);
                    }
                }
//                    }
//                });
                if (!isGetInstalmentList) {
                    //分期
                    //这里不能用service详情的instalment，要用任务列表item的instalment
                    //但是 目前任务列表item中的分期信息（分期比例）也不对，"instalmentcurr": 0, "instalmentcurrfinish": 0, "instalmentratio": "",
                    if (myTaskBean.instalment == 1) {//开启分期
                        setInstalmentVisibility(View.VISIBLE);
                        String instalmentRatioStr = "";
                        String[] ratios = myTaskBean.instalmentratio.split(",");
                        for (int i = 0; i < ratios.length; i++) {
                            String ratio = ratios[i];
                            if (TextUtils.isEmpty(ratio)) {
                                continue;
                            }
                            if (i < ratios.length - 1) {
                                instalmentRatioStr += ratio + "%/";
                            } else {
                                instalmentRatioStr += ratio + "%";
                            }
                        }
                        setInstalmentRatio(instalmentRatioStr);
                    } else {//未开启分期
                        setInstalmentVisibility(View.INVISIBLE);
                    }
                }

                loadDataTimes++;
                if (loadDataTimes >= 5) {
                    hideLoadLayer();
                }
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取服务详情信息失败:" + result);

                loadDataTimes++;
                if (loadDataTimes >= 5) {
                    hideLoadLayer();
                }
            }
        }, tid + "", "2");
    }

    boolean isSetOrderTime = false;

    /**
     * 根据soid(即tid)获取服务订单状态信息
     */
    private void getServiceOrderInfoData() {
        //这个接口好像不能使用，可以使用“v1/api/service/orderinfo”接口获取订单信息，里面有status
//        ServiceEngine.getServiceOrderStatus(new BaseProtocol.IResultExecutor<ServiceOrderStatusBean>() {
//            @Override
//            public void execute(ServiceOrderStatusBean dataBean) {
//                int status = dataBean.data.service.status;
//
//            }
//
//            @Override
//            public void executeResultError(String result) {
//
//            }
//        }, soid + "");

        ServiceEngine.getServiceOrderInfo(new BaseProtocol.IResultExecutor<ServiceOrderInfoBean>() {
            @Override
            public void execute(ServiceOrderInfoBean dataBean) {
                orderQuote = dataBean.data.order.quote;
//                CommonUtils.getHandler().post(new Runnable() {
//                    @Override
//                    public void run() {
                if (quoteunit != -1) {
                    if (quoteunit == 9) {
                        setQuote((int) orderQuote + "元");
                    } else if (quoteunit > 0 && quoteunit < 9) {
                        setQuote((int) orderQuote + "元/" + optionalPriceUnit[quoteunit - 1]);
                    }
                }
//                    }
//                });
                //纠纷处理方式（似乎协商处理就显示）
                if (dataBean.data.order.bp == 2) {//协商
                    setBpConsultVisibility(View.VISIBLE);
                    mActivityMyBidServiceBinding.tvBpText.setText("协商处理纠纷");
                    mActivityMyBidServiceBinding.ivBpIcon.setImageResource(R.mipmap.negotiation_icon);
                } else if (dataBean.data.order.bp == 1) {//平台
//                    setBpConsultVisibility(View.INVISIBLE);
                    setBpConsultVisibility(View.VISIBLE);
                    mActivityMyBidServiceBinding.tvBpText.setText("平台处理纠纷");
                    mActivityMyBidServiceBinding.ivBpIcon.setImageResource(R.mipmap.platform_icon);
                }
                //显示订单接口的开始时间和结束时间
                if (dataBean.data.order.starttime != 0 && dataBean.data.order.endtime != 0) {
                    isSetOrderTime = true;
                    SimpleDateFormat sdfIdleTime = new SimpleDateFormat("MM月dd日 HH:mm");
                    String starttimeStr = sdfIdleTime.format(dataBean.data.order.starttime);
                    String endtimeStr = sdfIdleTime.format(dataBean.data.order.endtime);
//                    setIdleTime("闲置时间:" + starttimeStr + "-" + endtimeStr);
                    setIdleTime(starttimeStr + "-" + endtimeStr);
                }

                int status = dataBean.data.order.status;
                displayStatusCycle(status);
                displayStatusButton(dataBean);//显示对应不同状态的操作按钮
                suid = dataBean.data.order.suid;
                duid = dataBean.data.order.uid;
                getDemandUserInfo();
                getServiceUserInfo();

                loadDataTimes++;
                if (loadDataTimes >= 5) {
                    hideLoadLayer();
                }
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取服务订单信息失败:" + result);

                loadDataTimes++;
                if (loadDataTimes >= 5) {
                    hideLoadLayer();
                }
            }
        }, soid + "");
    }

    boolean isGetInstalmentList = false;//是否已经从分期列表接口获取分期信息

    private void getInstalmentList() {
        ServiceEngine.getServiceInstalmentList(new BaseProtocol.IResultExecutor<ServiceInstalmentListBean>() {
            @Override
            public void execute(ServiceInstalmentListBean dataBean) {
                instalmentInfoList = dataBean.data.list;
                int totalInstalment = instalmentInfoList.size();//总的分期数
                if (totalInstalment <= 1) {
                    //不分期
                    setInstalmentVisibility(View.INVISIBLE);
                } else {
                    //分期
                    setInstalmentVisibility(View.VISIBLE);
                    String instalmentRatioStr = "";
                    for (int i = 0; i < instalmentInfoList.size(); i++) {
                        String ratio = (int) (instalmentInfoList.get(i).percent * 100) + "";
                        if (TextUtils.isEmpty(ratio)) {
                            continue;
                        }
                        if (i < instalmentInfoList.size() - 1) {
                            instalmentRatioStr += ratio + "%/";
                        } else {
                            instalmentRatioStr += ratio + "%";
                        }
                    }
                    setInstalmentRatio(instalmentRatioStr);
                }
                isGetInstalmentList = true;
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取服务订单的分期信息失败");
            }
        }, soid + "");
    }

    //    ArrayList<ServiceFlowLogList.LogInfo> logInfoList;
    ArrayList<CommonLogList.CommonLogInfo> logInfoList = new ArrayList<CommonLogList.CommonLogInfo>();

    /**
     * 获取服务流程的日志
     */
    private void getServiceFlowLogData() {
//        ServiceEngine.getServiceFlowLog(new BaseProtocol.IResultExecutor<ServiceFlowLogList>() {
//            @Override
//            public void execute(ServiceFlowLogList dataBean) {
//                logInfoList = dataBean.data.list;
//
//                if (logInfoList == null || logInfoList.size() <= 0) {
//                    ServiceFlowLogList serviceFlowLogList = new ServiceFlowLogList();
//                    ServiceFlowLogList.LogInfo logInfo2 = serviceFlowLogList.new LogInfo();
//                    logInfo2.cts = System.currentTimeMillis();
//                    logInfo2.action = "需求方预约了服务";
//                    ServiceFlowLogList.LogInfo logInfo = serviceFlowLogList.new LogInfo();
//                    logInfo.action = "开始支付";
//                    logInfo.cts = System.currentTimeMillis() + 100;
//                    logInfoList.add(logInfo2);
//                    logInfoList.add(logInfo);
//                }
//
//                setServiceFlowLogItemData();
//            }
//
//            @Override
//            public void executeResultError(String result) {
//                ToastUtils.shortToast("获取服务流程日志失败:" + result);
//            }
//        }, tid + "");

        MyTaskEngine.getLog(new BaseProtocol.IResultExecutor<CommonLogList>() {
            @Override
            public void execute(CommonLogList dataBean) {
                logInfoList = dataBean.data.list;
                setServiceFlowLogItemData();
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "", "2", "2");
    }

    private void setServiceFlowLogItemData() {
        mActivityMyBidServiceBinding.llServiceFlowLogs.removeAllViews();
        for (int i = logInfoList.size() - 1; i >= 0; i--) {
            CommonLogList.CommonLogInfo logInfo = logInfoList.get(i);
            View itemLogInfo = inflateItemLogInfo(logInfo);
            mActivityMyBidServiceBinding.llServiceFlowLogs.addView(itemLogInfo);
        }
    }

    public View inflateItemLogInfo(CommonLogList.CommonLogInfo logInfo) {
        ItemServiceFlowLogBinding itemServiceFlowLogBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_service_flow_log, null, false);
        ItemServiceLogModel itemServiceLogModel = new ItemServiceLogModel(itemServiceFlowLogBinding, mActivity, logInfo);
        itemServiceFlowLogBinding.setItemServiceLogModel(itemServiceLogModel);
        return itemServiceFlowLogBinding.getRoot();
    }

    String dAvatarUrl;
    String dname;

    /**
     * 获取需求者信息
     */
    private void getDemandUserInfo() {
        UserInfoEngine.getOtherUserInfo(new BaseProtocol.IResultExecutor<UserInfoBean>() {
            @Override
            public void execute(UserInfoBean dataBean) {
                UserInfoBean.UInfo uinfo = dataBean.data.uinfo;
                dAvatarUrl = GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + uinfo.avatar;
                BitmapKit.bindImage(mActivityMyBidServiceBinding.ivDemandUserAvatar, dAvatarUrl);
                if (uinfo.isauth == 0) {//未认证
                    setDemandUserIsAuthVisibility(View.GONE);
                } else {
                    setDemandUserIsAuthVisibility(View.VISIBLE);
                }
                dname = uinfo.name;
                setDemandUsername("需求方:" + uinfo.name);

                LogKit.v("需求方信息 uinfo.id:" + uinfo.id);

                loadDataTimes++;
                if (loadDataTimes >= 5) {
                    hideLoadLayer();
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.v("获取需求方信息失败:" + result);
            }
        }, duid + "", "0");
    }

    String sAvatarUrl;
    String sname;

    /**
     * 获取服务者信息
     */
    private void getServiceUserInfo() {
        UserInfoEngine.getOtherUserInfo(new BaseProtocol.IResultExecutor<UserInfoBean>() {
            @Override
            public void execute(UserInfoBean dataBean) {
                UserInfoBean.UInfo uinfo = dataBean.data.uinfo;
                sAvatarUrl = GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + uinfo.avatar;
                BitmapKit.bindImage(mActivityMyBidServiceBinding.ivServiceUserAvatar, sAvatarUrl);
                if (uinfo.isauth == 0) {//未认证
                    setServiceUserIsAuthVisibility(View.GONE);
                } else {
                    setServiceUserIsAuthVisibility(View.VISIBLE);
                }
                sname = uinfo.name;
                setServiceUsername("服务方:" + uinfo.name);

                LogKit.v("服务方信息 uinfo.id:" + uinfo.id);

                loadDataTimes++;
                if (loadDataTimes >= 5) {
                    hideLoadLayer();
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.v("获取服务方信息失败:" + result);
            }
        }, suid + "", "0");
    }

    ArrayList<ServiceInstalmentListBean.InstalmentInfo> instalmentInfoList;

    private void displayStatusButton(ServiceOrderInfoBean dataBean) {
        ServiceOrderInfoBean.Order order = dataBean.data.order;

        switch (order.status) {
            case 2:/*服务者确认*/
            case 3:/*需求方支付中*/
                setCommentVisibility(View.GONE);
                setConfirmFinishVisibility(View.GONE);
                setPaymentVisibility(View.VISIBLE);
                break;
            case 5:/*订单进行中*/
            case 6:/*订单完成*/
                setCommentVisibility(View.GONE);
                setPaymentVisibility(View.GONE);
                //首先隐藏确认完成条目,然后再根据分期完成情况列表接口返回的数据来判断是显示还是隐藏
                setConfirmFinishVisibility(View.GONE);
                setRectifyVisibility(View.GONE);
                //获取分期情况
                ServiceEngine.getServiceInstalmentList(new BaseProtocol.IResultExecutor<ServiceInstalmentListBean>() {
                    @Override
                    public void execute(ServiceInstalmentListBean dataBean) {
                        instalmentInfoList = dataBean.data.list;
                        int totalInstalment = instalmentInfoList.size();//总的分期数
                        for (int i = 0; i < totalInstalment; i++) {
                            ServiceInstalmentListBean.InstalmentInfo instalmentInfo = instalmentInfoList.get(i);
                            if (instalmentInfo.status != 2) {
                                fid = instalmentInfo.id;
                                if (instalmentInfo.status == 1) {
                                    setConfirmFinishVisibility(View.VISIBLE);
                                    mActivityMyBidServiceBinding.tvConfirmText.setText("确认(" + fid + "/" + totalInstalment + ")");
                                    if (fid == totalInstalment) {//如果是最后一期

                                        //获取是否延期支付过
                                        ServiceEngine.getRectifyStatus(new BaseProtocol.IResultExecutor<CommonResultBean>() {
                                            @Override
                                            public void execute(CommonResultBean dataBean) {
                                                if (dataBean.data.status != 1) {//还没有延过期
                                                    setRectifyVisibility(View.VISIBLE);
                                                }
                                            }

                                            @Override
                                            public void executeResultError(String result) {
                                                LogKit.v("获取延期支付状态失败:" + result);
                                                ToastUtils.shortToast("获取延期支付状态失败:" + result);
                                            }
                                        }, soid + "");


//                                        if (myTaskBean.rectify != 1) {//还没有延过期
//                                            setRectifyVisibility(View.VISIBLE);
//                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }

                    @Override
                    public void executeResultError(String result) {
                        ToastUtils.shortToast("获取服务订单的分期信息失败");
                    }
                }, soid + "");
                break;
            case 7:/*订单确认完成*/
                setConfirmFinishVisibility(View.GONE);
                setPaymentVisibility(View.GONE);
                if (order.iscommit == 0) {//未评论
                    setCommentVisibility(View.VISIBLE);//显示去评价
                } else {//已评论
                    setCommentVisibility(View.VISIBLE);//显示查看评价
                    mActivityMyBidServiceBinding.tvBtnComment.setText("查看评价");
                }
                break;
            case 1:/*初始化订单*/
            case 8:/*申请退款*/
            case 9:/*同意退款*/
            case 10:/*平台申诉处理*/
            case 4:/*订单已经取消*/
            case 11:/*服务方拒绝*/
            default:
                setCommentVisibility(View.GONE);
                setConfirmFinishVisibility(View.GONE);
                setPaymentVisibility(View.GONE);
                break;
        }
    }

    private void displayStatusCycle(int status) {
        switch (status) {
            case 1:/*初始化订单*/
                //预约中 大状态
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
            case 2:/*服务者确认*/
            case 3:/*需求方支付中*/
                //预支付 大状态
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
            case 5:/*订单进行中*/
            case 6:/*订单完成*/
            case 8:/*申请退款*/
            case 9:/*同意退款*/
            case 10:/*平台申诉处理*/
                //服务中 大状态
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
            case 7:/*订单确认完成*/
                //评价中 大状态
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4);
                break;
            case 4:/*订单已经取消*/
            case 11:/*服务方拒绝*/
            default:
                //失效 过期 状态 四个圈全都是灰色
                setStatusProgress(R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
        }
    }

    /**
     * 设置 4个圈 表示的大状态进度
     */
    private void setStatusProgress(int bigStateReservationBg, int bigStateReservationTextColor, int bigStatePaymentBg, int bigStatePaymentTextColor, int bigStateServiceBg, int bigStateServiceTextColor, int bigStateCommentBg, int bigStateCommentTextColor) {
        mActivityMyBidServiceBinding.tvServiceReservationing.setBackgroundResource(bigStateReservationBg);
        mActivityMyBidServiceBinding.tvServiceReservationing.setTextColor(bigStateReservationTextColor);
        mActivityMyBidServiceBinding.tvServicePayment.setBackgroundResource(bigStatePaymentBg);
        mActivityMyBidServiceBinding.tvServicePayment.setTextColor(bigStatePaymentTextColor);
        mActivityMyBidServiceBinding.tvServiceServiceing.setBackgroundResource(bigStateServiceBg);
        mActivityMyBidServiceBinding.tvServiceServiceing.setTextColor(bigStateServiceTextColor);
        mActivityMyBidServiceBinding.tvServiceComment.setBackgroundResource(bigStateCommentBg);
        mActivityMyBidServiceBinding.tvServiceComment.setTextColor(bigStateCommentTextColor);
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
     * 点击标题，进入任务详情页
     *
     * @param v
     */
    public void gotoServiceDetail(View v) {
        Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
        intentServiceDetailActivity.putExtra("serviceId", serviceId);
        mActivity.startActivity(intentServiceDetailActivity);
    }

    private String serviceTitle;
    private String idleTime;
    private String quote;
    private int instalmentVisibility;
    private String instalmentRatio;
    private int bpConsultVisibility;

    private int rectifyVisibility = View.GONE;//延期支付按钮是否可见
    private int commentVisibility = View.GONE;
    private int confirmFinishVisibility = View.GONE;
    private int paymentVisibility = View.GONE;
    private int rectifyLayerVisibility = View.GONE;//延期支付的浮层是否可见，默认为不可见

    private int demandUserIsAuthVisibility = View.GONE;
    private String demandUsername;
    private int serviceUserIsAuthVisibility = View.GONE;
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
    public int getDemandUserIsAuthVisibility() {
        return demandUserIsAuthVisibility;
    }

    public void setDemandUserIsAuthVisibility(int demandUserIsAuthVisibility) {
        this.demandUserIsAuthVisibility = demandUserIsAuthVisibility;
        notifyPropertyChanged(BR.demandUserIsAuthVisibility);
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
    public String getServiceUsername() {
        return serviceUsername;
    }

    public void setServiceUsername(String serviceUsername) {
        this.serviceUsername = serviceUsername;
        notifyPropertyChanged(BR.serviceUsername);
    }

    @Bindable
    public int getRectifyLayerVisibility() {
        return rectifyLayerVisibility;
    }

    public void setRectifyLayerVisibility(int rectifyLayerVisibility) {
        this.rectifyLayerVisibility = rectifyLayerVisibility;
        notifyPropertyChanged(BR.rectifyLayerVisibility);
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
    public int getBpConsultVisibility() {
        return bpConsultVisibility;
    }

    public void setBpConsultVisibility(int bpConsultVisibility) {
        this.bpConsultVisibility = bpConsultVisibility;
        notifyPropertyChanged(BR.bpConsultVisibility);
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
    public int getInstalmentVisibility() {
        return instalmentVisibility;
    }

    public void setInstalmentVisibility(int instalmentVisibility) {
        this.instalmentVisibility = instalmentVisibility;
        notifyPropertyChanged(BR.instalmentVisibility);
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

    @Bindable
    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
        notifyPropertyChanged(BR.serviceTitle);
    }
}


