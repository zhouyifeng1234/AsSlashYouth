package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyPublishServiceBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.domain.MyTaskItemBean;
import com.slash.youth.domain.ServiceDetailBean;
import com.slash.youth.domain.ServiceFlowComplainResultBean;
import com.slash.youth.domain.ServiceInstalmentListBean;
import com.slash.youth.domain.ServiceOrderInfoBean;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.engine.ServiceEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.view.SlashDateTimePicker;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhouyifeng on 2016/12/6.
 */
public class MyPublishServiceModel extends BaseObservable {

    ActivityMyPublishServiceBinding mActivityMyPublishServiceBinding;
    Activity mActivity;
    private SlashDateTimePicker mChooseDateTimePicker;

    private long tid;
    private long soid;
    private long suid;
    private int fid;//当前是第几期
    private double orderQuote = -1;//必须是服务订单信息接口返回的报价才是准确的
    private int quoteunit = -1;
    private long duid;//服务订单中的需求方ID
    String[] optionalPriceUnit = new String[]{"次", "个", "幅", "份", "单", "小时", "分钟", "天", "其他"};
    private boolean isUpdateInstalment = true;//修改条件按钮中的分期是否开启，默认为true开启

    public MyPublishServiceModel(ActivityMyPublishServiceBinding activityMyPublishServiceBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityMyPublishServiceBinding = activityMyPublishServiceBinding;
        initData();
        initView();
    }

    MyTaskBean myTaskBean;

    private void initData() {
        myTaskBean = (MyTaskBean) mActivity.getIntent().getSerializableExtra("myTaskBean");
        tid = myTaskBean.tid;//tid就是soid
        soid = tid;//tid（任务id）就是soid(服务订单id)
        fid = myTaskBean.instalmentcurr;//通过调试接口发现，这个字段当type=2为服务的时候，好像不准，一直都是0

        getDataFromServer();
    }

    private void getDataFromServer() {
        getTaskItemData();
        getServiceDetailFromServer();//通过tid获取服务详情信息
        getServiceOrderInfoData();//根据soid(即tid)获取服务订单状态信息
    }

    private void initView() {
        mChooseDateTimePicker = mActivityMyPublishServiceBinding.sdtpPublishServiceChooseDatetime;
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
     * 服务方申诉（不同意退款）按钮
     *
     * @param v
     */
    public void complain(View v) {
        ServiceEngine.serviceComplain(new BaseProtocol.IResultExecutor<ServiceFlowComplainResultBean>() {
            @Override
            public void execute(ServiceFlowComplainResultBean dataBean) {
                ToastUtils.shortToast("申诉完成");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("申诉失败：" + result);
            }
        }, soid + "", "");
    }

    /**
     * 服务方同意退款
     *
     * @param v
     */
    public void agreeRefund(View v) {
        ServiceEngine.serviceAgreeRefund(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("同意退款成功");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("同意退款失败：" + result);
            }
        }, soid + "");
    }

    /**
     * 服务方完成任务
     *
     * @param v
     */
    public void complete(View v) {
        ServiceEngine.complete(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("完成任务成功");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("完成任务失败:" + result);
            }
        }, soid + "", fid + "");
    }

    /**
     * 服务方拒绝需求方
     *
     * @param v
     */
    public void noAccept(View v) {
        ServiceEngine.noAccept(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("拒绝成功");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("拒绝失败:" + result);
            }
        }, soid + "", duid + "");
    }

    /**
     * 服务方接受需求方
     *
     * @param v
     */
    public void accept(View v) {
        //分期 比例暂时先用假数据
        starttime = 1579683600000l;
        endtime = 1679699600000l;
        instalmentRatioList.clear();
        instalmentRatioList.add(0.1);
        instalmentRatioList.add(0.9);
        //分期比例只是测试数据

        ServiceEngine.selected(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("服务方选定成功");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("服务方选定失败:" + result);
            }
        }, soid + "", duid + "", orderQuote + "", starttime + "", endtime + "", instalmentRatioList, bp + "");
    }

    /**
     * 服务方修改条件
     *
     * @param v
     */
    public void updateCondition(View v) {
        setUpdateLayerVisibility(View.VISIBLE);
    }

    /**
     * 修改挑中 按钮中的 开启或者关闭分期
     *
     * @param v
     */
    public void toggleInstalment(View v) {
        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) mActivityMyPublishServiceBinding.ivMyPublishServiceInstalmentHandle.getLayoutParams();
        if (isUpdateInstalment) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            mActivityMyPublishServiceBinding.ivMyPublishServiceInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle_weijihuo);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            mActivityMyPublishServiceBinding.ivMyPublishServiceInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle);
        }
        mActivityMyPublishServiceBinding.ivMyPublishServiceInstalmentHandle.setLayoutParams(layoutParams);
        isUpdateInstalment = !isUpdateInstalment;
    }

    int instalmentCount = 2;
    //    double instalmentRatio1 = 0.1;
