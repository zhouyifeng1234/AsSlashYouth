package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyBidDemandBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.DemandDetailBean;
import com.slash.youth.domain.InterventionBean;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.domain.MyTaskItemBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.text.SimpleDateFormat;

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
     * 打开聊天界面聊一聊
     *
     * @param v
     */
    public void havaAChat(View v) {

    }


    /**
     * 抢需求者同意需求方
     *
     * @param v
     */
    public void accept(View v) {
        DemandEngine.servicePartyConfirmServant(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("服务方确认成功");
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
        DemandEngine.servicePartyReject(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("服务方拒绝成功");
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
        DemandEngine.servicePartyComplete(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("服务方完成成功");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("服务方完成失败:" + result);
            }
        }, tid + "", fid + "");
    }

    /**
     * 同意退款
     *
     * @param v
     */
    public void agreeRefund(View v) {
        DemandEngine.servicePartyAgreeRefund(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("服务方同意退款成功");
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
        DemandEngine.servicePartyIntervention(new BaseProtocol.IResultExecutor<InterventionBean>() {
            @Override
            public void execute(InterventionBean dataBean) {
                ToastUtils.shortToast("服务方申诉成功");
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
                LogKit.v("taskinfo.instalmentcurr:" + taskinfo.instalmentcurr);
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
        setDemandTitle(innerDemandCardInfo.title);
        SimpleDateFormat sdfStarttime = new SimpleDateFormat("开始时间:yyyy年MM月dd日 hh:mm");
        setStarttime(sdfStarttime.format(innerDemandCardInfo.starttime));
        setQuote(innerDemandCardInfo.quote + "元");
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
                    ratioStr += ratio + "%";
                } else {
                    ratioStr += ratio + "%/";
                }
            }
        } else {//不分期
            setInstalmentVisibility(View.GONE);
        }
        if (innerDemandCardInfo.bp == 2) {//协商
            setBpConsultVisibility(View.VISIBLE);
        } else { //1 平台
            setBpConsultVisibility(View.GONE);
        }

        displayStatusButton();
        displayStatusProgressCycle();
    }

    private void displayStatusButton() {
        int status = innerDemandCardInfo.status;
        switch (status) {
            case 5:/*待确认*/
                setStatusButtonsVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE);
                LogKit.v("--------------待确认--------------------");
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
                if (innerDemandCardInfo.isComment == 1) {
                    setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.VISIBLE);
                } else {
                    setStatusButtonsVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
                }
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

    private String demandTitle;
    private String starttime; //开始时间:2016年9月18日 8:00
    private String quote;   //300元
    private int instalmentVisibility;
    private String instalmentRatio;//30%/40%/40%/50%
    private int bpConsultVisibility;

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
