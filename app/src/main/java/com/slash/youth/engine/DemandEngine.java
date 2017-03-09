package com.slash.youth.engine;

import com.slash.youth.http.protocol.AgreeRefundProtocol;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CancelDemandProtocol;
import com.slash.youth.http.protocol.CollectTaskProtocol;
import com.slash.youth.http.protocol.DelayPayProtocol;
import com.slash.youth.http.protocol.DemandDetailProtocol;
import com.slash.youth.http.protocol.DemandInstalmentListProtocol;
import com.slash.youth.http.protocol.DemandIsRectifyProtocol;
import com.slash.youth.http.protocol.DemandPartyConfirmCompleteProtocol;
import com.slash.youth.http.protocol.DemandPartyGetBidListProtocol;
import com.slash.youth.http.protocol.DemandPartyPrePaymentProtocol;
import com.slash.youth.http.protocol.DemandPartySelectServicePartyProtocol;
import com.slash.youth.http.protocol.DemandPurposeProtocol;
import com.slash.youth.http.protocol.DemandRefundProtocol;
import com.slash.youth.http.protocol.DemandThirdPayProtocol;
import com.slash.youth.http.protocol.DemandUpdateBidProtocol;
import com.slash.youth.http.protocol.DetailRecommendDemandProtocol;
import com.slash.youth.http.protocol.DownloadFileProtocol;
import com.slash.youth.http.protocol.EliminateProtocol;
import com.slash.youth.http.protocol.FileUploadProtocol;
import com.slash.youth.http.protocol.GetDemandDescProtocol;
import com.slash.youth.http.protocol.GetDemandFlowLogProtocol;
import com.slash.youth.http.protocol.InterventionProtocol;
import com.slash.youth.http.protocol.LoadBidInfoProtocol;
import com.slash.youth.http.protocol.MyPublishDemandListProtocol;
import com.slash.youth.http.protocol.MyPublishHistoryDemandListProtocol;
import com.slash.youth.http.protocol.PublishDemandProtocol;
import com.slash.youth.http.protocol.RecommendServiceUserProtocol;
import com.slash.youth.http.protocol.ServicePartyBidDemandProtocol;
import com.slash.youth.http.protocol.ServicePartyCompleteProtocol;
import com.slash.youth.http.protocol.ServicePartyConfirmServantProtocol;
import com.slash.youth.http.protocol.ServicePartyRejectProtocol;
import com.slash.youth.http.protocol.SetDemandDescProtocol;
import com.slash.youth.http.protocol.SetDemandRemarkProtocol;
import com.slash.youth.http.protocol.UpdateDemandProtocol;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/1.inputPasswordVisibility
 * 需求相关操作
 */
public class DemandEngine {


    /**
     * 发布需求
     */
    public static void publishDemand(BaseProtocol.IResultExecutor onPublishDemandFinished, String title, ArrayList<String> listTag, String starttime, String anonymity, String desc, ArrayList<String> listPic, String instalment, String bp, String pattern, String place, String placedetail, String lng, String lat, String offer, String quote) {
        PublishDemandProtocol publishDemandProtocol = new PublishDemandProtocol(title, listTag, starttime, anonymity, desc, listPic, instalment, bp, pattern, place, placedetail, lng, lat, offer, quote);
        publishDemandProtocol.getDataFromServer(onPublishDemandFinished);
    }

    /**
     * 需求方取消需求
     *
     * @param onCancelDemandFinished
     * @param cancelDemandId
     */
    public static void cancelDemand(BaseProtocol.IResultExecutor onCancelDemandFinished, String cancelDemandId) {
        CancelDemandProtocol cancelDemandProtocol = new CancelDemandProtocol(cancelDemandId);
        cancelDemandProtocol.getDataFromServer(onCancelDemandFinished);
    }

    /**
     * 获取我发布的需求列表
     *
     * @return
     */
    public static void getMyPublishDemandList(BaseProtocol.IResultExecutor onGetMyPublishDemandListFinished) {
        MyPublishDemandListProtocol myPublishDemandListProtocol = new MyPublishDemandListProtocol();
        myPublishDemandListProtocol.getDataFromServer(onGetMyPublishDemandListFinished);
    }

