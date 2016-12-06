package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyBidServiceBinding;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.domain.ServiceDetailBean;
import com.slash.youth.domain.ServiceOrderInfoBean;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.engine.ServiceEngine;
import com.slash.youth.http.protocol.BaseProtocol;

import java.text.SimpleDateFormat;

/**
 * Created by zhouyifeng on 2016/12/6.
 */
public class MyBidServiceModel extends BaseObservable {

    ActivityMyBidServiceBinding mActivityMyBidServiceBinding;
    Activity mActivity;

    private long tid;
    private long soid;
    String[] optionalPriceUnit = new String[]{"次", "个", "幅", "份", "单", "小时", "分钟", "天", "其他"};

    public MyBidServiceModel(ActivityMyBidServiceBinding activityMyBidServiceBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityMyBidServiceBinding = activityMyBidServiceBinding;
        initData();
        initView();
    }

    MyTaskBean myTaskBean;

    private void initData() {
//        tid = mActivity.getIntent().getLongExtra("tid", -1);
        myTaskBean = (MyTaskBean) mActivity.getIntent().getSerializableExtra("myTaskBean");
        tid = myTaskBean.tid;//tid就是soid
        soid = tid;//tid（任务id）就是soid(服务订单id)

        getServiceDetailFromServer();//通过tid获取服务详情信息
        getServiceOrderInfoData();//根据soid(即tid)获取服务订单状态信息
    }

    private void initView() {

    }

    public void goBack(View v) {
        mActivity.finish();
    }

    /**
     * 打开聊天界面
     *
     * @param v
     */
    public void havaAChat(View v) {

    }

    /**
     * 通过tid获取服务详情信息
     */
    private void getServiceDetailFromServer() {
        MyTaskEngine.getServiceDetailByTid(new BaseProtocol.IResultExecutor<ServiceDetailBean>() {
            @Override
            public void execute(ServiceDetailBean dataBean) {
                ServiceDetailBean.Service service = dataBean.data.service;
                //服务标题，布局文件中有两个地方需要设置
                setServiceTitle(service.title);
                //闲置时间
                SimpleDateFormat sdfIdleTime = new SimpleDateFormat("MM月dd日 hh:mm");
                String starttimeStr = sdfIdleTime.format(service.starttime);
                String endtimeStr = sdfIdleTime.format(service.endtime);
                setIdleTime("闲置时间:" + starttimeStr + "-" + endtimeStr);
                //报价
                if (service.quoteunit == 9) {
                    setQuote(service.quote + "元");
                } else if (service.quoteunit > 0 && service.quoteunit < 9) {
                    setQuote(service.quote + "元/" + optionalPriceUnit[service.quoteunit - 1]);
                }
                //分期
                //这里不能用service详情的instalment，要用任务列表item的instalment
                if (myTaskBean.instalment == 1) {//开启分期
                    setInstalmentVisibility(View.VISIBLE);
                    String instalmentRatioStr = "";
                    String[] ratios = myTaskBean.instalmentratio.split(",");
                    for (int i = 0; i < ratios.length; i++) {
                        if (i < ratios.length - 1) {
                            instalmentRatioStr += ratios[i] + "%/";
                        } else {
                            instalmentRatioStr += ratios[i] + "%";
                        }
                    }
                    setInstalmentRatio(instalmentRatioStr);
                } else {//未开启分期
                    setInstalmentVisibility(View.INVISIBLE);
                }
                //纠纷处理方式（似乎协商处理就显示）
                if (service.bp == 2) {//协商
                    setBpConsultVisibility(View.VISIBLE);
                } else if (service.bp == 1) {//平台
                    setBpConsultVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "", "2");
    }

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
                int status = dataBean.data.order.status;
                displayStatusCycle(status);

            }

            @Override
            public void executeResultError(String result) {

            }
        }, soid + "");
    }

    private void displayStatusCycle(int status) {
        switch (status) {
            case 1:/*初始化订单*/
            case 2:/*服务者确认*/
                //预约中 大状态
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
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

    private String serviceTitle;
    private String idleTime;
    private String quote;
    private int instalmentVisibility;
    private String instalmentRatio;
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
        notifyPropertyChanged(BR.instalmentItemVisibility);
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


