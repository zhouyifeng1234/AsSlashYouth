package com.slash.youth.engine;

import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.PublishServiceProtocol;
import com.slash.youth.http.protocol.ServiceConfirmCompleteProtocol;
import com.slash.youth.http.protocol.ServiceDelayPayProtocol;
import com.slash.youth.http.protocol.ServiceDetailProtocol;
import com.slash.youth.http.protocol.ServiceInstalmentListProtocol;
import com.slash.youth.http.protocol.ServiceOrderInfoProtocol;
import com.slash.youth.http.protocol.ServiceOrderStatusProtocol;
import com.slash.youth.http.protocol.UpdateServiceProtocol;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/1.
 * 服务相关操作
 */
public class ServiceEngine {

    public static final int SERVICE_TIMETYPE_USER_DEFINED = 0;
    public static final int SERVICE_TIMETYPE_AFTER_WORK = 1;
    public static final int SERVICE_TIMETYPE_WEEKEND = 2;
    public static final int SERVICE_TIMETYPE_AFTER_WORK_AND_WEEKEND = 3;
    public static final int SERVICE_TIMETYPE_ANYTIME = 4;

    /**
     * 一、[服务]-发布服务
     *
     * @param onPublishServiceFinished
     * @param title
     * @param listTag
     * @param startime
     * @param endtime
     * @param anonymity
     * @param desc
     * @param timetype
     * @param listPic
     * @param instalment
     * @param bp
     * @param pattern
     * @param place
     * @param lng
     * @param lat
     * @param quote
     * @param quoteunit
     */
    public static void publishService(BaseProtocol.IResultExecutor onPublishServiceFinished, String title, ArrayList<String> listTag, long startime, long endtime, int anonymity, String desc, int timetype, ArrayList<String> listPic, int instalment, int bp, int pattern, String place, double lng, double lat, double quote, int quoteunit) {
        PublishServiceProtocol publishServiceProtocol = new PublishServiceProtocol(title, listTag, startime, endtime, anonymity, desc, timetype, listPic, instalment, bp, pattern, place, lng, lat, quote, quoteunit);
        publishServiceProtocol.getDataFromServer(onPublishServiceFinished);
    }


    /**
     * 二、[服务]-查看服务详情
     *
     * @param onGetServiceDetailFinished
     * @param id                         服务ID
     */
    public static void getServiceDetail(BaseProtocol.IResultExecutor onGetServiceDetailFinished, String id) {
        ServiceDetailProtocol serviceDetailProtocol = new ServiceDetailProtocol(id);
        serviceDetailProtocol.getDataFromServer(onGetServiceDetailFinished);
    }


    /**
     * 三、[服务]-修改服务
     *
     * @param onUpdateServiceFinished
     * @param id
     * @param title
     * @param listTag
     * @param startime
     * @param endtime
     * @param anonymity
     * @param desc
     * @param timetype
     * @param listPic
     * @param instalment
     * @param bp
     * @param pattern
     * @param place
     * @param lng
     * @param lat
     * @param quote
     * @param quoteunit
     */
    public static void updateService(BaseProtocol.IResultExecutor onUpdateServiceFinished, String id, String title, ArrayList<String> listTag, long startime, long endtime, int anonymity, String desc, int timetype, ArrayList<String> listPic, int instalment, int bp, int pattern, String place, double lng, double lat, double quote, int quoteunit) {
        UpdateServiceProtocol updateServiceProtocol = new UpdateServiceProtocol(id, title, listTag, startime, endtime, anonymity, desc, timetype, listPic, instalment, bp, pattern, place, lng, lat, quote, quoteunit);
        updateServiceProtocol.getDataFromServer(onUpdateServiceFinished);
    }

    /**
     * 六、[服务]-需求方预支付
     */
    public static void servicePayment(BaseProtocol.IResultExecutor onPaymentFinished, String soid, String amount, String channel) {
        ServiceFlowPaymentProtocol serviceFlowPaymentProtocol = new ServiceFlowPaymentProtocol(soid, amount, channel);
        serviceFlowPaymentProtocol.getDataFromServer(onPaymentFinished);
    }


    /**
     * 八、[服务]-需求方确认完成任务
     */
    public static void confirmComplete(BaseProtocol.IResultExecutor onConfirmCompleteFinished, String soid, String fid) {
        ServiceConfirmCompleteProtocol serviceConfirmCompleteProtocol = new ServiceConfirmCompleteProtocol(soid, fid);
        serviceConfirmCompleteProtocol.getDataFromServer(onConfirmCompleteFinished);
    }

    /**
     * 十二、[服务]-查看分期完成情况列表
     */
    public static void getServiceInstalmentList(BaseProtocol.IResultExecutor onGetInstalmentListFinished, String soid) {
        ServiceInstalmentListProtocol serviceInstalmentListProtocol = new ServiceInstalmentListProtocol(soid);
        serviceInstalmentListProtocol.getDataFromServer(onGetInstalmentListFinished);
    }

    /**
     * 十三、[服务]-查看服务订单状态(好像这个接口不能使用了，使用“v1/api/service/orderinfo”接口获取订单信息，里面有status)
     */
    public static void getServiceOrderStatus(BaseProtocol.IResultExecutor onGetServiceOrderStatusFinished, String soid) {
        ServiceOrderStatusProtocol serviceOrderStatusProtocol = new ServiceOrderStatusProtocol(soid);
        serviceOrderStatusProtocol.getDataFromServer(onGetServiceOrderStatusFinished);
    }

    public static void getServiceOrderInfo(BaseProtocol.IResultExecutor onGetServiceOrderInfoFinished, String soid) {
        ServiceOrderInfoProtocol serviceOrderInfoProtocol = new ServiceOrderInfoProtocol(soid);
        serviceOrderInfoProtocol.getDataFromServer(onGetServiceOrderInfoFinished);
    }

    /**
     * 预约服务者 延期支付
     */
    public static void delayPay(BaseProtocol.IResultExecutor onDelayPayFinished, String soid, String fid) {
        ServiceDelayPayProtocol serviceDelayPayProtocol = new ServiceDelayPayProtocol(soid, fid);
        serviceDelayPayProtocol.getDataFromServer(onDelayPayFinished);
    }


}
