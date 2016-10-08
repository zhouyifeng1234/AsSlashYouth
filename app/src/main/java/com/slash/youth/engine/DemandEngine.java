package com.slash.youth.engine;

import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CancelDemandProtocol;
import com.slash.youth.http.protocol.DemandPartyPrePaymentProtocol;
import com.slash.youth.http.protocol.DemandPartySelectServicePartyProtocol;
import com.slash.youth.http.protocol.GetDemandFlowLogProtocol;
import com.slash.youth.http.protocol.MyPublishDemandListProtocol;
import com.slash.youth.http.protocol.PublishDemandProtocol;
import com.slash.youth.http.protocol.ServicePartyBidDemandProtocol;
import com.slash.youth.http.protocol.ServicePartyConfirmServantProtocol;
import com.slash.youth.http.protocol.ServicePartyRejectProtocol;

/**
 * Created by zhouyifeng on 2016/9/1.
 * 需求相关操作
 */
public class DemandEngine {


    /**
     * 发布需求
     */
    public static void publishDemand(BaseProtocol.IResultExecutor onPublishDemandFinished, String title, String label, String tasktime, String fighttime, String anonymity, String desc, String pic, String type, String proofbox, String invoice, String pattern, String place, String placedetail, String consume, String lng, String lat, String offer, String quote) {
        PublishDemandProtocol publishDemandProtocol = new PublishDemandProtocol(title, label, tasktime, fighttime, anonymity, desc, pic, type, proofbox, invoice, pattern, place, placedetail, consume, lng, lat, offer, quote);
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
     */
    public static void servicePartyBidDemand(BaseProtocol.IResultExecutor onBidDemandFinished, String id, String quote) {
        ServicePartyBidDemandProtocol servicePartyBidDemandProtocol = new ServicePartyBidDemandProtocol(id, quote);
        servicePartyBidDemandProtocol.getDataFromServer(onBidDemandFinished);
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
     * @param uid                      服务方UID
     */
    public static void servicePartyConfirmServant(BaseProtocol.IResultExecutor onConfirmServantFinished, String id, String uid) {
        ServicePartyConfirmServantProtocol servicePartyConfirmServantProtocol = new ServicePartyConfirmServantProtocol(id, uid);
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
     * 九、[需求]-需求方预支付
     *
     * @param onDemandPartyPrePaymentFinished
     * @param id                              需求ID
     */
    public static void demandPartyPrePayment(BaseProtocol.IResultExecutor onDemandPartyPrePaymentFinished, String id) {
        DemandPartyPrePaymentProtocol demandPartyPrePaymentProtocol = new DemandPartyPrePaymentProtocol(id);
        demandPartyPrePaymentProtocol.getDataFromServer(onDemandPartyPrePaymentFinished);
    }


}