    /**
     * 服务方竞标需求
     *
     * @param onBidDemandFinished
     * @param id
     * @param quote
     * @param bidDemandInstalmentRatioList
     * @param bp
     * @param starttime
     */
    public static void servicePartyBidDemand(BaseProtocol.IResultExecutor onBidDemandFinished, String id, String quote, ArrayList<Double> bidDemandInstalmentRatioList, String bp, String starttime) {
        ServicePartyBidDemandProtocol servicePartyBidDemandProtocol = new ServicePartyBidDemandProtocol(id, quote, bidDemandInstalmentRatioList, bp, starttime);
        servicePartyBidDemandProtocol.getDataFromServer(onBidDemandFinished);
    }

    /**
     * 五、[需求]-加载抢单信息(用于抢单人加载自己的抢单信息)
     */
    public static void loadBidInfo(BaseProtocol.IResultExecutor onLoadBidInfoFinished, String id, String uid) {
        LoadBidInfoProtocol loadBidInfoProtocol = new LoadBidInfoProtocol(id, uid);
        loadBidInfoProtocol.getDataFromServer(onLoadBidInfoFinished);
    }


    /**
     * 五、[需求]-需求方选择服务方
     *
     * @param onSelectServicePartyFinished
     * @param id                           需求ID
     * @param uid                          服务方UID
     */
    public static void demandPartySelectServiceParty(BaseProtocol.IResultExecutor onSelectServicePartyFinished, String id, String uid) {
        DemandPartySelectServicePartyProtocol demandPartySelectServicePartyProtocol = new DemandPartySelectServicePartyProtocol(id, uid);
        demandPartySelectServicePartyProtocol.getDataFromServer(onSelectServicePartyFinished);
    }


    /**
     * 六、[需求]-服务方确认一个服务者
     *
     * @param onConfirmServantFinished
     * @param id                       需求ID
     */
    public static void servicePartyConfirmServant(BaseProtocol.IResultExecutor onConfirmServantFinished, String id) {
        ServicePartyConfirmServantProtocol servicePartyConfirmServantProtocol = new ServicePartyConfirmServantProtocol(id);
        servicePartyConfirmServantProtocol.getDataFromServer(onConfirmServantFinished);
    }


    /**
     * 七、[需求]-查看需求流程日志
     *
     * @param onGetDemnadFlowLogFinished
     * @param id                         需求ID
     */
    public static void getDemandFlowLog(BaseProtocol.IResultExecutor onGetDemnadFlowLogFinished, String id) {
        GetDemandFlowLogProtocol getDemandFlowLogProtocol = new GetDemandFlowLogProtocol(id);
        getDemandFlowLogProtocol.getDataFromServer(onGetDemnadFlowLogFinished);
    }

    /**
     * 八、[需求]-服务方拒绝
     *
     * @param onServicePartyRejectFinished
     * @param id                           需求ID
     */
    public static void servicePartyReject(BaseProtocol.IResultExecutor onServicePartyRejectFinished, String id) {
        ServicePartyRejectProtocol servicePartyRejectProtocol = new ServicePartyRejectProtocol(id);
        servicePartyRejectProtocol.getDataFromServer(onServicePartyRejectFinished);
    }


    /**
     * 九、[需求]-需求方预支付（余额支付）
     *
     * @param onDemandPartyPrePaymentFinished
     * @param id                              需求ID
     * @param amount
     * @param channel                         PAYMENT_TYPE_BALANCE= 0; PAYMENT_TYPE_ALIPAY = 1;PAYMENT_TYPE_WX = 2;
     * @param pass                            支付密码 这个是原始密码经过一次MD5
     */
    public static void demandPartyPrePayment(BaseProtocol.IResultExecutor onDemandPartyPrePaymentFinished, String id, String amount, String channel, String pass) {
        DemandPartyPrePaymentProtocol demandPartyPrePaymentProtocol = new DemandPartyPrePaymentProtocol(id, amount, channel, pass);
        demandPartyPrePaymentProtocol.getDataFromServer(onDemandPartyPrePaymentFinished);
    }

