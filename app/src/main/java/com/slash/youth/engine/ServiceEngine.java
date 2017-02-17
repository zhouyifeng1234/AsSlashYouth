package com.slash.youth.engine;

import com.slash.youth.http.protocol.AppointmentServiceProtocol;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CollectTaskProtocol;
import com.slash.youth.http.protocol.DetailRecommendServiceProtocol;
import com.slash.youth.http.protocol.GetServiceFlowLogProtocol;
import com.slash.youth.http.protocol.ServiceIsRectifyProtocol;
import com.slash.youth.http.protocol.PublishServiceProtocol;
import com.slash.youth.http.protocol.RecommendDemandUserProtocol;
import com.slash.youth.http.protocol.ServiceConfirmCompleteProtocol;
import com.slash.youth.http.protocol.ServiceDelayPayProtocol;
import com.slash.youth.http.protocol.ServiceDetailProtocol;
import com.slash.youth.http.protocol.ServiceFlowAgreeRefundProtocol;
import com.slash.youth.http.protocol.ServiceFlowComplainProtocol;
import com.slash.youth.http.protocol.ServiceFlowCompleteProtocol;
import com.slash.youth.http.protocol.ServiceFlowNoAcceptProtocol;
import com.slash.youth.http.protocol.ServiceFlowPaymentProtocol;
import com.slash.youth.http.protocol.ServiceFlowSelectedProtocol;
import com.slash.youth.http.protocol.ServiceInstalmentListProtocol;
import com.slash.youth.http.protocol.ServiceOrderInfoProtocol;
import com.slash.youth.http.protocol.ServiceOrderStatusProtocol;
import com.slash.youth.http.protocol.ServiceRefundProtocol;
import com.slash.youth.http.protocol.ServiceThirdPayProtocol;
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

    public static void appointmentService(BaseProtocol.IResultExecutor onAppointmentServiceFinished, String serviceId) {
        AppointmentServiceProtocol appointmentServiceProtocol = new AppointmentServiceProtocol(serviceId);
        appointmentServiceProtocol.getDataFromServer(onAppointmentServiceFinished);
    }


    /**
     * 五、[服务]-服务方选定
     */
    public static void selected(BaseProtocol.IResultExecutor onSelectedFinished, String soid, String uid, String quote, String starttime, String endtime, ArrayList<Double> instalment, String bp, String ismodify) {
        ServiceFlowSelectedProtocol serviceFlowSelectedProtocol = new ServiceFlowSelectedProtocol(soid, uid, quote, starttime, endtime, instalment, bp, ismodify);
        serviceFlowSelectedProtocol.getDataFromServer(onSelectedFinished);
    }

    /**
     * 六、[服务]-需求方预支付
     *
     * @param onPaymentFinished
     * @param soid
     * @param amount
     * @param channel
     * @param pass              支付密码 这个是原始密码经过一次MD5
     */
    public static void servicePayment(BaseProtocol.IResultExecutor onPaymentFinished, String soid, String amount, String channel, String pass) {
        ServiceFlowPaymentProtocol serviceFlowPaymentProtocol = new ServiceFlowPaymentProtocol(soid, amount, channel, pass);
        serviceFlowPaymentProtocol.getDataFromServer(onPaymentFinished);
    }

    /**
     * 第三方支付，获取charge字符串
     */
    public static void serviceThirdPay(BaseProtocol.IResultExecutor onThirdPayFinished, String id, String amount, String channel) {
        ServiceThirdPayProtocol serviceThirdPayProtocol = new ServiceThirdPayProtocol(id, amount, channel);
        serviceThirdPayProtocol.getDataFromServer(onThirdPayFinished);
    }

    /**
     * 七、[服务]-服务方完成任务
     *
     * @param onCompleteFinished
     * @param soid               需求ID
     * @param fid                表示完成第几个分期
     */
    public static void complete(BaseProtocol.IResultExecutor onCompleteFinished, String soid, String fid) {
        ServiceFlowCompleteProtocol serviceFlowCompleteProtocol = new ServiceFlowCompleteProtocol(soid, fid);
        serviceFlowCompleteProtocol.getDataFromServer(onCompleteFinished);
    }


    /**
     * 八、[服务]-需求方确认完成任务
     */
    public static void confirmComplete(BaseProtocol.IResultExecutor onConfirmCompleteFinished, String soid, String fid) {
        ServiceConfirmCompleteProtocol serviceConfirmCompleteProtocol = new ServiceConfirmCompleteProtocol(soid, fid);
        serviceConfirmCompleteProtocol.getDataFromServer(onConfirmCompleteFinished);
    }

    /**
     * 九、[服务]-需求方申请退款
     *
     * @param onRefundFinished
     * @param soid             服务订单ID
     * @param reason           1、2、3   reason和reasondetail至少有一个
     * @param reasondetail     退款原因详细   reason和reasondetail至少有一个
     */
    public static void refund(BaseProtocol.IResultExecutor onRefundFinished, String soid, String reason, String reasondetail) {
        ServiceRefundProtocol serviceRefundProtocol = new ServiceRefundProtocol(soid, reason, reasondetail);
        serviceRefundProtocol.getDataFromServer(onRefundFinished);
    }


    /**
     * 十、[服务]-服务方同意退款
     */
    public static void serviceAgreeRefund(BaseProtocol.IResultExecutor onAgreeRefundFinished, String soid) {
        ServiceFlowAgreeRefundProtocol serviceFlowAgreeRefundProtocol = new ServiceFlowAgreeRefundProtocol(soid);
        serviceFlowAgreeRefundProtocol.getDataFromServer(onAgreeRefundFinished);
    }

    /**
     * 十一、[服务]-服务方不同意退款并申请平台介入
     *
     * @param onInterventionFinished
     * @param soid                   服务订单ID
     * @param remark                 申请平台介入原因  这个字段好像可以不写
     */
    public static void serviceComplain(BaseProtocol.IResultExecutor onInterventionFinished, String soid, String remark) {
        ServiceFlowComplainProtocol serviceFlowComplainProtocol = new ServiceFlowComplainProtocol(soid, remark);
        serviceFlowComplainProtocol.getDataFromServer(onInterventionFinished);
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

    /**
     * 十三、[服务]-查看服务订单信息和状态
     *
     * @param onGetServiceOrderInfoFinished
     * @param soid
     */
    public static void getServiceOrderInfo(BaseProtocol.IResultExecutor onGetServiceOrderInfoFinished, String soid) {
        ServiceOrderInfoProtocol serviceOrderInfoProtocol = new ServiceOrderInfoProtocol(soid);
        serviceOrderInfoProtocol.getDataFromServer(onGetServiceOrderInfoFinished);
    }

    /**
     * 十六、[需求]-服务方淘汰某需求方
     */
    public static void noAccept(BaseProtocol.IResultExecutor onNoAcceptFinished, String soid, String uid) {
        ServiceFlowNoAcceptProtocol serviceFlowNoAcceptProtocol = new ServiceFlowNoAcceptProtocol(soid, uid);
        serviceFlowNoAcceptProtocol.getDataFromServer(onNoAcceptFinished);
    }

    public static void getServiceFlowLog(BaseProtocol.IResultExecutor onGetLogFinished, String soid) {
        GetServiceFlowLogProtocol getServiceFlowLogProtocol = new GetServiceFlowLogProtocol(soid);
        getServiceFlowLogProtocol.getDataFromServer(onGetLogFinished);
    }

    /**
     * 预约服务者 延期支付
     */
    public static void delayPay(BaseProtocol.IResultExecutor onDelayPayFinished, String soid, String fid) {
        ServiceDelayPayProtocol serviceDelayPayProtocol = new ServiceDelayPayProtocol(soid, fid);
        serviceDelayPayProtocol.getDataFromServer(onDelayPayFinished);
    }

    /**
     * 一、[推荐]-发布服务推荐给需求者  发布服务成功页面，推荐需求方
     *
     * @param onRecommendDemandUserFinished
     * @param id                            服务详情ID
     * @param limit                         一次拉取限制
     */
    public static void getRecommendDemandUser(BaseProtocol.IResultExecutor onRecommendDemandUserFinished, String id, String limit) {
        RecommendDemandUserProtocol recommendDemandUserProtocol = new RecommendDemandUserProtocol(id, limit);
        recommendDemandUserProtocol.getDataFromServer(onRecommendDemandUserFinished);
    }

    /**
     * 服务详情页 收藏服务
     */
    public static void collectService(BaseProtocol.IResultExecutor onCollectServiceFinished, String serviceId) {
        CollectTaskProtocol collectTaskProtocol = new CollectTaskProtocol(serviceId, "2");
        collectTaskProtocol.getDataFromServer(onCollectServiceFinished);
    }

    /**
     * 二、[推荐]-服务详情中更多服务列表
     *
     * @param onGetRecommendDataFinished
     * @param id                         服务明细ID
     * @param limit                      一次拉取限制
     */
    public static void getDetailRecommendService(BaseProtocol.IResultExecutor onGetRecommendDataFinished, String id, String limit) {
        DetailRecommendServiceProtocol detailRecommendServiceProtocol = new DetailRecommendServiceProtocol(id, limit);
        detailRecommendServiceProtocol.getDataFromServer(onGetRecommendDataFinished);
    }

    /**
     * 十七、[服务]-查询是否延期支付过
     *
     * @param onGetRectifyStatusFinished
     * @param soid                       服务订单ID
     */
    public static void getRectifyStatus(BaseProtocol.IResultExecutor onGetRectifyStatusFinished, String soid) {
        ServiceIsRectifyProtocol isRectifyProtocol = new ServiceIsRectifyProtocol(soid);
        isRectifyProtocol.getDataFromServer(onGetRectifyStatusFinished);
    }

}
