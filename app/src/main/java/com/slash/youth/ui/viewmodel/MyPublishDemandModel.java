package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyPublishDemandBinding;
import com.slash.youth.databinding.ItemDemandFlowLogBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.DelayPayBean;
import com.slash.youth.domain.DemandDetailBean;
import com.slash.youth.domain.DemandFlowLogList;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.domain.MyTaskItemBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.CommentActivity;
import com.slash.youth.ui.activity.PaymentActivity;
import com.slash.youth.ui.activity.RefundActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;

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

        initData();
        initView();
    }

    ArrayList<DemandFlowLogList.LogInfo> listLogInfo = new ArrayList<DemandFlowLogList.LogInfo>();

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

    public void goBack(View v) {
        mActivity.finish();
    }

    /**
     * 打开聊天界面聊一聊
     *
     * @param v
     */
    public void havaAChat(View v) {

    }

    //需求方评论
    public void comment(View v) {
        Intent intentCommentActivity = new Intent(CommonUtils.getContext(), CommentActivity.class);

        Bundle commentInfo = new Bundle();
        commentInfo.putLong("tid", tid);
        commentInfo.putInt("type", type);
        commentInfo.putLong("suid", innerDemandCardInfo.suid);
        intentCommentActivity.putExtras(commentInfo);

        mActivity.startActivity(intentCommentActivity);
    }

    //申请退款
    public void refund(View v) {
        Intent intentRefundActivity = new Intent(CommonUtils.getContext(), RefundActivity.class);
        mActivity.startActivity(intentRefundActivity);
    }

    //延期支付,出现他弹框
    public void rectifyPayment(View v) {
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
        int fid = innerDemandCardInfo.instalmentcurr;
        DemandEngine.demandPartyConfirmComplete(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                //确认完成成功
                ToastUtils.shortToast("确认完成成功");
            }

            @Override
            public void executeResultError(String result) {
                //确认完成失败
                ToastUtils.shortToast("确认完成失败");
            }
        }, tid + "", fid + "");
    }

    //打开支付界面
    public void openPaymentActivity(View v) {
        Intent intentPaymentActivity = new Intent(CommonUtils.getContext(), PaymentActivity.class);

        Bundle payInfo = new Bundle();
        payInfo.putLong("tid", tid);
        payInfo.putDouble("quote", innerDemandCardInfo.quote);
        intentPaymentActivity.putExtras(payInfo);

        mActivity.startActivity(intentPaymentActivity);

        //暂时先用余额支付测试
    }

    private void getDemandFlowLogData() {
        DemandEngine.getDemandFlowLog(new BaseProtocol.IResultExecutor<DemandFlowLogList>() {
            @Override
            public void execute(DemandFlowLogList dataBean) {
                listLogInfo = dataBean.data.list;
                setDemandFlowLogItemData();
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "");
    }

    public void setDemandFlowLogItemData() {
        for (int i = listLogInfo.size() - 1; i >= 0; i--) {
            DemandFlowLogList.LogInfo logInfo = listLogInfo.get(i);
            View itemLogInfo = inflateItemLogInfo(logInfo);
            mActivityMyPublishDemandBinding.llDemandFlowLogs.addView(itemLogInfo);
        }
    }

    public View inflateItemLogInfo(DemandFlowLogList.LogInfo logInfo) {
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
                innerDemandCardInfo = new InnerDemandCardInfo();

                innerDemandCardInfo.uid = taskinfo.uid;
                innerDemandCardInfo.avatar = taskinfo.avatar;
                innerDemandCardInfo.username = taskinfo.name;
                innerDemandCardInfo.isAuth = taskinfo.isauth;
                innerDemandCardInfo.title = taskinfo.title;
                innerDemandCardInfo.starttime = taskinfo.starttime;
                innerDemandCardInfo.quote = taskinfo.quote;
                innerDemandCardInfo.instalment = taskinfo.instalment;//0 or 1 表示是否开启分期 0不允许分期 1允许分期
                innerDemandCardInfo.instalmentcurr = taskinfo.instalmentcurr;//表示当前处于第几个分期
                innerDemandCardInfo.instalmentcurrfinish = taskinfo.instalmentcurrfinish;//表示当期是否服务方完成 0未完成 1已经完成
                innerDemandCardInfo.instalmentratio = taskinfo.instalmentratio;//表示分期情况，格式为30,20,10,40 (英文逗号分隔)
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
                innerDemandCardInfo.suid = dataBean.data.demand.suid;
                innerDemandCardInfo.bp = dataBean.data.demand.bp;
                innerDemandCardInfo.isComment = dataBean.data.demand.iscomment;

                setMyPublishDemandInfo();
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "");
    }

    private void setMyPublishDemandInfo() {
        //设置详细信息


        displayStatusButton();
        displayStatusProgressCycle();
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
                int totalInstalmentCount = innerDemandCardInfo.instalmentratio.split(",").length;//总的分期数
                int instalmentcurr = innerDemandCardInfo.instalmentcurr;
                if (instalmentcurr == 0) {
                    instalmentcurr = 1;
                }
                if (instalmentcurr < totalInstalmentCount) {//不是最后一期
                    if (innerDemandCardInfo.instalmentcurrfinish == 1) {
                        setStatusButtonsVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE);
                    } else {
                        setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
                    }
                } else {//最后一期
                    if (innerDemandCardInfo.instalmentcurrfinish == 1) {
                        if (innerDemandCardInfo.rectify == 1) {
                            setStatusButtonsVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE);
                        } else {
                            setStatusButtonsVisibility(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
                        }
                    } else {
                        setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
                    }
                }
                break;
            case 8:
                if (innerDemandCardInfo.isComment == 0) {
                    setStatusButtonsVisibility(View.GONE, View.GONE, View.VISIBLE, View.GONE);
                } else {
                    setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
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

    private int paymentVisibility = View.GONE;
    private int confirmFinishVisibility = View.GONE;
    private int commentVisibility = View.GONE;
    private int rectifyVisibility = View.GONE;
    private int rectifyLayerVisibility = View.GONE;

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