    /**
     * 第三方支付，获取charge字符串
     */
    public static void demandThirdPay(BaseProtocol.IResultExecutor onThirdPayFinished, String id, String amount, String channel) {
        DemandThirdPayProtocol demandThirdPayProtocol = new DemandThirdPayProtocol(id, amount, channel);
        demandThirdPayProtocol.getDataFromServer(onThirdPayFinished);
    }

    /**
     * 十、[需求]-我发布的历史需求列表
     *
     * @param onGetHistoryDemandListFinished
     * @param offset                         分页 >=0
     * @param limit                          分页 >=0 最大20 如果设置=0 则系统默认10
     */
    public static void getMyPublishHistoryDemandList(BaseProtocol.IResultExecutor onGetHistoryDemandListFinished, String offset, String limit) {
        MyPublishHistoryDemandListProtocol myPublishHistoryDemandListProtocol = new MyPublishHistoryDemandListProtocol(offset, limit);
        myPublishHistoryDemandListProtocol.getDataFromServer(onGetHistoryDemandListFinished);
    }

    /**
     * 十一、[需求]-服务方完成任务
     *
     * @param onServicePartyCompleteFinished
     * @param id                             需求ID
     */
    public static void servicePartyComplete(BaseProtocol.IResultExecutor onServicePartyCompleteFinished, String id, String fid) {
        ServicePartyCompleteProtocol servicePartyCompleteProtocol = new ServicePartyCompleteProtocol(id, fid);
        servicePartyCompleteProtocol.getDataFromServer(onServicePartyCompleteFinished);
    }

    /**
     * 十二、[需求]-服务方完成任务(应该是 需求方确认完成 ？？？)
     *
     * @param onConfirmCompleteFinished
     * @param id                        需求ID
     */
    public static void demandPartyConfirmComplete(BaseProtocol.IResultExecutor onConfirmCompleteFinished, String id, String fid) {
        DemandPartyConfirmCompleteProtocol demandPartyConfirmCompleteProtocol = new DemandPartyConfirmCompleteProtocol(id, fid);
        demandPartyConfirmCompleteProtocol.getDataFromServer(onConfirmCompleteFinished);
    }

    /**
     * 十三、[需求]-需求方查看竞标（抢需求服务者）列表
     *
     * @param onGetBidListFinished
     * @param id                   需求ID
     */
    public static void demandPartyGetBidList(BaseProtocol.IResultExecutor onGetBidListFinished, String id) {
        DemandPartyGetBidListProtocol demandPartyGetBidListProtocol = new DemandPartyGetBidListProtocol(id);
        demandPartyGetBidListProtocol.getDataFromServer(onGetBidListFinished);
    }

    /**
     * 十四、[需求]-加载需求描述信息
     *
     * @param onGetDemandDescFinished
     * @param id                      需求ID
     */
    public static void getDemandDesc(BaseProtocol.IResultExecutor onGetDemandDescFinished, String id) {
        GetDemandDescProtocol getDemandDescProtocol = new GetDemandDescProtocol(id);
        getDemandDescProtocol.getDataFromServer(onGetDemandDescFinished);
    }

    /**
     * 十五、[需求]-设置需求描述信息
     *
     * @param onSetDemandDescFinished
     * @param id                      需求ID	>=0
     * @param desc                    需求描述信息	长度小于4096，该接口只能在服务方未确认前修改，服务方一旦确认该接口设置失效
     */
    public static void setDemandDesc(BaseProtocol.IResultExecutor onSetDemandDescFinished, String id, String desc) {
        SetDemandDescProtocol setDemandDescProtocol = new SetDemandDescProtocol(id, desc);
        setDemandDescProtocol.getDataFromServer(onSetDemandDescFinished);
    }

