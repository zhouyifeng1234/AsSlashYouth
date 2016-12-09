package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyBidDemandBinding;
import com.slash.youth.domain.DemandDetailBean;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.domain.MyTaskItemBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.http.protocol.BaseProtocol;

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

    public MyBidDemandModel(ActivityMyBidDemandBinding activityMyBidDemandBinding, Activity activity) {
        this.mActivityMyBidDemandBinding = activityMyBidDemandBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {
        Bundle taskInfo = mActivity.getIntent().getExtras();
        tid = taskInfo.getLong("tid");
        type = taskInfo.getInt("type");
        roleid = taskInfo.getInt("roleid");

        getDataFromServer();
    }

    private void getDataFromServer() {
        getTaskItemData();
//        getDemandFlowLogData();
    }

    private void initView() {

    }

    public void goBack(View v) {
        mActivity.finish();
    }


    /**
     * 抢需求者同意需求方
     *
     * @param v
     */
    public void accept(View v) {

    }

    /**
     * 抢需求者拒绝需求方
     *
     * @param v
     */
    public void noAccept(View v) {

    }

    /**
     * 服务方完成 任务
     *
     * @param v
     */
    public void complete(View v) {

    }

    /**
     * 同意退款
     *
     * @param v
     */
    public void agreeRefund(View v) {

    }

    /**
     * 申诉，不同意退款
     *
     * @param v
     */
    public void complain(View v) {

    }

    /**
     * 查看评价
     *
     * @param v
     */
    public void viewComment(View v) {

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


        displayStatusButton();
        displayStatusProgressCycle();
    }

    private void displayStatusButton() {
        int status = innerDemandCardInfo.status;
        switch (status) {
            case 5:/*待确认*/
                setStatusButtonsVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE);
                break;
            case 7:/*进行中*/
                if (innerDemandCardInfo.instalmentcurr == 0) {
                    fid = 1;
                } else {
                    fid = innerDemandCardInfo.instalmentcurr;
                }
                if (innerDemandCardInfo.instalmentcurrfinish == 0) {
                    setStatusButtonsVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE);
                } else {
                    setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
                }
                break;
            case 9:/*申请退款*/
                setStatusButtonsVisibility(View.GONE, View.GONE, View.VISIBLE, View.GONE);
                break;
            case 8:/*已完成*/
                //服务方完成了最后一期（需求方可能还没去确认完成，也可能确认了完成），当需求方确认完成以后，并且进行了评价，才显示"查看评价"按钮
                setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.VISIBLE);
                break;
            case 1:/*已发布*/
            case 2:/*已取消*/
            case 3:/*被拒绝*/
            case 4:/*待选择*/
            case 6://待支付
            case 10:/*已退款*/
            case 11:/*已淘汰*/
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

    private void setStatusButtonsVisibility(int acceptItemVisibility, int completeItemVisibility, int isAgreeRefundItemVisibility, int viewCommentItemVisibility) {
        setAcceptItemVisibility(acceptItemVisibility);
        setCompleteItemVisibility(completeItemVisibility);
        setIsAgreeRefundItemVisibility(isAgreeRefundItemVisibility);
        setViewCommentItemVisibility(viewCommentItemVisibility);
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

    private int acceptItemVisibility = View.GONE;
    private int completeItemVisibility = View.GONE;
    private int isAgreeRefundItemVisibility = View.GONE;
    private int viewCommentItemVisibility = View.GONE;

    @Bindable
    public int getAcceptItemVisibility() {
        return acceptItemVisibility;
    }

    public void setAcceptItemVisibility(int acceptItemVisibility) {
        this.acceptItemVisibility = acceptItemVisibility;
    }

    @Bindable
    public int getCompleteItemVisibility() {
        return completeItemVisibility;
    }

    public void setCompleteItemVisibility(int completeItemVisibility) {
        this.completeItemVisibility = completeItemVisibility;
    }

    @Bindable
    public int getIsAgreeRefundItemVisibility() {
        return isAgreeRefundItemVisibility;
    }

    public void setIsAgreeRefundItemVisibility(int isAgreeRefundItemVisibility) {
        this.isAgreeRefundItemVisibility = isAgreeRefundItemVisibility;
    }

    @Bindable
    public int getViewCommentItemVisibility() {
        return viewCommentItemVisibility;
    }

    public void setViewCommentItemVisibility(int viewCommentItemVisibility) {
        this.viewCommentItemVisibility = viewCommentItemVisibility;
    }
}