//    double instalmentRatio2 = 0.9;
//    double instalmentRatio3 = 0;
//    double instalmentRatio4 = 0;
    ArrayList<Double> instalmentRatioList = new ArrayList<Double>();
    int bp = 1;

    /**
     * 删除分期
     *
     * @param v
     */
    public void deleteInstalment(View v) {

    }

    /**
     * 添加分期
     *
     * @param v
     */
    public void addInstalment(View v) {

    }

    /**
     * 初始化修改条件 蒙层中的分期列表信息（根据myTaskBean任务条目中的分期信息来初始化）
     */
    private void initUpdateInstalmentList(String taskItemInstalmentRatio) {
//一开始应该没有分期比例数据
    }

    private boolean mIsChooseStartTime;

    /**
     * @param v
     */
    public void openUpdateTaskTimeLayer(View v) {
        setSetStartTimeAndEndTimeLayerVisibility(View.VISIBLE);
    }

    public void okChooseIdleStartTimeAndEndTime(View v) {
        setSetStartTimeAndEndTimeLayerVisibility(View.GONE);
    }

    public void closeStartTimeAndEndTimeLayer(View v) {
        setSetStartTimeAndEndTimeLayerVisibility(View.GONE);
    }

    public void chooseStartTime(View v) {
        mIsChooseStartTime = true;
        setChooseDateTimeLayerVisibility(View.VISIBLE);
    }

    public void chooseEndTime(View v) {
        mIsChooseStartTime = false;
        setChooseDateTimeLayerVisibility(View.VISIBLE);
    }

    public void cancelChooseTime(View v) {
        setChooseDateTimeLayerVisibility(View.GONE);
    }

    private int mCurrentChooseMonth;
    private int mCurrentChooseDay;
    private int mCurrentChooseHour;
    private int mCurrentChooseMinute;
    long starttime;
    long endtime;
    String starttimeStr;
    String endtimeStr;

    public void okChooseTime(View v) {
        setChooseDateTimeLayerVisibility(View.GONE);
        mCurrentChooseMonth = mChooseDateTimePicker.getCurrentChooseMonth();
        mCurrentChooseDay = mChooseDateTimePicker.getCurrentChooseDay();
        mCurrentChooseHour = mChooseDateTimePicker.getCurrentChooseHour();
        mCurrentChooseMinute = mChooseDateTimePicker.getCurrentChooseMinute();
        String dateTimeStr = mCurrentChooseMonth + "月" + mCurrentChooseDay + "日" + "-" + mCurrentChooseHour + ":" + (mCurrentChooseMinute < 10 ? "0" + mCurrentChooseMinute : mCurrentChooseMinute);
        if (mIsChooseStartTime) {
            mActivityMyPublishServiceBinding.tvStartTime.setText(dateTimeStr);
            starttime = convertTimeToMillis();
            starttimeStr = dateTimeStr;
        } else {
            mActivityMyPublishServiceBinding.tvEndTime.setText(dateTimeStr);
            endtime = convertTimeToMillis();
            endtimeStr = dateTimeStr;
        }

    }

    public long convertTimeToMillis() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, mCurrentChooseMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, mCurrentChooseDay);
        calendar.set(Calendar.HOUR_OF_DAY, mCurrentChooseHour);
        calendar.set(Calendar.MINUTE, mCurrentChooseMinute);
        return calendar.getTimeInMillis();
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
                initUpdateInstalmentList(myTaskBean.instalmentratio);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "", "2", "1");
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
                //报价 这里不能使用服务详情接口返回的报价
                quoteunit = service.quoteunit;
                CommonUtils.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (orderQuote != -1) {
                            if (quoteunit == 9) {
                                setQuote(orderQuote + "元");
                            } else if (quoteunit > 0 && quoteunit < 9) {
                                setQuote(orderQuote + "元/" + optionalPriceUnit[quoteunit - 1]);
                            }
                        }
                    }
                });
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
        }, tid + "", "1");
    }


    /**
     * 根据soid(即tid)获取服务订单状态信息
     */
    private void getServiceOrderInfoData() {

        ServiceEngine.getServiceOrderInfo(new BaseProtocol.IResultExecutor<ServiceOrderInfoBean>() {
            @Override
            public void execute(ServiceOrderInfoBean dataBean) {
                orderQuote = dataBean.data.order.quote;
                CommonUtils.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (quoteunit != -1) {
                            if (quoteunit == 9) {
                                setQuote(orderQuote + "元");
                            } else if (quoteunit > 0 && quoteunit < 9) {
                                setQuote(orderQuote + "元/" + optionalPriceUnit[quoteunit - 1]);
                            }
                        }
                    }
                });

                int status = dataBean.data.order.status;
                displayStatusCycle(status);
                displayStatusButton(dataBean);//显示对应不同状态的操作按钮
                suid = dataBean.data.order.suid;
                duid = dataBean.data.order.uid;
            }

            @Override
            public void executeResultError(String result) {

            }
        }, soid + "");
    }

    ArrayList<ServiceInstalmentListBean.InstalmentInfo> instalmentInfoList;

    private void displayStatusButton(ServiceOrderInfoBean dataBean) {
        ServiceOrderInfoBean.Order order = dataBean.data.order;

        switch (order.status) {
            case 1:/*初始化订单*/
                setAcceptItemVisibility(View.VISIBLE);
                break;
            case 5:/*订单进行中*/
                //获取分期情况 来判断是否需要显示完成按钮
                ServiceEngine.getServiceInstalmentList(new BaseProtocol.IResultExecutor<ServiceInstalmentListBean>() {
                    @Override
                    public void execute(ServiceInstalmentListBean dataBean) {
                        instalmentInfoList = dataBean.data.list;
                        int totalInstalment = instalmentInfoList.size();//总的分期数
                        for (int i = 0; i < totalInstalment; i++) {
                            ServiceInstalmentListBean.InstalmentInfo instalmentInfo = instalmentInfoList.get(i);
                            if (instalmentInfo.status != 2) {
                                fid = instalmentInfo.id;
                                if (instalmentInfo.status == 0) {
                                    setFinishItemVisibility(View.VISIBLE);
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
            case 8:/*申请退款*/
                setRefundItemVisibility(View.VISIBLE);
                break;
            case 7:/*订单确认完成*/
            case 2:/*服务者确认*/
            case 3:/*需求方支付中*/
            case 6:/*订单完成*/
            case 9:/*同意退款*/
            case 10:/*平台申诉处理*/
            case 4:/*订单已经取消*/
            case 11:/*服务方拒绝*/
            default:
                setFinishItemVisibility(View.GONE);
                setAcceptItemVisibility(View.GONE);
                setRefundItemVisibility(View.GONE);
                break;
        }
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
        mActivityMyPublishServiceBinding.tvServiceReservationing.setBackgroundResource(bigStateReservationBg);
        mActivityMyPublishServiceBinding.tvServiceReservationing.setTextColor(bigStateReservationTextColor);
        mActivityMyPublishServiceBinding.tvServicePayment.setBackgroundResource(bigStatePaymentBg);
        mActivityMyPublishServiceBinding.tvServicePayment.setTextColor(bigStatePaymentTextColor);
        mActivityMyPublishServiceBinding.tvServiceServiceing.setBackgroundResource(bigStateServiceBg);
        mActivityMyPublishServiceBinding.tvServiceServiceing.setTextColor(bigStateServiceTextColor);
        mActivityMyPublishServiceBinding.tvServiceComment.setBackgroundResource(bigStateCommentBg);
        mActivityMyPublishServiceBinding.tvServiceComment.setTextColor(bigStateCommentTextColor);
    }

    private String serviceTitle;
    private String idleTime;
    private String quote;
    private int instalmentVisibility;
    private String instalmentRatio;
    private int bpConsultVisibility;

    private int refundItemVisibility = View.GONE;//申诉、同意退款 条目是否可见
    private int finishItemVisibility = View.GONE;//完成 条目是否可见
    private int acceptItemVisibility = View.GONE;//不接受、接受、修改条件 条目是否可见

    private int updateLayerVisibility = View.GONE;//修改服务订单信息蒙层是否可见
    private int updateInstalmentLine1Visibility = View.GONE;
    private int updateInstalmentLine2Visibility = View.GONE;
    private int updateInstalmentLine3Visibility = View.GONE;
    private int updateInstalmentLine4Visibility = View.GONE;

    private int setStartTimeAndEndTimeLayerVisibility = View.GONE;
    private int chooseDateTimeLayerVisibility = View.GONE;

    @Bindable
    public int getChooseDateTimeLayerVisibility() {
        return chooseDateTimeLayerVisibility;
    }

    public void setChooseDateTimeLayerVisibility(int chooseDateTimeLayerVisibility) {
        this.chooseDateTimeLayerVisibility = chooseDateTimeLayerVisibility;
        notifyPropertyChanged(BR.chooseDateTimeLayerVisibility);
    }

    @Bindable
    public int getSetStartTimeAndEndTimeLayerVisibility() {
        return setStartTimeAndEndTimeLayerVisibility;
    }

    public void setSetStartTimeAndEndTimeLayerVisibility(int setStartTimeAndEndTimeLayerVisibility) {
        this.setStartTimeAndEndTimeLayerVisibility = setStartTimeAndEndTimeLayerVisibility;
        notifyPropertyChanged(BR.setStartTimeAndEndTimeLayerVisibility);
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
    public int getUpdateInstalmentLine2Visibility() {
        return updateInstalmentLine2Visibility;
    }

    public void setUpdateInstalmentLine2Visibility(int updateInstalmentLine2Visibility) {
        this.updateInstalmentLine2Visibility = updateInstalmentLine2Visibility;
        notifyPropertyChanged(BR.updateInstalmentLine2Visibility);
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
    public int getUpdateInstalmentLine4Visibility() {
        return updateInstalmentLine4Visibility;
    }

    public void setUpdateInstalmentLine4Visibility(int updateInstalmentLine4Visibility) {
        this.updateInstalmentLine4Visibility = updateInstalmentLine4Visibility;
        notifyPropertyChanged(BR.updateInstalmentLine4Visibility);
    }

    @Bindable
    public int getUpdateLayerVisibility() {
        return updateLayerVisibility;
    }

    public void setUpdateLayerVisibility(int updateLayerVisibility) {
        this.updateLayerVisibility = updateLayerVisibility;
        notifyPropertyChanged(BR.updateLayerVisibility);
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
    public int getFinishItemVisibility() {
        return finishItemVisibility;
    }

    public void setFinishItemVisibility(int finishItemVisibility) {
        this.finishItemVisibility = finishItemVisibility;
        notifyPropertyChanged(BR.finishItemVisibility);
    }

    @Bindable
    public int getRefundItemVisibility() {
        return refundItemVisibility;
    }

    public void setRefundItemVisibility(int refundItemVisibility) {
        this.refundItemVisibility = refundItemVisibility;
        notifyPropertyChanged(BR.refundItemVisibility);
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
    public int getBpConsultVisibility() {
        return bpConsultVisibility;
    }

    public void setBpConsultVisibility(int bpConsultVisibility) {
        this.bpConsultVisibility = bpConsultVisibility;
        notifyPropertyChanged(BR.bpConsultVisibility);
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