    /**
     * 十五、[需求]-设置需求备注信息
     *
     * @param onSetRemarkdFinished
     * @param id                   需求ID
     * @param remark               需求备注 长度小于4096，该接口只能在服务方未确认前修改，服务方一旦确认该接口设置失效
     */
    public static void setDemandRemark(BaseProtocol.IResultExecutor onSetRemarkdFinished, String id, String remark) {
        SetDemandRemarkProtocol setDemandRemarkProtocol = new SetDemandRemarkProtocol(id, remark);
        setDemandRemarkProtocol.getDataFromServer(onSetRemarkdFinished);
    }

    /**
     * 十六、[需求]-需求方获取意向单列表
     *
     * @param onGetPurposeListFinished
     * @param id                       需求ID
     */
    public static void getDemandPurposeList(BaseProtocol.IResultExecutor onGetPurposeListFinished, String id) {
        DemandPurposeProtocol demandPurposeProtocol = new DemandPurposeProtocol(id);
        demandPurposeProtocol.getDataFromServer(onGetPurposeListFinished);
    }

    /**
     * 十七、[需求]-需求方申请退款
     *
     * @param onRefundFinished
     * @param id               需求ID
     * @param reason           按照目前的客户端逻辑，reason和reasondetail至少传其中一个字段
     * @param reasondetail     按照目前的客户端逻辑，reason和reasondetail至少传其中一个字段
     */
    public static void refund(BaseProtocol.IResultExecutor onRefundFinished, String id, String reason, String reasondetail) {
        DemandRefundProtocol demandRefundProtocol = new DemandRefundProtocol(id, reason, reasondetail);
        demandRefundProtocol.getDataFromServer(onRefundFinished);
    }

    /**
     * 十八、[需求]-服务方确认同意退款
     *
     * @param onAgreeRefundFinished
     * @param id                    需求ID
     */
    public static void servicePartyAgreeRefund(BaseProtocol.IResultExecutor onAgreeRefundFinished, String id) {
        AgreeRefundProtocol agreeRefundProtocol = new AgreeRefundProtocol(id);
        agreeRefundProtocol.getDataFromServer(onAgreeRefundFinished);
    }

    /**
     * 十九、[需求]-服务方不同意退款并申请平台介入
     *
     * @param onInterventionFinished
     * @param id                     需求ID
     * @param remark                 申请平台介入原因 这个字段好像不用填，暂时先留着
     */
    public static void servicePartyIntervention(BaseProtocol.IResultExecutor onInterventionFinished, String id, String remark) {
        InterventionProtocol interventionProtocol = new InterventionProtocol(id, remark);
        interventionProtocol.getDataFromServer(onInterventionFinished);
    }

    /**
     * [需求]-需求方要求延期付款接口
     *
     * @param onDelayPayFinished
     * @param id                 需求ID
     * @param fid                当前第几期（延期支付只能为最后一期）
     */
    public static void delayPay(BaseProtocol.IResultExecutor onDelayPayFinished, String id, String fid) {
        DelayPayProtocol delayPayProtocol = new DelayPayProtocol(id, fid);
        delayPayProtocol.getDataFromServer(onDelayPayFinished);
    }


    //一、[文件]-图片上传
    public static void uploadFile(BaseProtocol.IResultExecutor onUploadFileFinished, String filePath) {
        FileUploadProtocol fileUploadProtocol = new FileUploadProtocol(filePath);
        fileUploadProtocol.getDataFromServer(onUploadFileFinished);
    }

    //二、[文件]-图片下载
    public static void downloadFile(BaseProtocol.IResultExecutor onDownloadFileFinished, String fileId) {
        DownloadFileProtocol downloadFileProtocol = new DownloadFileProtocol(fileId);
        downloadFileProtocol.getDataFromServer(onDownloadFileFinished);
    }

    /**
     * 二、[需求]-需求方淘汰服务者
     *
     * @param onEliminateFinished
     * @param id                  需求ID
     * @param uid                 服务者ID
     */
    public static void demandEliminateService(BaseProtocol.IResultExecutor onEliminateFinished, String id, String uid) {
        EliminateProtocol eliminateProtocol = new EliminateProtocol(id, uid);
        eliminateProtocol.getDataFromServer(onEliminateFinished);
    }

    /**
     * 二、[需求]-查看需求详情
     *
     * @param onGetDemandDetailFinished
     * @param id                        需求ID
     */
    public static void getDemandDetail(BaseProtocol.IResultExecutor onGetDemandDetailFinished, String id) {
        DemandDetailProtocol demandDetailProtocol = new DemandDetailProtocol(id);
        demandDetailProtocol.getDataFromServer(onGetDemandDetailFinished);
    }

    /**
     * 三、[需求]-修改需求
     */
    public static void updateDemand(BaseProtocol.IResultExecutor onUpdateDemandFinished, String id, String title, ArrayList<String> listTag, String starttime, String anonymity, String desc, ArrayList<String> listPic, String instalment, String bp, String pattern, String place, String placedetail, String lng, String lat, String offer, String quote) {
        UpdateDemandProtocol updateDemandProtocol = new UpdateDemandProtocol(id, title, listTag, starttime, anonymity, desc, listPic, instalment, bp, pattern, place, placedetail, lng, lat, offer, quote);
        updateDemandProtocol.getDataFromServer(onUpdateDemandFinished);
    }

    /**
     * 一、[推荐]-需求订单页服务者推荐接口   发布需求成功页面，服务者推荐
     *
     * @param onRecommendServiceUserFinished
     * @param id                             需求详情ID
     * @param limit                          一次拉取限制
     */
    public static void getRecommendServiceUser(BaseProtocol.IResultExecutor onRecommendServiceUserFinished, String id, String limit) {
        RecommendServiceUserProtocol recommendServiceUserProtocol = new RecommendServiceUserProtocol(id, limit);
        recommendServiceUserProtocol.getDataFromServer(onRecommendServiceUserFinished);
    }

    /**
     * 需求详情页，服务方视角，收藏需求
     *
     * @param onCollectDemandFinished
     * @param demandId
     */
    public static void collectDemand(BaseProtocol.IResultExecutor onCollectDemandFinished, String demandId) {
        CollectTaskProtocol collectDemandProtocol = new CollectTaskProtocol(demandId, "1");
        collectDemandProtocol.getDataFromServer(onCollectDemandFinished);
    }

    public static void getDetailRecommendDemand(BaseProtocol.IResultExecutor onGetRecommendDataFinished, String id, String limit) {
        DetailRecommendDemandProtocol detailRecommendDemandProtocol = new DetailRecommendDemandProtocol(id, limit);
        detailRecommendDemandProtocol.getDataFromServer(onGetRecommendDataFinished);
    }

    /**
     * 获取需求的分期信息
     *
     * @param onGetInstalmentListFinished
     * @param id                          需求ID
     */
    public static void getDemandInstalmentList(BaseProtocol.IResultExecutor onGetInstalmentListFinished, String id) {
        DemandInstalmentListProtocol demandInstalmentListProtocol = new DemandInstalmentListProtocol(id);
        demandInstalmentListProtocol.getDataFromServer(onGetInstalmentListFinished);
    }

    /**
     * 二十四、[需求]-查询是否延期支付过
     *
     * @param onGetRectifyStatusFinished
     * @param id                         需求ID
     */
    public static void getRectifyStatus(BaseProtocol.IResultExecutor onGetRectifyStatusFinished, String id) {
        DemandIsRectifyProtocol demandIsRectifyProtocol = new DemandIsRectifyProtocol(id);
        demandIsRectifyProtocol.getDataFromServer(onGetRectifyStatusFinished);
    }

    /**
     * 需求流程中服务方修改抢单信息
     */
    public static void updateBid(BaseProtocol.IResultExecutor onUpdateBidFinished, String id, String quote, ArrayList<Double> instalment, String bp, String starttime) {
        DemandUpdateBidProtocol demandUpdateBidProtocol = new DemandUpdateBidProtocol(id, quote, instalment, bp, starttime);
        demandUpdateBidProtocol.getDataFromServer(onUpdateBidFinished);
    }
}
